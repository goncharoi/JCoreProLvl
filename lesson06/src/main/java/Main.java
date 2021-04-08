import java.util.ArrayList;
import java.util.List;

public class Main {
    public static Integer[] first(Integer[] itInt) throws EmptyArrayException {
        //в качестве аргумента передается не пустой одномерный целочисленный массив
        if (itInt.length == 0) throw new EmptyArrayException();

        List<Integer> loInt = new ArrayList<Integer>(List.of(itInt));

        //Входной массив должен содержать хотя бы одну четверку, иначе в методе необходимо выбросить RuntimeException
        if (!loInt.contains(4)) throw new RuntimeException();

        //вернуть новый массив, который получен путем вытаскивания из исходного массива элементов, идущих после последней четверки
        if (loInt.lastIndexOf(4) == loInt.size() - 1)
            return new Integer[0];
        else {
            return loInt.subList(loInt.lastIndexOf(4)+1, loInt.size()).stream().toArray(Integer[]::new);
        }
    }

    public static boolean second(Integer[] itInt) {
        //метод, который проверяет состав массива из чисел 1 и 4 - если
        for (int lvInt: itInt) {
            if (lvInt != 1 && lvInt != 4) throw new IllegalArgumentException();
        }

        List<Integer> loInt = new ArrayList<Integer>(List.of(itInt));

        //Если в нем нет хоть одной четверки или единицы, то метод вернет false;
        return loInt.contains(1) && loInt.contains(4);
    }
}
