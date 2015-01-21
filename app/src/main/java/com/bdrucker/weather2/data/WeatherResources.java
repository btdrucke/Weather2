package com.bdrucker.weather2.data;

import android.util.SparseArray;

import com.bdrucker.weather2.R;

import java.util.HashMap;
import java.util.Map;

public class WeatherResources {

    private static SparseArray<WeatherCodeResourceSet> weatherCodeResources = new SparseArray<>();

    static {
        weatherCodeResources.append(0, new WeatherCodeResourceSet(R.string.weather_0_day, R.string.weather_0_night, R.drawable.sunny, R.drawable.clear));
        weatherCodeResources.append(1, new WeatherCodeResourceSet(R.string.weather_1, R.string.weather_1, R.drawable.partlycloudyday, R.drawable.partlycloudynight));
        weatherCodeResources.append(2, new WeatherCodeResourceSet(R.string.weather_2, R.string.weather_2, R.drawable.cloudy, R.drawable.cloudy));
        weatherCodeResources.append(3, new WeatherCodeResourceSet(R.string.weather_3, R.string.weather_3, R.drawable.overcast, R.drawable.overcast));
        weatherCodeResources.append(10, new WeatherCodeResourceSet(R.string.weather_10, R.string.weather_10, R.drawable.mist, R.drawable.mist));
        weatherCodeResources.append(21, new WeatherCodeResourceSet(R.string.weather_21, R.string.weather_21, R.drawable.occlightrain, R.drawable.occlightrain));
        weatherCodeResources.append(22, new WeatherCodeResourceSet(R.string.weather_22, R.string.weather_22, R.drawable.isosleetswrsday, R.drawable.isosleetswrsnight));
        weatherCodeResources.append(23, new WeatherCodeResourceSet(R.string.weather_23, R.string.weather_23, R.drawable.occlightsleet, R.drawable.occlightsleet));
        weatherCodeResources.append(24, new WeatherCodeResourceSet(R.string.weather_24, R.string.weather_24, R.drawable.freezingdrizzle, R.drawable.freezingdrizzle));
        weatherCodeResources.append(29, new WeatherCodeResourceSet(R.string.weather_29, R.string.weather_29, R.drawable.partcloudrainthunderday, R.drawable.partcloudrainthundernight));
        weatherCodeResources.append(38, new WeatherCodeResourceSet(R.string.weather_38, R.string.weather_38, R.drawable.modsnow, R.drawable.modsnow));
        weatherCodeResources.append(39, new WeatherCodeResourceSet(R.string.weather_39, R.string.weather_39, R.drawable.blizzard, R.drawable.blizzard));
        weatherCodeResources.append(45, new WeatherCodeResourceSet(R.string.weather_45, R.string.weather_45, R.drawable.fog, R.drawable.fog));
        weatherCodeResources.append(49, new WeatherCodeResourceSet(R.string.weather_49, R.string.weather_49, R.drawable.freezingfog, R.drawable.freezingfog));
        weatherCodeResources.append(50, new WeatherCodeResourceSet(R.string.weather_50, R.string.weather_50, R.drawable.isorainswrsday, R.drawable.isorainswrsnight));
        weatherCodeResources.append(51, new WeatherCodeResourceSet(R.string.weather_51, R.string.weather_51, R.drawable.occlightrain, R.drawable.occlightrain));
        weatherCodeResources.append(56, new WeatherCodeResourceSet(R.string.weather_56, R.string.weather_56, R.drawable.freezingdrizzle, R.drawable.freezingdrizzle));
        weatherCodeResources.append(57, new WeatherCodeResourceSet(R.string.weather_57, R.string.weather_57, R.drawable.freezingdrizzle, R.drawable.freezingdrizzle));
        weatherCodeResources.append(60, new WeatherCodeResourceSet(R.string.weather_60, R.string.weather_60, R.drawable.occlightrain, R.drawable.occlightrain));
        weatherCodeResources.append(61, new WeatherCodeResourceSet(R.string.weather_61, R.string.weather_61, R.drawable.modrain, R.drawable.modrain));
        weatherCodeResources.append(62, new WeatherCodeResourceSet(R.string.weather_62, R.string.weather_62, R.drawable.modrainswrsday, R.drawable.modrainswrsnight));
        weatherCodeResources.append(63, new WeatherCodeResourceSet(R.string.weather_63, R.string.weather_63, R.drawable.modrain, R.drawable.modrain));
        weatherCodeResources.append(64, new WeatherCodeResourceSet(R.string.weather_64, R.string.weather_64, R.drawable.heavyrainswrsday, R.drawable.heavyrainswrsnight));
        weatherCodeResources.append(65, new WeatherCodeResourceSet(R.string.weather_65, R.string.weather_65, R.drawable.heavyrain, R.drawable.heavyrain));
        weatherCodeResources.append(66, new WeatherCodeResourceSet(R.string.weather_66, R.string.weather_66, R.drawable.freezingrain, R.drawable.freezingrain));
        weatherCodeResources.append(67, new WeatherCodeResourceSet(R.string.weather_67, R.string.weather_67, R.drawable.freezingrain, R.drawable.freezingrain));
        weatherCodeResources.append(68, new WeatherCodeResourceSet(R.string.weather_68, R.string.weather_68, R.drawable.modsleet, R.drawable.modsleet));
        weatherCodeResources.append(69, new WeatherCodeResourceSet(R.string.weather_69, R.string.weather_69, R.drawable.heavysleet, R.drawable.heavysleet));
        weatherCodeResources.append(70, new WeatherCodeResourceSet(R.string.weather_70, R.string.weather_70, R.drawable.occlightsnow, R.drawable.occlightsnow));
        weatherCodeResources.append(71, new WeatherCodeResourceSet(R.string.weather_71, R.string.weather_71, R.drawable.occlightsnow, R.drawable.occlightsnow));
        weatherCodeResources.append(72, new WeatherCodeResourceSet(R.string.weather_72, R.string.weather_72, R.drawable.modsnow, R.drawable.modsnow));
        weatherCodeResources.append(73, new WeatherCodeResourceSet(R.string.weather_73, R.string.weather_73, R.drawable.modsnow, R.drawable.modsnow));
        weatherCodeResources.append(74, new WeatherCodeResourceSet(R.string.weather_74, R.string.weather_74, R.drawable.heavysnowswrsday, R.drawable.heavysnowswrsnight));
        weatherCodeResources.append(75, new WeatherCodeResourceSet(R.string.weather_75, R.string.weather_75, R.drawable.heavysnow, R.drawable.heavysnow));
        weatherCodeResources.append(79, new WeatherCodeResourceSet(R.string.weather_79, R.string.weather_79, R.drawable.freezingrain, R.drawable.freezingrain));
        weatherCodeResources.append(80, new WeatherCodeResourceSet(R.string.weather_80, R.string.weather_80, R.drawable.isorainswrsday, R.drawable.isorainswrsnight));
        weatherCodeResources.append(81, new WeatherCodeResourceSet(R.string.weather_81, R.string.weather_81, R.drawable.modrainswrsday, R.drawable.modrainswrsnight));
        weatherCodeResources.append(82, new WeatherCodeResourceSet(R.string.weather_82, R.string.weather_82, R.drawable.heavyrain, R.drawable.heavyrain));
        weatherCodeResources.append(83, new WeatherCodeResourceSet(R.string.weather_83, R.string.weather_83, R.drawable.modsleetswrsday, R.drawable.modsleetswrsnight));
        weatherCodeResources.append(84, new WeatherCodeResourceSet(R.string.weather_84, R.string.weather_84, R.drawable.modsleetswrsday, R.drawable.modsleetswrsnight));
        weatherCodeResources.append(85, new WeatherCodeResourceSet(R.string.weather_85, R.string.weather_85, R.drawable.isosnowswrsday, R.drawable.isosnowswrsnight));
        weatherCodeResources.append(86, new WeatherCodeResourceSet(R.string.weather_86, R.string.weather_86, R.drawable.modsnowswrsday, R.drawable.modsnowswrsnight));
        weatherCodeResources.append(87, new WeatherCodeResourceSet(R.string.weather_87, R.string.weather_87, R.drawable.freezingrain, R.drawable.freezingrain));
        weatherCodeResources.append(88, new WeatherCodeResourceSet(R.string.weather_88, R.string.weather_88, R.drawable.freezingrain, R.drawable.freezingrain));
        weatherCodeResources.append(91, new WeatherCodeResourceSet(R.string.weather_91, R.string.weather_91, R.drawable.partcloudrainthunderday, R.drawable.partcloudrainthundernight));
        weatherCodeResources.append(92, new WeatherCodeResourceSet(R.string.weather_92, R.string.weather_92, R.drawable.cloudrainthunder, R.drawable.cloudrainthunder));
        weatherCodeResources.append(93, new WeatherCodeResourceSet(R.string.weather_93, R.string.weather_93, R.drawable.partcloudsleetsnowthunderday, R.drawable.partcloudsleetsnowthundernight));
        weatherCodeResources.append(94, new WeatherCodeResourceSet(R.string.weather_94, R.string.weather_94, R.drawable.cloudsleetsnowthunder, R.drawable.cloudsleetsnowthunder));
    }

    private static Map<String, Integer> windResources = new HashMap<>();

    static {
        windResources.put("N", R.string.wind_value_N);
        windResources.put("NE", R.string.wind_value_NE);
        windResources.put("NW", R.string.wind_value_NW);
        windResources.put("NNE", R.string.wind_value_NNE);
        windResources.put("NNW", R.string.wind_value_NNW);
        windResources.put("S", R.string.wind_value_S);
        windResources.put("SE", R.string.wind_value_SE);
        windResources.put("SW", R.string.wind_value_SW);
        windResources.put("SSE", R.string.wind_value_SSE);
        windResources.put("SSW", R.string.wind_value_SSW);
        windResources.put("E", R.string.wind_value_E);
        windResources.put("ENE", R.string.wind_value_ENE);
        windResources.put("ESE", R.string.wind_value_ESE);
        windResources.put("W", R.string.wind_value_W);
        windResources.put("WNW", R.string.wind_value_WNW);
        windResources.put("WSW", R.string.wind_value_WSW);
    }

    public static class WeatherCodeResourceSet {
        final int dayStringId;
        final int nightStringId;
        final int dayIconId;
        final int nightIconId;

        WeatherCodeResourceSet(int dayStringId, int nightStringId, int dayIconId, int nightIconId) {
            this.dayStringId = dayStringId;
            this.nightStringId = nightStringId;
            this.dayIconId = dayIconId;
            this.nightIconId = nightIconId;
        }
    }

    public static WeatherCodeResourceSet getWeatherCodeResources(int weatherCode) {
        return weatherCodeResources.get(weatherCode);
    }

    public static Integer getWindResources(String windDirection) {
        return windResources.get(windDirection);
    }

    // No instantiation allowed.
    private WeatherResources() {
    }
}
