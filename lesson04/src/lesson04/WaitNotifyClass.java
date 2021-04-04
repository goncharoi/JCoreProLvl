package lesson04;

public class WaitNotifyClass {

    private final Object mon = new Object();
    private volatile char currentLetter = mtSeq[0];
    private static char[] mtSeq = {'A','B','C','D'}; //можно добавлять буквы не меняя код

    public static void main(String[] args) {
        WaitNotifyClass w = new WaitNotifyClass();
        Thread[] ltT = new Thread[mtSeq.length];
        for (int i = 0; i < ltT.length; i++) {
            int finalI = i;
            ltT[i] = new Thread(() -> {
                w.printLetter(mtSeq, finalI);
            });
        }
        for (int i = 0; i < ltT.length; i++) {
            ltT[i].start();
        }
    }

    public void printLetter(char[] itSeq, int ivIndex) {
        synchronized (mon) {
            try {
                for (int i = 0; i < 3; i++) {
                    while (currentLetter != itSeq[ivIndex]) {
                        mon.wait();
                    }
                    System.out.print(itSeq[ivIndex]);
                    if(ivIndex == itSeq.length-1)
                        currentLetter = itSeq[0];
                    else
                        currentLetter = itSeq[ivIndex+1];
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

