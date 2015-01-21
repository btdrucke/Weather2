package com.bdrucker.weather2.api;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.test.InstrumentationTestCase;

import com.bdrucker.weather2.data.Forecast;
import com.bdrucker.weather2.data.FutureForecast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ForecastResponseHandlerTest extends InstrumentationTestCase {

    Context context;
    Context testCaseContext;

    /**
     * Runs before test is started.
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Context of app under test.
        context = this.getInstrumentation().getTargetContext();
        // We set up our own context since our test assets are in this package.
        testCaseContext = this.getInstrumentation().getContext();
    }

    byte[] loadAsset(String fileName) {
        final AssetManager am = testCaseContext.getAssets();
        BufferedReader reader = null;
        String fileContents = null;
        try {
            reader = new BufferedReader(new InputStreamReader(am.open(fileName)));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line);
            fileContents = sb.toString();
        } catch (IOException e) {
            fail("Could not load test asset!");
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    fail("Could not close test asset!");
                }
        }
        return fileContents.getBytes();
    }

    class TestForecastListener implements ForecastClient.ForecastListener {

        boolean calledOnFetchStart = false;
        boolean calledOnSuccess = false;
        boolean calledOnFailure = false;
        Forecast testTodaysForecast;
        List<FutureForecast> testFutureForecasts;

        public TestForecastListener(Forecast todaysForecast, List<FutureForecast> futureForecasts) {
            testTodaysForecast = todaysForecast;
            testFutureForecasts = futureForecasts;
        }

        @Override
        public void onFetchStart() {
            assertFalse("Should only call onFetchStart once", calledOnFetchStart);
            assertTrue("Should not call anything before onFetchStart", !calledOnSuccess && !calledOnFailure);
            calledOnFetchStart = true;
        }

        @Override
        public void onSuccess(Forecast todaysForecast, List<FutureForecast> futureForecasts) {
            assertTrue("Should have called onFetchStart before onSuccess", calledOnFetchStart);
            assertTrue("Should not call onSuccess/onFailure before onSuccess", !calledOnSuccess && !calledOnFailure);
            calledOnSuccess = true;

            assertNotNull("This test should have failed", testTodaysForecast);
            assertNotNull("No current weather forecasts returned", todaysForecast);

            Resources resources = context.getResources();
            checkOneForecast(resources, testTodaysForecast, todaysForecast);

            assertNotNull("No future forecasts returned", futureForecasts);
            assertEquals("Wrong number of days of forecasts", testFutureForecasts.size(), futureForecasts.size());
            checkFutureForecasts(resources, futureForecasts);
        }

        private void checkOneForecast(Resources resources, Forecast testForecast, Forecast forecast) {
            assertEquals("Descriptions differ", testForecast.getDescription(resources), forecast.getDescription(resources));

            assertEquals("Night descriptions differ", testForecast.getNightDescription(resources), forecast.getNightDescription(resources));
            assertEquals("Icon resource IDs differ", testForecast.getIconId(), forecast.getIconId());
            assertEquals("Night icon resource IDs differ", testForecast.getNightIconId(), forecast.getNightIconId());

            assertEquals("Temperatures in metric units differ", testForecast.getTemperature(resources, true), forecast.getTemperature(resources, true));
            assertEquals("Temperatures in imperial units differ", testForecast.getTemperature(resources, false), forecast.getTemperature(resources, false));

            assertEquals("Wind speed in metric units differ", testForecast.getWindValue(resources, true), forecast.getWindValue(resources, true));
            assertEquals("Wind speed in imperial units differ", testForecast.getWindValue(resources, false), forecast.getWindValue(resources, false));

            assertEquals("Humidity values differ", testForecast.getHumidity(resources), forecast.getHumidity(resources));
            assertEquals("Pressure values differ", testForecast.getPressure(resources), forecast.getPressure(resources));
        }

        private void checkFutureForecasts(Resources resources, List<FutureForecast> forecasts) {
            for (int i = 0; i < testFutureForecasts.size(); ++i) {
                FutureForecast tf = testFutureForecasts.get(i);
                FutureForecast f = forecasts.get(i);
                assertEquals("Forecast dates on items[" + i + "] differ", tf.forecastDate, f.forecastDate);

                checkOneForecast(resources, tf.day, f.day);
                checkOneForecast(resources, tf.night, f.night);
            }
        }

        @Override
        public void onFailure(ForecastClient.FailureReasonEnum reason, int statusCode, String response) {
            assertTrue("Should have called onFetchStart before onFailure", calledOnFetchStart);
            assertTrue("Should not call onSuccess/onFailure before onFailure", !calledOnSuccess && !calledOnFailure);
            calledOnFailure = true;

            assertNull("This test should have succeeded", testTodaysForecast);
        }

        /**
         * Make sure that success or failure callbacks were called as expected
         */
        public void checkCalls() {
            assertTrue("This test should have failed", testTodaysForecast == null && !calledOnSuccess && calledOnFailure);
            assertNull("This test should have succeeded", testTodaysForecast != null && calledOnSuccess && !calledOnFailure);
        }
    }

    public void testStart() {

        TestForecastListener listener = new TestForecastListener(null, null);
        ForecastResponseHandler handler = new ForecastResponseHandler(listener);
        handler.onStart();
        handler.onSuccess(200, null, loadAsset("response_success.json"));
        listener.checkCalls();
    }
}