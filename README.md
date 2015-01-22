# Weather2

This Android application fetches weather prediction ddata using the
2-day API at http://www.weather2.com.  By default, the app displays a
forecast for the 97206 postal code, shown in metric units.  There are
two tabs which bring the user to either the current weather page or
the 2-day future forecast.

The minimum SDK version is 11 and it was built with Android Studio
1.0.1.  (I don't have 1.0.2 installed, but it should work with 1.0.2
tools).

Some notable features:

* Multi-screen layouts using multiple resource qualifications such as
  layout "land" and "w600dp".
* A settings page in which users can change the postal code and units
  of measurement (metric or imperial).
* No new service calls are required when changing units of
  measurement.
* Error handling for network issues (no mobile data coverage, broken
  proxies) and for server errors (unrecognized postal codes)
* Saved instance state to keep tract of the current page, even with
  the "Don't keep activities" developer options.
* Positive and negative unit tests (not enough, but some).
* Service calls are throttled to once every thirty minutes, and only
  when the app is in the foreground.
* Postman REST tool confiuration to test the weather2 service, posted
  here: https://www.getpostman.com/collections/51ac37efb2a8856622fa.
