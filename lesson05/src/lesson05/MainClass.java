package lesson05;

public class MainClass {
    public static final int CARS_COUNT = 4;
    private static final Race race = new Race(CARS_COUNT, new Road(60), new Tunnel(CARS_COUNT / 2), new Road(40));

    public static void main(String[] args) {

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");
        Car[] cars = new Car[CARS_COUNT];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10));
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }

        allReady(); //ДЗ 5: барьер, чтобы все подготовились

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

        startRace(); //ДЗ 5: барьер, чтобы все начали одновременно

        callWinner(); //ДЗ 5: зависаем тут, пока не объявится победитель, и называем его

        allFinished(); //ДЗ 5: барьер, чтобы все финишировали перед объявлением об окончании гонки

        System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");

    }

    private static void startRace() { //ДЗ 5: снимает барьер окончательно
        try {
            race.getRaceStartCDL().countDown();
            race.getRaceStartCDL().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void allReady() { //ДЗ 5: ждет когда все подготовятся
        try {
            race.getAllReadyCDL().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void allFinished() { //ДЗ 5: ждет когда все финиширют
        try {
            race.getAllFinishedCDL().await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void callWinner(){ //ДЗ 5: называем победителя
        synchronized (race.getMoMon()) {
            try {
                while (race.getWinner() == null) {
                    race.getMoMon().wait(); //зависаем тут, пока не объявится победитель
                }
                System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Победил " + race.getWinner() + "!!!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

