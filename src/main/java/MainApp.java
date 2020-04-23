import java.io.IOException;
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

    private void startApp() {
        scanner = new Scanner(System.in);
        System.out.println("Wybierz po czym chcesz znaleźć miejsce dla którego wyświetlisz pogodę \n0 - Zakończ działanie \n1 - Nazwa Miasta \n2 - Kod pocztowy");
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



    private void connectByZipCode() {
        //TODO
        try {
            String response = new HttpService().connect( Config.APP_URL+"zip="+zip+","+country +"&appid="+Config.APP_ID);
            parseJson(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseJson(String json) {
        //TODO
        JSONObject jsonObject = new JSONObject(json);
        JSONObject jsonArrayPogoda = jsonObject.getJSONObject("main");
//        JSONObject


//        System.out.println(jsonObject);
        System.out.println("Temperatura: " +jsonArrayPogoda.getDouble("temp")+"F");
        System.out.println("Temperatura maksymalna: " +jsonArrayPogoda.getDouble("temp_max")+"F");
        Double min = jsonArrayPogoda.getDouble("temp_min");
        Double max = jsonArrayPogoda.getDouble("temp_min");
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

    @Override
    public void run() {
        startApp();
    }
}
