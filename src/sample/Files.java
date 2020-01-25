package sample;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Files {

    private static List<File> originalDirectoryFiles = new ArrayList<>();
    private static List<File> backupDirectoryFiles = new ArrayList<>();

    static void Synchronize() throws IOException {
        ListFiles();
        RemoveRedundantFiles();
        ReplaceWithNewerFile();
        AddFiles();
    }

    private static void ListFiles() {
        originalDirectoryFiles.clear();
        backupDirectoryFiles.clear();

        Utils.listFiles(Constants.ORIGINAL_CONTENT_PATH, originalDirectoryFiles);
        Utils.listFiles(Constants.COPIED_CONTENT_PATH, backupDirectoryFiles);
    }

    private static void ReplaceWithNewerFile() throws IOException {
        List<File> filesToReplace = originalDirectoryFiles
                .stream()
                .filter(file -> backupDirectoryFiles.stream()
                        .anyMatch(f -> f.getAbsolutePath().replace(Constants.COPIED_CONTENT_PATH, "")
                                .equals(file.getAbsolutePath().replace(Constants.ORIGINAL_CONTENT_PATH, "")) &&
                                f.lastModified() < file.lastModified()))
                .collect(Collectors.toList());

        for (File file : filesToReplace) {
            File newFile = new File(Constants.COPIED_CONTENT_PATH + file.getAbsolutePath().replace(Constants.ORIGINAL_CONTENT_PATH, ""));
            java.nio.file.Files.copy(file.toPath(),
                    (newFile).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void AddFiles() throws IOException {
        List<File> filesToAdd = originalDirectoryFiles
                .stream()
                .filter(file -> backupDirectoryFiles.stream().map(f -> f.getAbsolutePath().replace(Constants.COPIED_CONTENT_PATH, ""))
                        .noneMatch(f -> f.equals(file.getAbsolutePath().replace(Constants.ORIGINAL_CONTENT_PATH, ""))))
                .collect(Collectors.toList());

        for (File file : filesToAdd) {
            File newFile = new File(Constants.COPIED_CONTENT_PATH + file.getAbsolutePath().replace(Constants.ORIGINAL_CONTENT_PATH, ""));
            java.nio.file.Files.copy(file.toPath(),
                    (newFile).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void RemoveRedundantFiles() {
        List<File> filesToRemove = backupDirectoryFiles
                .stream()
                .filter(file -> originalDirectoryFiles.stream().map(f -> f.getAbsolutePath().replace(Constants.ORIGINAL_CONTENT_PATH, ""))
                        .noneMatch(f -> f.equals(file.getAbsolutePath().replace(Constants.COPIED_CONTENT_PATH, ""))))
                .collect(Collectors.toList());
        filesToRemove.forEach(File::delete);
    }
}