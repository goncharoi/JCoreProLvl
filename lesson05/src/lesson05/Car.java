package lesson05;

public class Car implements Runnable {
    private static int CARS_COUNT;

    static {
        CARS_COUNT = 0;
    }

    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));
            System.out.println(this.name + " готов");
            race.getAllReadyCDL().countDown(); //ДЗ 5: понижает барьер для команды на старт
        } catch (Exception e) {
            e.printStackTrace();
        }

        startRace(); //ДЗ 5: понижает барьер и ждет общего старта

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }

        race.setWinner(name); //ДЗ 5: пытается заявить о своей победе
        race.getAllFinishedCDL().countDown(); //ДЗ 5: понижает барьер для объявления о завершении гонки
    }

    private void startRace(){ //ДЗ 5: понижает барьер и ждет общего старта
        try {
            race.getRaceStartCDL().countDown();
            race.getRaceStartCDL().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
