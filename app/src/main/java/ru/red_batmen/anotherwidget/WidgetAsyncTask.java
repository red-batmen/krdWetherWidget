package ru.red_batmen.anotherwidget;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by red on 08.07.15.
 */
public class WidgetAsyncTask extends AsyncTask<Integer, Integer, WeatherUnit> {

    final String LOG_WEATHER_TASK = "";

    private Context context;

    public WidgetAsyncTask(Context context){
        this.context = context;
    }

    public final String weatherUrlPath = "http://export.yandex.ru/weather-ng/forecasts/";

    /*
    лист для храннеия картинки погоды.
    забить самому - брать заранее подготовленный например из бд
    чем больше индекс тем хуже погода
    выбираем по значению и сравниваем индексы
    возвращаем самую плохую погоду
     */

    private WeatherUnit weather;

    public AsyncResponse delegate = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected WeatherUnit doInBackground(Integer... params) {
        if (params.length > 0) {
            weather = getNearestWeather(params[0]);
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(WeatherUnit result) {
        super.onPostExecute(result);
        delegate.processFinish(weather);
    }

    private WeatherUnit getNearestWeather(int cityId) {
        String  urlPath = weatherUrlPath + cityId + ".xml";

        weather = new WeatherUnit();
        weather.setNowWeatherDegree(context.getString(R.string.default_temperature));

        try {
            //получим xml
            URL url = new URL(urlPath);
            URLConnection conn = url.openConnection();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(conn.getInputStream());

            NodeList nodes, list, dayParts, tempData;
            String dayTemperature, // = context.getString(R.string.default_temperature),
                imageWeather;

            //погода на сегодня
            nodes = doc.getElementsByTagName("fact");
            if (nodes.getLength() == 1) {
                Element line = (Element) nodes.item(0);

                //температура
                list = line.getElementsByTagName("temperature");
                if (list.getLength() > 0) {
                    Element elementLine = (Element) list.item(0);
                    weather.setNowWeatherDegree(elementLine.getTextContent());

                    if (weather.getNowWeatherDegree().contains("-")) {
                        weather.setNowWeatherSign("-");
                    }
                }

                //влажность
                list = line.getElementsByTagName("humidity");
                if (list.getLength() > 0) {
                    Element elementLine = (Element) list.item(0);
                    weather.setNowWeatherHumidity(elementLine.getTextContent());
                }

                //картинка погоды
                list = line.getElementsByTagName("image-v3");
                Element elementLine = (Element) list.item(0);
                weather.setNowWeatherImage(elementLine.getTextContent());
            }

            //погода на завтра и послезавтра
            nodes = doc.getElementsByTagName("day");
            for (int nodeI = 1; nodeI < 3; nodeI++) {

                Element dayNode = (Element) nodes.item(nodeI);

                dayParts = dayNode.getElementsByTagName("day_part");

                Element partNode = (Element) dayParts.item(4);

                /*
                for (int partI = 0; partI < dayParts.getLength(); partI++) {
                    Element partNode = (Element) dayParts.item(partI);

                    if (!partNode.getAttribute("type").equals("day_short")){
                        continue;
                    }
                    */

                    //градусы
                    tempData = partNode.getElementsByTagName("temperature");

                    Element el = (Element) tempData.item(0);
                    //el = (Element) el.getElementsByTagName("avg").item(0);
                    dayTemperature = el.getTextContent();

                    //картинка погоды
                    tempData = partNode.getElementsByTagName("image-v3");
                    Element elementLine = (Element) tempData.item(0);

                    imageWeather = elementLine.getTextContent();
                //}

                //завтра
                if (nodeI == 1) {
                    weather.setTomorrowWeatherDegree(dayTemperature);
                    if (weather.getTomorrowWeatherDegree().contains("-")) {
                        weather.setTomorrowWeatherSign("-");
                    }

                    weather.setTomorrowWeatherImage(imageWeather);
                } else {
                    //после завтра
                    weather.setAfterTomorrowWeatherDegree(dayTemperature);
                    if (weather.getAfterTomorrowWeatherDegree().contains("-")) {
                        weather.setAfterTomorrowWeatherSign("-");
                    }

                    weather.setAfterTomorrowWeatherImage(imageWeather);

                    break;
                }

            }

            weather.hasError = false;
        } catch (MalformedURLException e) {
            Log.d(LOG_WEATHER_TASK, e.getMessage(), e);
            weather.setErrorText(e.getMessage());
        } catch (IOException e) {
            Log.d(LOG_WEATHER_TASK, e.getMessage(), e);
            weather.setErrorText("Нет интернета");
        } catch (ParserConfigurationException e) {
            Log.d(LOG_WEATHER_TASK, e.getMessage(), e);
            weather.setErrorText(e.getMessage());
        } catch (SAXException e) {
            e.printStackTrace();
            weather.setErrorText(e.getMessage());
        }

        return weather;
    }

    public WeatherUnit getWeather() {
        return weather;
    }

    public void setWeather(WeatherUnit weather) {
        this.weather = weather;
    }

}
