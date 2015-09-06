package ru.red_batmen.anotherwidget;


import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;
import android.widget.Button;

/**
 * Created by red on 17.08.15.
 */
public class SettingsActivity extends PreferenceActivity {

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;

    final String LOG_TAG = "Settings_activity";

    static final String PREFERENCE_CITY_ID = "krdWeatherCityid";
    static final String PREFERENCE_WIDGET_COLOR = "krdWeatherColor";
    public final static int WIDGET_DFAULT_COLOR = Color.parseColor("#ee6b00ee");

    //Button btnSaveConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // и проверяем его корректность
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        //addPreferencesFromResource(R.xml.settings);
        PreferenceScreen rootScreen = getPreferenceManager().createPreferenceScreen(this);
        // говорим Activity, что rootScreen - корневой
        setPreferenceScreen(rootScreen);

        String[] cityEntries = getResources().getStringArray(R.array.cities_titles);
        String[] cityValues = getResources().getStringArray(R.array.cities_values);

        ListPreference cityListPreference = new ListPreference(this);
        cityListPreference.setKey(SettingsActivity.PREFERENCE_CITY_ID + widgetID);
        cityListPreference.setEntries(cityEntries);
        cityListPreference.setEntryValues(cityValues);
        cityListPreference.setTitle("Город");
        cityListPreference.setSummary("Выберите город");

        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        //String defValue = sp.getString(SettingsActivity.PREFERENCE_CITY_ID + widgetID, String.valueOf(R.string.default_city_id));
        //cityListPreference.setDefaultValue(Integer.valueOf(defValue));

        rootScreen.addPreference(cityListPreference);

        setResult(RESULT_OK, new Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID));

        /*
        btnSaveConfig = (Button) findViewById(R.id.btnSaveConfig);
        btnSaveConfig.setOnClickListener(this);
        */
    }

    /*
    public void onClick(View v) {
        // Записываем значения с экрана в Preferences
        SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(WIDGET_COLOR + widgetID, 21);
        editor.commit();

        // положительный ответ
        setResult(RESULT_OK, resultValue);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        WeatherKrdWidget.updateWidget(this, appWidgetManager, widgetID, sp);

        finish();
    }
    */

}
