import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Model m = new Model();
        String soubor = "deskovky.txt";
        try (Scanner sc = new Scanner(new BufferedReader(new FileReader(soubor)))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] blocks = line.split(";");
                Model.addDeskovka(new Deskovka(blocks[0], Boolean.parseBoolean(blocks[1]), Integer.parseInt(blocks[2])));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Soubor "+soubor+" nenalezen!\n"+e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Chyba při vytváření deskovky:\n"+e.getMessage());
        }
        DeskovkyFrame d = new DeskovkyFrame();
        d.setVisible(true);
    }
}
