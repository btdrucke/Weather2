package com.bdrucker.weather2.api;

import android.content.Context;

import com.bdrucker.weather2.data.Forecast;
import com.bdrucker.weather2.data.FutureForecast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by bdrucker on 1/19/15.
 */
public class ForecastClient {

    // The weather2 service endpoint.
    private static final String SERVICE_URL = "http://www.myweather2.com/developer/forecast.ashx?output=json";

    // Query parameter names used by the weather2 API.
    private static final String KEY_ACCESS_CODE = "uac";
    private static final String KEY_TEMPERATURE_UNITS = "temp_unit";
    private static final String KEY_WIND_SPEED_UNITS = "wind_unit";
    private static final String KEY_POSTAL_CODE = "query";

    // Query parameter values for metric/imperial units
    private static final String VALUE_TEMPERATURE_UNITS_METRIC = "c";
    private static final String VALUE_WIND_SPEED_METRIC = "kph";

    // Calling context used for getting string resources, etc.
    private final Context context;

    // HTTP client and handler used for the GET request.
    private final AsyncHttpClient client;
    private final ResponseHandler responseHandler;

    /**
     * The reason why a forecast fetch has failed.
     * <ul>
     * <li><code>ERROR_NETWORK</code> - There has been a problem reaching the server, e.g. timeout.
     * <li><code>ERROR_SERVICE</code> - The service has returned as error.
     * </ul>
     */
    public enum FailureReasonEnum {
        ERROR_NETWORK, ERROR_SERVICE
    }

    public interface ForecastListener {
        /**
         * Forecast fetch has started.
         */
        public void onFetchStart();

        /**
         * Forecast fetch has completed successfully and weather data is available.
         *
         * @param todaysForecast  Today's weather.
         * @param futureForecasts List of forecasts for upcoming days.
         */
        public void onSuccess(Forecast todaysForecast, List<FutureForecast> futureForecasts);

        /**
         * Forecast fetch has failed for some reason.
         *
         * @param reason     Why the fetch has failed.
         * @param statusCode HTTP status code of the response.
         * @param response   Response body text.  May be null.
         */
        public void onFailure(FailureReasonEnum reason, int statusCode, String response);
    }

    /**
     * Fetches weather forecasts.
     *
     * @param context  Calling context, used internally to fetch resources.
     * @param listener Handler for fetch events such as start, success and failure.
     */
    public ForecastClient(Context context, ForecastListener listener) {
        if (context == null)
            throw new IllegalArgumentException("Context is required");

        if (listener == null)
            throw new IllegalArgumentException("Listener is required");

        this.context = context;
        this.client = new AsyncHttpClient();
        this.responseHandler = new ResponseHandler(listener);
    }

    /**
     * Fetch a new forecast for the given postal code.
     *
     * @param postalCode The postal code of the location to forecast.  Required.
     */
    public void get(String postalCode) {
        if (postalCode == null)
            throw new IllegalArgumentException("Postal code is required");

        final RequestParams params = new RequestParams();
        params.add(KEY_ACCESS_CODE, getAccessCodeValue());
        params.add(KEY_POSTAL_CODE, postalCode);

        // Always fetch in metric units.  We can convert later if need be.
        params.add(KEY_TEMPERATURE_UNITS, VALUE_TEMPERATURE_UNITS_METRIC);
        params.add(KEY_WIND_SPEED_UNITS, VALUE_WIND_SPEED_METRIC);

        client.get(SERVICE_URL, params, responseHandler);
    }

    /**
     * Get the weather2 API unique access code as a plain String.<br/>
     *
     * @return Access code.
     */
    private String getAccessCodeValue() {
        return "GyZ4eIgOgp";  //TODO: This should be obfuscated rather than stored in the binary in clear text.
    }

    private static class ResponseHandler extends AsyncHttpResponseHandler {
        // Used to call back on request progress.  Is never null.
        private final ForecastListener listener;

        private ResponseHandler(ForecastListener listener) {
            this.listener = listener;
        }

        @Override
        public void onStart() {
            listener.onFetchStart();
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
            Forecast forecast = null;
            List<FutureForecast> futureForecasts = null;

            final String responseString = new String(response);
            try {
                final JSONObject body = new JSONObject(responseString);
                final JSONObject weatherNode = body.optJSONObject("weather");
                if (weatherNode != null) {
                    forecast = getTodaysForecast(weatherNode);
                    futureForecasts = getFutureForecasts(weatherNode);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if ((forecast == null) || (futureForecasts == null) || futureForecasts.isEmpty())
                listener.onFailure(FailureReasonEnum.ERROR_SERVICE, statusCode, responseString);
            else
                listener.onSuccess(forecast, futureForecasts);
        }

        /**
         * Find the current weather info from a JSON tree.
         *
         * @param weatherNode The top-level "weather" node of the response JSON.
         * @return The weather forecast for today.
         */
        private Forecast getTodaysForecast(JSONObject weatherNode) {
            final JSONObject currentWeatherNode = weatherNode.optJSONObject("curren_weather");  // Type-o in API.

            final Forecast.Builder builder = new Forecast.Builder();
            builder.setForecastDate(new Date())
                    .setDegreesCelsius(getIntegerIfExists(currentWeatherNode, "temp"))
                    .setPressure(getIntegerIfExists(currentWeatherNode, "pressure"))
                    .setHumidity(getIntegerIfExists(currentWeatherNode, "humidity"));
            parseCommonNodes(currentWeatherNode, builder);

            return builder.build();
        }

        private void parseCommonNodes(JSONObject node, Forecast.Builder builder) {
            if (node != null) {
                builder.setWeatherCode(getIntegerIfExists(node, "weather_code"));
                final JSONObject windNode = getFirstArrayElementIfExists(node, "wind");
                if (windNode != null) {
                    builder.setWindDirection(windNode.optString("dir"))
                            .setWindKilometersPerHour(getIntegerIfExists(windNode, "speed"));
                }
            }
        }

        /**
         * Find a list of future weather forecasts from a JSON tree.
         *
         * @param weatherNode The top-level "weather" node of the response JSON.
         * @return Future weather forecasts for the next two days.
         */
        private List<FutureForecast> getFutureForecasts(JSONObject weatherNode) {
            final List<FutureForecast> forecasts = new ArrayList<>();

            final JSONArray forecastList = weatherNode.optJSONArray("forecast");
            if (forecastList == null)
                return forecasts;

            final int size = forecastList.length();
            for (int i = 0; i < size; ++i) {
                final JSONObject forecastNode = forecastList.optJSONObject(i);
                if (forecastNode == null)
                    continue;

                forecasts.add(new FutureForecast(
                        getDate(forecastNode, "date"),
                        getFutureForecast(forecastNode, "day_max_temp", "day"),
                        getFutureForecast(forecastNode, "night_min_temp", "night")));
            }

            return forecasts;
        }

        private Forecast getFutureForecast(JSONObject node, String tempName, String childNodeName) {
            final Forecast.Builder builder = new Forecast.Builder();
            builder.setDegreesCelsius(getIntegerIfExists(node, tempName));

            final JSONObject childNode = node.optJSONObject(childNodeName);
            if (childNode != null)
                parseCommonNodes(childNode, builder);

            return builder.build();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
            // TODO: check for socket timeout.
            final String responseString = new String(errorResponse);
            listener.onFailure(FailureReasonEnum.ERROR_SERVICE, statusCode, responseString);
        }

        private static Integer getIntegerIfExists(JSONObject node, String name) {
            Integer result = null;
            try {
                result = node.getInt(name);
            } catch (JSONException | NullPointerException e) {
                // Ignore.
            }
            return result;
        }

        private JSONObject getFirstArrayElementIfExists(JSONObject node, String name) {
            JSONObject result = null;
            try {
                final JSONArray array = node.getJSONArray(name);
                result = array.getJSONObject(0);
            } catch (JSONException | NullPointerException e) {
                // Ignore.
            }
            return result;
        }

        private Date getDate(JSONObject node, String name) {
            Date date = null;
            final String dateString = node.optString(name);
            if (dateString != null) {
                try {
                    date = DateFormat.getDateTimeInstance().parse(dateString);
                } catch (ParseException e) {
                    // Ignore.
                }
            }
            return date;
        }
    }
}
