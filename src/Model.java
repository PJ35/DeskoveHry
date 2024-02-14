import java.util.ArrayList;
import java.util.List;

public class Model {
    private static List<Deskovka> seznamDeskovek = new ArrayList<Deskovka>();

    public Model() {
        seznamDeskovek.add(new Deskovka("Carcassonne", true, 3));
        seznamDeskovek.add(new Deskovka("Catan", false, 2));
        seznamDeskovek.add(new Deskovka("Ticket to Ride", true, 1));
        seznamDeskovek.add(new Deskovka("Pandemic", true, 3));
        Main.zaklad = seznamDeskovek.size();
    }

    public static Deskovka getDeskovka(int index) {
        return seznamDeskovek.get(index);
    }

    public static void setDeskovka(int index, Deskovka d) {
        seznamDeskovek.set(index, d);
    }

    public static void addDeskovka(Deskovka d) {
        seznamDeskovek.add(d);
    }

    public static void removeDeskovka(int index) {
        seznamDeskovek.remove(index);
    }

    public static int getSize() {
        return seznamDeskovek.size();
    }
}
