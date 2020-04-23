import org.json.JSONObject;
import org.junit.jupiter.api.*;
import java.io.IOException;
public class MainAppTest {
    @BeforeAll
    static void init() {
        System.out.println("Rozpoczęcie testów aplikacji");
    }
    @Test
    @DisplayName("Główny test połączenia")
    @Tag("dev")
    void connectionTest() {
        HttpService httpService = new HttpService();
        JSONObject rootObject = null;
        try {
            String respone = httpService.connect(Config.APP_URL + "q=" + "Warszawa" + "&appid=" + Config.APP_ID);
            rootObject = new JSONObject(respone);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(rootObject.getInt("cod") == 200);
    }

    @Test
    @DisplayName("Główny test połączenia")
    @Tag("dev")
    void connectionTestLong() {
        HttpService HttpService = new HttpService();
        JSONObject rootObject = null;
        try {

           String response = HttpService.connect(Config.APP_URL_DAILY + "Grojec"+"&units=metric&appid="+Config.APP_ID);
            rootObject = new JSONObject(response);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(rootObject.getInt("cod") == 200);
    }

    @Test
    @DisplayName("Test pogody dla miasta Warszawa")
    void connectByCityNameTest() {
        MainApp mainApp = new MainApp();
        String responseTest = mainApp.connectByCityName("Warsaw");
        JSONObject jsonObject = new JSONObject(responseTest);
        Assertions.assertEquals(200, jsonObject.getInt("cod"));
        Assertions.assertEquals("Warsaw", jsonObject.getString("name"));
    }

    @Test
    @DisplayName("Test pogody dla Wwa przez zip code")
    void connectByZipCodeTest(){
        MainApp mainApp = new MainApp();
        String responseTest = mainApp.connectByZipCode("00-100","pl");
        JSONObject jsonObject = new JSONObject(responseTest);
        Assertions.assertEquals(200, jsonObject.getInt("cod"));
        Assertions.assertEquals("Warszawa", jsonObject.getString("name"));
    }


    @Test
    @DisplayName("Test pogody dla WWa przez coord")
    void connectByCoordTest(){
        MainApp mainApp = new MainApp();
        String responseTest = mainApp.connectByCoord("52,22","21,01");
        JSONObject jsonObject = new JSONObject(responseTest);
        Assertions.assertEquals(200, jsonObject.getInt("cod"));
        Assertions.assertEquals("Warsaw", jsonObject.getString("name"));
    }

    @Test
    @DisplayName("Test pogody dla Grojca , liczba dni")
    void connectByCityLong(){
        MainApp mainApp = new MainApp();

        String responseTest = mainApp.liczbaDni(2,"Grojec");
        JSONObject jsonObject = new JSONObject(responseTest);
        JSONObject jsonObject1 = jsonObject.getJSONObject("city");
        Assertions.assertEquals(200, jsonObject.getInt("cod"));
        Assertions.assertEquals("Grójec", jsonObject1.getString("name"));
    }

    @AfterAll
    static void done() {
        System.out.println("Zakończenie testów");
    }
}