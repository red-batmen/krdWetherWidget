package ru.red_batmen.anotherwidget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by red on 17.08.15.
 */
public class SettingsActivity extends Activity implements View.OnClickListener {

    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;

    final String LOG_TAG = "Settings_activity";

    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_COLOR = "widget_color";
    public final static int WIDGET_DFAULT_COLOR = Color.parseColor("#ee6b00ee");

    Button btnSaveConfig;

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

        // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);

        // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);

        setContentView(R.layout.settings);

        btnSaveConfig = (Button) findViewById(R.id.btnSaveConfig);
        btnSaveConfig.setOnClickListener(this);
    }

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

}
