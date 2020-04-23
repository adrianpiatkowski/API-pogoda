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
        return "\nData: " + data +
        "\nTemperatura: " + temp +"[C]" +
                "\nWilgotnosc: " + humidity+"%" +
                "\nCisnienie: " + pressure +"hPa"+
                "\nZachmurzenie: " + clouds+ "%" +
                "\nWiatr: " + wind + "m/s\n";
    }
}
