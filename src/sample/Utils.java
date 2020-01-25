package sample;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

class Utils {

    static void listFiles(String directoryName, List<File> files) {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        if (fList != null)
            for (File file : fList) {
                if (file.isFile()) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listFiles(file.getAbsolutePath(), files);
                }
            }
    }

    static void listDirectories(String directoryName, List<File> directories) {
        File directory = new File(directoryName);

        File[] fList = directory.listFiles();
        if (fList != null)
            for (File file : fList) {
                if (file.isDirectory()) {
                    directories.add(file);
                    listDirectories(file.getAbsolutePath(), directories);
                }
            }
    }

    static String GetTimeToLog(){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));
    }

    static String AddLog(String currentText, String logContent){
        return currentText + "\n" + logContent;
    }
}
