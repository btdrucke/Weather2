# Weather2

This Android application fetches weather prediction data using the
2-day API at http://www.myweather2.com.  By default, the app displays
a forecast for the 97206 postal code, shown in metric units.  There
are two tabs which bring the user to either the current weather page
or the 2-day future forecast.

The minimum SDK version is 11 and it was built with Android Studio
1.0.1.  (I don't have 1.0.2 installed, but it should work with 1.0.2
tools).

### Some Notable Features

* Multi-screen layouts using multiple resource qualifications such as
  layout "land" and "w600dp".
* A settings page in which users can change the postal code and units
  of measurement (metric or imperial).
* No new service calls are required when changing units of
  measurement.
* Error handling for network issues (no mobile data coverage, broken
  proxies) and for server errors (unrecognized postal codes).
* Saved instance state to keep track of the current page, even with
  the "Don't keep activities" developer option.
* Positive and negative unit tests (not enough, but some).
* Service calls are throttled to once every thirty minutes, and only
  when the app is in the foreground.
* [Postman REST tool](https://chrome.google.com/webstore/detail/postman-rest-client/fdmmgilgnpjigdojojpjoooidkmcomcm?hl=en)
  configuration to test the weather2 service, posted here:
  https://www.getpostman.com/collections/51ac37efb2a8856622fa.

### Next Steps

This effort was intentionally time-boxed at two days.  As such, there
was not time for everything.  The next steps I would take on this
application if I were to continue development are:

* Custom tabs and animations.  Tabs currently are implemented with the
  support action bar.  This works and was quick, but a custom control
  with press-state effects and an animating underbar would look
  better.
* Better unit test coverage.  There is one test for a full success and
  one test for an utter failure, but there are middle cases that could
  use attention.  The service could return data that is well formed
  JSON but missing key elements of data.  I have coded some defense,
  and I have observed this to work in the cases where wind speed is
  sometimes unavailable.  But I have not seen a natural example of
  required data which is missing.  And automated test would help here.
* Test hooks for decreasing the half-hour service timeout would help
  test this feature.
