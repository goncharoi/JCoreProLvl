package lesson01;

import java.util.ArrayDeque;

public class Box<T extends Fruit> {
    private int mvCapacity; //Вместимость
    private ArrayDeque<T> mtList = new ArrayDeque<>(); //коробка работает по принципу LIFO

    public Box(int ivCapacity){
        mvCapacity = ivCapacity;
    }

//    Сделать метод getWeight(), который высчитывает вес коробки, зная количество фруктов и вес одного фрукта
//    (вес яблока – 1.0f, апельсина – 1.5f. Не важно, в каких это единицах);
    public float getWeight(){
        float rvWeight = 0.0f;
        for (T lvItem: mtList) {
            rvWeight += lvItem.getMvWeight();
        }
        return rvWeight;
    }
//    Внутри класса Коробка сделать метод compare, который позволяет сравнить текущую коробку с той, которую подадут
//    в compare в качестве параметра, true – если она равны по весу, false – в противном случае
//    (коробки с яблоками мы можем сравнивать с коробками с апельсинами);
    public boolean compare(Box<?> ioBox){
        return getWeight() == ioBox.getWeight();
    }
//    Не забываем про метод добавления фрукта в коробку.
    public void addItem(T ioItem){
        if(!isFull())
            mtList.add(ioItem);
    }
//    Написать метод, который позволяет пересыпать фрукты из текущей коробки в другую
    public void toOtherBox(Box<T> ioBox){
        while (!mtList.isEmpty()&&!ioBox.isFull())
            ioBox.addItem(mtList.pollFirst());
    }

    public boolean isFull() {
        return mvCapacity <= mtList.size();
    }
}
