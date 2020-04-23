import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pogoda {
    private double temp;
    private int humidity;
    private int pressure;
    private int clouds;
    private double wind;
    private String data;

    @Override
    public String toString() {
        return "Data: " + data +
        " ,Temperatura: " + temp +"[C]" +
                " ,Wilgotnosc: " + humidity+"%" +
                " ,Cisnienie: " + pressure +"hPa"+
                " ,Zachmurzenie: " + clouds+ "%" +
                " ,Wiatr: " + wind + "m/s\n";
    }
}
