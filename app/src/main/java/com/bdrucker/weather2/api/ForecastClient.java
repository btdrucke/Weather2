package com.bdrucker.weather2.api;

import android.content.Context;

import com.bdrucker.weather2.data.Forecast;
import com.bdrucker.weather2.data.FutureForecast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import java.util.List;

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

    // HTTP client and handler used for the GET request.
    private final AsyncHttpClient client;
    private final ForecastResponseHandler responseHandler;

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
         * @param reason         Why the fetch has failed.
         * @param statusCode     HTTP status code of the response.
         * @param responseString Response body text.  May be null.
         */
        public void onFailure(FailureReasonEnum reason, int statusCode, String responseString);
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

        this.client = new AsyncHttpClient();
        this.responseHandler = new ForecastResponseHandler(listener);
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
}
