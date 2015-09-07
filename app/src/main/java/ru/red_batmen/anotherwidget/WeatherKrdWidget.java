package ru.red_batmen.anotherwidget;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.audiofx.BassBoost;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

//http://yandex.st/weather/1.1.78/i/icons/48x48/<?php echo $value1['image']; ?>.png
//http://wptrafficanalyzer.in/blog/android-app-widget-with-configuration-activity/
//http://maarkus.ru/wp-content/uploads/projects/weather/example/
//http://export.yandex.ru/weather-ng/forecasts/34929.xml

/**
 * Created by red on 08.07.15.
 */
public class WeatherKrdWidget extends AppWidgetProvider {

    final String LOG_TAG = "Weather_Krd_Widget";

    public static int CITY_ID_KRASNODAR = 34929;

    private static String[] citiesEntries = null;
    private static String[] citiesValues = null;

    private static Context context;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        this.context = context;

        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id, sp);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));

        // Удаляем Preferences
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

        for (int widgetID : appWidgetIds) {
            editor.remove(SettingsActivity.PREFERENCE_WIDGET_BG_COLOR + widgetID);
            editor.remove(SettingsActivity.PREFERENCE_WIDGET_FONT_COLOR + widgetID);
            editor.remove(SettingsActivity.PREFERENCE_CITY_ID + widgetID);
        }
        editor.commit();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    public static Integer getArrayKey(String[] array, String value){
        Integer key = null;

        for(int i=0; i<array.length; i++){
            if (array[i].equals(value)){
                key = i;
                break;
            }
        }

        return key;
    }

    public static void updateWidget(final Context context, final AppWidgetManager appWidgetManager,
                              final int widgetId, final SharedPreferences sp) {

        String strCityId = sp.getString(SettingsActivity.PREFERENCE_CITY_ID + widgetId, String.valueOf(WeatherKrdWidget.CITY_ID_KRASNODAR));

        Integer key = getArrayKey(getCitiesValues(), strCityId);

        if (key == null){
            Toast.makeText(context, "Неверный город", Toast.LENGTH_LONG);
            return;
        }

        int cityId = Integer.valueOf(strCityId);

        final String cityText = getCitiesEntries()[key];

        //берем по id параметры температуры и т.д.
        WidgetAsyncTask wt = new WidgetAsyncTask(context);
        wt.execute(cityId);

        wt.delegate = new AsyncResponse() {

            private String cityText;

            private int defineImageWeather(String input) {
                int resource = R.drawable.skc_d;

                if (input != null){
                    if (input.equals("bkn_n")) {
                        resource = R.drawable.bkn_n;
                    } else if (input.equals("bkn_ra_n")) {
                        resource = R.drawable.bkn_ra_n;
                    } else if (input.equals("ovc")) {
                        resource = R.drawable.ovc;
                    } else if (input.equals("ovc_ra")) {
                        resource = R.drawable.ovc_ra;
                    } else if (input.equals("ovc_ts_ra")) {
                        resource = R.drawable.ovc_ts_ra;
                    } else if (input.equals("skc_n")) {
                        resource = R.drawable.skc_n;
                    }
                }

                return resource;
            }

            @Override
            public void processFinish(WeatherUnit weather) {
                RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);

                if (weather.hasError){

                    Toast.makeText(context, weather.getErrorText(), Toast.LENGTH_LONG);
                    return;
                }

                //сегодня
                widgetView.setTextViewText(R.id.text_temperature_today,
                        weather.getNowWeatherSign() +
                                " " +
                                weather.getNowWeatherDegreeUnsigned() +
                                " " +
                                "\u00B0");
                widgetView.setTextViewText(R.id.text_city, cityText);
                widgetView.setTextViewText(R.id.text_hummidity, weather.getNowWeatherHumidity());
                widgetView.setImageViewResource(R.id.image_weather_today, defineImageWeather(weather.getNowWeatherImage()));


                //завтра
                widgetView.setTextViewText(R.id.text_temperature_tomorow,
                        weather.getTomorrowWeatherSign() +
                                " " +
                                weather.getTomorrowWeatherDegreeUnsigned() +
                                "\u00B0");

                widgetView.setTextViewText(R.id.text_date_tomorow, weather.getTomorrowDayWeekRu());

                widgetView.setImageViewResource(R.id.image_weather_tomorow, defineImageWeather(weather.getTomorrowWeatherImage()));

                //после завтра
                widgetView.setTextViewText(R.id.text_temperature_after_tomorow,
                        weather.getAfterTomorrowWeatherSign() +
                                " " +
                                weather.getAfterTomorrowWeatherDegreeUnsigned() +
                                "\u00B0");

                widgetView.setTextViewText(R.id.text_date_after_tomorow, weather.getAfterTomorrowDayWeekRu());

                widgetView.setImageViewResource(R.id.image_weather_after_tomorow, defineImageWeather(weather.getAfterTomorrowWeatherImage()));

                int widgetBgColor = sp.getInt(SettingsActivity.PREFERENCE_WIDGET_BG_COLOR + widgetId, SettingsActivity.WIDGET_DFAULT_COLOR);
                int widgetFontColor = sp.getInt(SettingsActivity.PREFERENCE_WIDGET_FONT_COLOR + widgetId, Color.WHITE);
                widgetView.setInt(R.id.main_layout, "setBackgroundColor", widgetBgColor);
                //widgetView.setInt(R.id.main_layout, "setTextColor", widgetFontColor);

                appWidgetManager.updateAppWidget(widgetId, widgetView);
            }
        };
    }

    public static String[] getCitiesEntries() {
        if (citiesEntries == null){
            citiesEntries = context.getResources().getStringArray(R.array.cities_titles);
        }

        return citiesEntries;
    }

    public static String[] getCitiesValues() {
        if (citiesValues == null){
            citiesValues = context.getResources().getStringArray(R.array.cities_values);
        }

        return citiesValues;
    }

}
