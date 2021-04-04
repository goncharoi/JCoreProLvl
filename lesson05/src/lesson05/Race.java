package lesson05;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Race {
    private ArrayList<Stage> stages;

    private CountDownLatch moRaceStartCDL; //ДЗ 5: барьер, чтобы все начали одновременно
    private CountDownLatch moAllReadyCDL; //ДЗ 5: барьер, чтобы все подготовились
    private CountDownLatch moAllFinishedCDL; //ДЗ 5: барьер, чтобы все финишировали
    private String mvWinner; //ДЗ 5: барьер, чтобы все финишировали
    private Object moMon;

    public ArrayList<Stage> getStages() {
        return stages;
    }

    public Race(int ivCarsCount, Stage... stages) {
        this.stages = new ArrayList<>(Arrays.asList(stages));
        moRaceStartCDL = new CountDownLatch(ivCarsCount + 1);
        moAllReadyCDL = new CountDownLatch(ivCarsCount);
        moAllFinishedCDL = new CountDownLatch(ivCarsCount);
        moMon = new Object();
    }

    public CountDownLatch getRaceStartCDL() {
        return moRaceStartCDL;
    }

    public CountDownLatch getAllReadyCDL() { return moAllReadyCDL; }

    public CountDownLatch getAllFinishedCDL() { return moAllFinishedCDL; }

    public String getWinner() { return mvWinner; }

    public Object getMoMon() { return moMon; }

    public void setWinner(String mvWinner) { //Заявить о победе можно только один раз, пока победитель неопределен
        synchronized (moMon) {
            if (this.mvWinner == null) {
                this.mvWinner = mvWinner;
                moMon.notify();
            }
        }
    }
}
