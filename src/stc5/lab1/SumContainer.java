package stc5.lab1;

public class SumContainer {
    private int sum;//volatile  or atomic

    public SumContainer() {
        sum = 0;
    }

    synchronized void sumValue(int value) {
        sum += value;
    }

    public synchronized int getSum() {
        return sum;
    }
}
