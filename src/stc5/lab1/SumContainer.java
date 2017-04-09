package stc5.lab1;

class SumContainer {
    private int sum;

    SumContainer() {
        sum = 0;
    }

    synchronized void sumValue(int value) {
        sum += value;
    }

    synchronized int getSum() {
        return sum;
    }
}
