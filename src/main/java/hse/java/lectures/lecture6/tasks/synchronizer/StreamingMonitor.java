package hse.java.lectures.lecture6.tasks.synchronizer;

public class StreamingMonitor {
    private int currentWriter = 0;
    private final int writersAmount;
    private final int[] ticksDone;
    private final int ticksPerWriter;
    private boolean isFinish = false;

    public StreamingMonitor(int writersAmount, int ticksPerWriter) {
        this.writersAmount = writersAmount;
        this.ticksPerWriter = ticksPerWriter;
        this.ticksDone = new int[writersAmount];
    }

    public synchronized boolean streamWait(int id) throws InterruptedException {
        int myIndex = id - 1;

        while (currentWriter != myIndex && !isFinish) {
            wait();
        }

        return !isFinish;
    }

    public synchronized void streamComplete(int id) {
        int myIndex = id - 1;
        ticksDone[myIndex]++;

        currentWriter = (currentWriter + 1) % writersAmount;

        int checker = 0;
        while (checker < writersAmount && ticksDone[currentWriter] >= ticksPerWriter) {
            currentWriter = (currentWriter + 1) % writersAmount;
            checker++;
        }

        if (checker == writersAmount) {
            isFinish = true;
        }

        notifyAll();
    }

    public synchronized void synchFinish() throws InterruptedException {
        while (!isFinish) {
            wait();
        }
    }
}
