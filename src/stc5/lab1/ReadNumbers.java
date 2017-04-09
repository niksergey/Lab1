package stc5.lab1;

/**
 * Created by sergey on 07.04.17.
 */
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

class ReadNumbers implements Runnable {
    static boolean keepExecution = true;

    Thread t;
    private String threadname;
    private String sourcePath;
    private SumContainer sumContainer;

    ReadNumbers(String name, String sourcePath, SumContainer sumContainer) {
        this.sumContainer = sumContainer;
        threadname = name;
        t = new Thread(this, threadname);
        this.sourcePath = sourcePath;
        t.start();
    }

    private void readFile(InputStream fis) {
        try (InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr)) {
            String line;
            String[] words;
            int numb;
            int numLine = 0;
            while((line = br.readLine()) != null && keepExecution) {
                numLine++;
                words = line.split(" ");
                for (String word: words)
                    try {
                        numb = Integer.parseInt(word);
                        if (numb > 0 && numb % 2 == 0) {
                            sumContainer.sumValue(numb);
                        }
                    } catch (NumberFormatException e) {
                        keepExecution = false;
                        System.out.println("Неверный формат файла:\n Строка " +
                                numLine + ", слово " + word + "\nПрограмма остановлена");
                    }
            }
        } catch (IOException e) {
            keepExecution = false;
            System.out.println("Ошибка при чтении файла:\n" +
                    sourcePath + "\nПрограмма остановлена");
        }

    }

    public void run() {
        File file = new File(sourcePath);
        if (file.isFile()) {
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
