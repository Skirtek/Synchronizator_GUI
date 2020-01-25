package sample;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Directories {

    private static List<File> originalDirectoryDirs = new ArrayList<>();
    private static List<File> backupDirectoryDirs = new ArrayList<>();

    static void Synchronize() {
        originalDirectoryDirs.clear();
        backupDirectoryDirs.clear();

        ListDirectories();
        RemoveRedundantDirectories();
        CreateDirectories();
    }

    private static void ListDirectories() {
        Utils.listDirectories(Constants.ORIGINAL_CONTENT_PATH, originalDirectoryDirs);
        Utils.listDirectories(Constants.COPIED_CONTENT_PATH, backupDirectoryDirs);
    }

    private static void RemoveRedundantDirectories() {
        List<File> directoriesToRemove = backupDirectoryDirs
                .stream()
                .filter(file -> originalDirectoryDirs.stream().map(f -> f.getAbsolutePath().replace(Constants.ORIGINAL_CONTENT_PATH, ""))
                        .noneMatch(f -> f.equals(file.getAbsolutePath().replace(Constants.COPIED_CONTENT_PATH, ""))))
                .collect(Collectors.toList());

        directoriesToRemove.forEach(File::delete);
    }

    private static void CreateDirectories() {
        List<File> directoriesToAdd = originalDirectoryDirs
                .stream()
                .filter(file -> backupDirectoryDirs.stream().map(f -> f.getAbsolutePath().replace(Constants.COPIED_CONTENT_PATH, ""))
                        .noneMatch(f -> f.equals(file.getAbsolutePath().replace(Constants.ORIGINAL_CONTENT_PATH, ""))))
                .collect(Collectors.toList());

        directoriesToAdd
                .forEach(dir ->
                        new File(Constants.COPIED_CONTENT_PATH + dir.getAbsolutePath()
                                .replace(Constants.ORIGINAL_CONTENT_PATH, "")).mkdir());
    }
}