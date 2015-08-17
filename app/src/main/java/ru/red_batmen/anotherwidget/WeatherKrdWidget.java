package ru.red_batmen.anotherwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

//http://yandex.st/weather/1.1.78/i/icons/48x48/<?php echo $value1['image']; ?>.png

/**
 * Created by red on 08.07.15.
 */
public class WeatherKrdWidget extends AppWidgetProvider {

    final String LOG_TAG = "myLogs";

    WidgetAsyncTask wt;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));

        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    private void updateWidget(final Context context, final AppWidgetManager appWidgetManager, final int widgetId) {

        //берем по id параметры температуры и т.д.
        wt = new WidgetAsyncTask();
        wt.execute(34929);

        wt.delegate = new AsyncResponse() {

            private int defineImageWeather(String input) {
                int resource = R.drawable.sun;
                if (input.equals("skc_d")) {
                    Log.d(LOG_TAG, "skc_d");
                    resource = R.drawable.sun;
                    //солнце и дождь
                } else if (input.equals("bkn_ra_d")) {
                    Log.d(LOG_TAG, "bkn_ra_d");
                    resource = R.drawable.rain;
                } else {
                    Log.d(LOG_TAG, "other image");
                    resource = R.drawable.cloud;
                }

                return resource;
            }

            @Override
            public void processFinish(WeatherUnit weather) {
                RemoteViews widgetView = new RemoteViews(context.getPackageName(), R.layout.widget);

                //сегодня
                widgetView.setTextViewText(R.id.text_temperature_today,
                        weather.getNowWeatherSign() +
                                " " +
                                weather.getNowWeatherDegreeUnsigned() +
                                " " +
                                "\u00B0");
                widgetView.setTextViewText(R.id.text_hummidity, weather.getNowWeatherHumidity());
                widgetView.setImageViewResource(R.id.image_weather_today, defineImageWeather(weather.getNowWeatherImage()));


                //завтра
                widgetView.setTextViewText(R.id.text_temperature_tomorow,
                        weather.getTomorrowWeatherSign() +
                                " " +
                                weather.getTomorrowWeatherDegreeUnsigned() +
                                " " +
                                "\u00B0");
                widgetView.setImageViewResource(R.id.image_weather_tomorow, defineImageWeather(weather.getTomorrowWeatherImage()));

                //после завтра
                widgetView.setTextViewText(R.id.text_temperature_after_tomorow,
                        weather.getAfterTomorrowWeatherSign() +
                                " " +
                                weather.getAfterTomorrowWeatherDegreeUnsigned() +
                                " " +
                                "\u00B0");
                widgetView.setImageViewResource(R.id.image_weather_after_tomorow, defineImageWeather(weather.getAfterTomorrowWeatherImage()));

                appWidgetManager.updateAppWidget(widgetId, widgetView);
            }
        };
    }
}
