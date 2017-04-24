package stc5.lab1;

/**
 * Created by sergey on 07.04.17.
 */
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadNumbers implements Runnable {
    private static volatile boolean keepExecution = true;

    public Thread t;
    private String threadname;
    private String sourcePath;
    private SumContainer sumContainer;

    public ReadNumbers(String name, String sourcePath, SumContainer sumContainer) {
        this.sumContainer = sumContainer;
        threadname = name;
        t = new Thread(this, threadname);
        this.sourcePath = sourcePath;
    }

    private void readFile(InputStream fis) {
        try (InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr)) {
            String line;
            List<String> words;

            while((line = br.readLine()) != null && keepExecution) {
                words = Arrays.asList(line.split(" "));

                Predicate<String> possibleWord = (s) -> isPossibleWord(s);

                Function<String, Integer> converter = (word) -> {
                    Integer numb = null;
                    try {
                        numb = Integer.parseInt(word);

                    } catch (NumberFormatException e) {
                        keepExecution = false;
                        System.out.println("Неверный regexp" + "\nПрограмма остановлена");
                    }
                    return numb;
                };

                Consumer<Integer> sendFromThread = (v) -> sumContainer.sumValue(v);
                Predicate<Integer> positive = (n) -> (n > 0 && n % 2 == 0);

                words.parallelStream().filter(possibleWord)
                        .map(converter).filter(positive).forEach(sendFromThread);
            }
        } catch (IOException e) {
            keepExecution = false;
            System.out.println("Ошибка при чтении файла:\n" +
                    sourcePath + "\nПрограмма остановлена");
        }
    }

    private boolean isPossibleWord(String word) {
        Pattern pattern = Pattern.compile("-?\\d+");
        Matcher matcher = pattern.matcher(word);
        return matcher.matches();
    }

    public void run() {
        System.out.println("В потоке " + t.getName());
        File file = new File(sourcePath);
        if (file.isFile()) {//refactor
            try(InputStream fis = new FileInputStream(sourcePath)) {
                readFile(fis);
            } catch (IOException e) {
                keepExecution = false;
                System.out.println("Ошибка при открытии файла:\n" +
                        sourcePath + "\nПрограмма остановлена");
            }
        } else {
            try {
                URL fileURL = new URL(sourcePath);
                try (InputStream fis = fileURL.openStream()) {
                    readFile(fis);
                } catch (IOException e) {
                    keepExecution = false;
                    System.out.println("Ошибка при открытии URL:\n" +
                            sourcePath + "\nПрограмма остановлена");
                }
            } catch (MalformedURLException e) {
                keepExecution = false;
                System.out.println("Неверный формат URL:\n" +
                        sourcePath + "\nПрограмма остановлена");
            }
        }
    }

}
