package stc5.lab1;

//Необходимо разработать программу,
// которая получает на вход список ресурсов, содержащих набор чисел
// и считает сууму всех положительных четных. Каждый ресурс должен быть
// обработан в отдельном потоке, набор должен содержать лишь числа,
// унарный оператор "-" и пробелы. Общая сумма должна отображаться на
// экране и изменяться в режиме реального времени. Все ошибки должны быть
// корректно обработаны, все API покрыто модульными тестами

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<ReadNumbers> threadList = new ArrayList<>();

        SumContainer sc = new SumContainer();

        int threadNum = 0;
        for(String arg : args) {
            File file = new File(arg);
            if (file.isFile()) {
                threadList.add(new ReadNumbers(arg, arg, sc));
                threadNum++;
            }
        }

        String[] urlList = {
                "https://raw.githubusercontent.com/niksergey/plainFiles/master/data_numbers.txt",
                "https://raw.githubusercontent.com/niksergey/plainFiles/master/data_numbers2.txt"};

        for(String url : urlList) {
            threadList.add(new ReadNumbers(Integer.toString(threadNum), url, sc));
            threadNum++;
        }

        while (isSomeoneAlive(threadList)) {
            System.out.println("Current sum: " + sc.getSum());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Sum after all threads " + sc.getSum());

        for(ReadNumbers rn : threadList) {
            try {
                rn.t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isSomeoneAlive(List<ReadNumbers> threads) {
        for(ReadNumbers rn : threads) {
            if (rn.t.isAlive())
                return true;
        }
        return false;
    }
}
