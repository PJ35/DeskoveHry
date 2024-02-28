import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Model {
    private static List<Deskovka> seznamDeskovek = new ArrayList<>();

    public Model() {

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

    public static void sort() {
        seznamDeskovek.sort(Comparator.comparing(Deskovka::getName));
    }
}
