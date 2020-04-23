import java.io.IOException;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainApp implements Runnable {

    private Scanner scanner;
    private String city;
    private String zip;
    private String country;
    private String idMiasta;
    private String lat;
    private String lon;
    private String cnt;
    private double dni;
    private String miasto1;

    private void startApp() {
        scanner = new Scanner(System.in);
        System.out.println("Wybierz po czym chcesz znaleźć miejsce dla którego wyświetlisz pogodę \n0 - Zakończ działanie \n1 - Nazwa Miasta \n2 - Kod pocztowy \n5 - Pogoda dlugoterminowa");
        Integer name = scanner.nextInt();
        chooseTypeSearching(name);
    }

    private void chooseTypeSearching(Integer typeNumber)  {
        switch (typeNumber) {
            case 0:

                break;
            case 1:
                System.out.println("Podaj nazwe miasta");
                city = scanner.next();
                connectByCityName();
                startApp();
                break;
            case 2:
                System.out.println("Podaj państwo");
                country=scanner.next() ;
                System.out.println("Podaj kod pocztowy");
                zip=scanner.next();
                connectByZipCode();
                startApp();
                break;

            case 3:
                System.out.println("Podaj lat miasta");
                lat = scanner.next();
                System.out.println("Podaj lon miasta");
                lon = scanner.next();
                connectByCoord();
                startApp();
                break;

            case 4:
                System.out.println("Podaj nazwe miasta");
                city = scanner.next();
                System.out.printf("Podaj ilosć dni");
                cnt = scanner.next();
                connectByCityForXDays();
                startApp();
                break;
            case 5:
                liczbaDni();

                break;

        }
    }
    private void liczbaDni(){
        System.out.println("Podaj na ile chcesz wyswietlic pogode, zakres 1-5");
      int dni = scanner.nextInt();
      if(dni<=5&&dni>0){
      this.dni=dni;
          System.out.println("Podaj nazwe miasta");
          city = scanner.next();
          connectLongByCityName();
          startApp();
      }
      else{
          System.out.println("Wprowadziles zla ilosc dni");
          startApp();
              }
    }
    public String liczbaDni(int dni, String miasto){
        this.dni=dni;

        return connectLongByCityName(miasto);
    }

    public String connectLongByCityName(String miasto1){
        String response = null;
        try {

            response = new HttpService().connect(Config.APP_URL_DAILY + miasto1+"&units=metric&appid="+Config.APP_ID);
            parseJsonDaily(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

return response;
    }

    private void connectLongByCityName() {
        try {
            String response = new HttpService().connect(Config.APP_URL_DAILY +city+"&units=metric&appid="+Config.APP_ID);
            parseJsonDaily(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void connectByCityName() {
        //TODO
        try {
            String response = new HttpService().connect(Config.APP_URL+"q="+city+"&appid="+Config.APP_ID);
            parseJson(response);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void parseJsonDaily(String json) {
        int humidity;
        int pressure;
        int clouds;
        double wind;
        String data;
//        System.out.println("Podaj na ile chcesz wyswietlic pogode, zakres 1-5");
//        int dni = scanner.nextInt();
        JSONObject rootObject = new JSONObject(json);
        if (rootObject.getInt("cod") == 200) {
            JSONArray tablicaPogody = rootObject.getJSONArray("list");
            List<Pogoda> pogoda1 = new ArrayList<>();
            for(int i = 0 ; i<dni*8 ; i=i+8) {

                Pogoda pogodaOBject = new Pogoda();

                JSONObject one = (JSONObject) tablicaPogody.get(i);
                JSONObject mainObject = one.getJSONObject("main");
                pogodaOBject.setTemp(Double.parseDouble(mainObject.get("temp").toString()));

                humidity = mainObject.getInt("humidity");
                pogodaOBject.setHumidity(humidity);
                pressure = mainObject.getInt("pressure");
                pogodaOBject.setPressure(pressure);
                JSONObject mainObject1 = one.getJSONObject("clouds");
                clouds = mainObject1.getInt("all");
                pogodaOBject.setClouds(clouds);
                JSONObject mainObject2 = one.getJSONObject("wind");
                wind = mainObject2.getDouble("speed");
                pogodaOBject.setWind(wind);
                data = one.get("dt_txt").toString();
                pogodaOBject.setData(data);

                pogoda1.add(pogodaOBject);
            }
            System.out.println(pogoda1);
      }
    }
    public String connectByCityName(String miasto){
        String response = null;
        try {
             response = new HttpService().connect(Config.APP_URL+"q="+miasto+"&appid="+Config.APP_ID);
            parseJson(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }



    private void connectByZipCode() {
        //TODO
        try {
            String response = new HttpService().connect( Config.APP_URL+"zip="+zip+","+country +"&appid="+Config.APP_ID);
            parseJson(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String connectByZipCode(String kod,String kraj){
        String response = null;
        try {
            response = new HttpService().connect( Config.APP_URL+"zip="+kod+","+kraj +"&appid="+Config.APP_ID);
            parseJson(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void parseJson(String json) {
        //TODO
        JSONObject jsonObject = new JSONObject(json);
        JSONObject jsonArrayPogoda = jsonObject.getJSONObject("main");

        System.out.println("Temperatura: " +jsonArrayPogoda.getDouble("temp")+"F");
        System.out.println("Temperatura maksymalna: " +jsonArrayPogoda.getDouble("temp_max")+"F");
        Double min = jsonArrayPogoda.getDouble("temp_min");
        Double max = jsonArrayPogoda.getDouble("temp_max");
        Double srednia = (min + max)/2;
        System.out.println("średnia temperatura: "+ srednia+ "F");
        JSONObject zachmurzenie = jsonObject.getJSONObject("clouds");
        System.out.println("Zachmurzenie: "+ zachmurzenie.getDouble("all"));

        JSONObject wiatr = jsonObject.getJSONObject("wind");
        System.out.println("Wiatr: "+ wiatr.getDouble("speed")+"km/h");

        System.out.println("Ciśnienie: "+ jsonArrayPogoda.getDouble("pressure")+"hPa");

        Integer widocznosc = jsonObject.getInt("visibility");
        System.out.println("Widoczność: " + widocznosc.toString());

        JSONArray opis = jsonObject.getJSONArray("weather");
        JSONObject opisss = (JSONObject) opis.get(0);

        System.out.println("Opis: "+opisss.getString("description"));


    }

    private void connectByCoord(){
        try {
            String response = new HttpService().connect( Config.APP_URL+"lat="+lat+"&lon="+lon +"&appid="+Config.APP_ID);
            parseJson(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String connectByCoord(String dl , String sz){
        String response = null;
        try {
            response = new HttpService().connect( Config.APP_URL+"lat="+dl+"&lon="+sz +"&appid="+Config.APP_ID);
            parseJson(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void connectByCityForXDays(){
        try {
            String response = new HttpService().connect( Config.APP_URL+"q="+city+"&cnt="+cnt +"&appid="+Config.APP_ID);
            parseJson(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //api.openweathermap.org/data/2.5/forecast/daily?q={city name},{state}&cnt={cnt}&appid={your api key}

    private void parseJsonForXDays(String json){
        JSONObject jsonObject = new JSONObject(json);
        JSONObject jsonArrayPogoda = jsonObject.getJSONObject("main");


    }

    @Override
    public void run() {
        startApp();
    }
}
