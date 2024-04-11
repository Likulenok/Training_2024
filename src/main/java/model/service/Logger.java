package model.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private static final String LOG_FILE_PATH = "src/Log.txt";

    /**
     * Выполняет сохранение информации о событии в файле с логом.
     * @param stringMessage - строка для сохранения в файле с логом
     */
    public synchronized static void writeLog(String stringMessage) {
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write("\n" + stringMessage);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
