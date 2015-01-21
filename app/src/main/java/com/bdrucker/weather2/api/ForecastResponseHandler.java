package com.bdrucker.weather2.api;

import com.bdrucker.weather2.data.Forecast;
import com.bdrucker.weather2.data.FutureForecast;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Handles parsing API responses and dispatching start/failure events.
 */
public class ForecastResponseHandler extends AsyncHttpResponseHandler {
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-d");
    private final ForecastClient.ForecastListener listener;

    /**
     * Constructor.
     *
     * @param listener Used for callbacks on API request/response events.  Required.
     */
    public ForecastResponseHandler(ForecastClient.ForecastListener listener) {
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
            listener.onFailure(ForecastClient.FailureReasonEnum.ERROR_SERVICE, statusCode, responseString);
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
        final JSONObject currentWeatherNode = getFirstArrayElementIfExists(weatherNode, "curren_weather");  // Type-o in API.

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

        final JSONObject childNode = getFirstArrayElementIfExists(node, childNodeName);
        if (childNode != null)
            parseCommonNodes(childNode, builder);

        return builder.build();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
        // TODO: check for socket timeout.
        final String responseString = new String(errorResponse);
        listener.onFailure(ForecastClient.FailureReasonEnum.ERROR_SERVICE, statusCode, responseString);
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
                date = DATE_FORMAT.parse(dateString);
            } catch (ParseException e) {
                // Ignore.
            }
        }
        return date;
    }
}
