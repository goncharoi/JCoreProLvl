package lesson01;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {
	    Integer[] ltArr1 = {1,2,3,4,5};
	    try {
            change(ltArr1, 2, 4);
        } catch (ArrayIndexOutOfBoundsException loEx){
            loEx.printStackTrace();
        }
        for (Integer lvI:ltArr1) {
            System.out.print(lvI+", ");
        }

        System.out.println();
        System.out.println(toArrayList(ltArr1));

        Box<Orange> loBox1 = new Box<>(10); //сюда влезает 10 аппельсинов
        while (!loBox1.isFull()) loBox1.addItem(new Orange());
        System.out.println("Box1 weight = "+loBox1.getWeight()); // 10*1,5=15
        Box<Orange> loBox2 = new Box<>(4); //а сюда - только 5
        while (!loBox2.isFull()) loBox2.addItem(new Orange());
        System.out.println("Box2 weight = "+loBox2.getWeight()); // 4*1,5=6
        Box<Apple> loBox3 = new Box<>(5);
        //while (!loBox3.isFull()) loBox3.addItem(new Orange()); //так уже не могу
        while (!loBox3.isFull()) loBox3.addItem(new Apple()); //а так могу
        Box<Apple> loBox4 = new Box<>(6);
        for (int i = 0; i < 3; i++) {
            loBox4.addItem(new Apple());
        }
        loBox3.toOtherBox(loBox4);
        //loBox2.toOtherBox(loBox4); //так тоже не могу
        System.out.println("Box3 weight = "+loBox3.getWeight()); //8-6=2
        System.out.println("Box4 weight = "+loBox4.getWeight()); //3+5=8
        System.out.println(loBox4.compare(loBox2)); //6=6

    }
//    Написать метод, который меняет два элемента массива местами (массив может быть любого ссылочного типа);
    public static <T> void change(T[] itArr, int ivFirst, int ivSecond) throws ArrayIndexOutOfBoundsException{
        T lvTmp = itArr[ivFirst];
        itArr[ivFirst] = itArr[ivSecond];
        itArr[ivSecond] = lvTmp;
    }
//    Написать метод, который преобразует массив в ArrayList;
    public static <T> ArrayList<T> toArrayList(T[] itArr){
        ArrayList<T> rtRes = new ArrayList<>();
        Collections.addAll(rtRes, itArr);
        return rtRes;
    }
}
