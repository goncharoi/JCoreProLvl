package lesson05;

import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {
    private Semaphore moSem; //ДЗ 5: семафор, чтобы в туннеле было не более ivCount участников

    public Tunnel(int ivCount) {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";

        moSem = new Semaphore(ivCount); //ДЗ 5: семафор, чтобы в туннеле было не более ivCount участников
    }

    @Override
    public void go(Car c) {
        try {
            try {
                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);

                moSem.acquire(); //ДЗ 5: ждет семафор

                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);

                moSem.release(); //ДЗ 5: освобожает семафор
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
