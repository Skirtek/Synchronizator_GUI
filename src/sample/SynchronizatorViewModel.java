package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SynchronizatorViewModel {

    public Button synchronize;
    public Button stop;
    public Label state;
    public TextArea log_area;

    private Runnable synchronization;
    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    private Future<?> synchronizationTask;

    public void initialize() {
        state.setText("Zatrzymany");
        log_area.setText(Utils.GetTimeToLog() + " Uruchomiono program");

        synchronization = () -> {
            try {
                Platform.runLater(() -> log_area.setText(Utils.AddLog(log_area.getText(), Utils.GetTimeToLog() + " Rozpoczęto synchronizację")));

                Directories.Synchronize();
                Files.Synchronize();

                log_area.setText(Utils.AddLog(log_area.getText(), Utils.GetTimeToLog() + " Pomyślna synchronizacja"));
            } catch (Exception ex) {
                ex.printStackTrace();
                log_area.setText(Utils.AddLog(log_area.getText(), Utils.GetTimeToLog() + " Wystąpił błąd"));
            }
        };
    }

    @FXML
    public void onSynchronizeStarted() {
        state.setText("Pracuje");
        log_area.setText(Utils.AddLog(log_area.getText(), Utils.GetTimeToLog() + " Uruchomiono synchronizację"));

        if(synchronizationTask == null || synchronizationTask.isCancelled()){
            synchronizationTask = scheduledExecutorService.scheduleAtFixedRate(synchronization, 0, 1, TimeUnit.SECONDS);
        }
    }

    @FXML
    public void onSynchronizeStopped() {
        state.setText("Zatrzymany");
        log_area.setText(Utils.AddLog(log_area.getText(), Utils.GetTimeToLog() + " Zatrzymano synchronizację"));

        if(synchronizationTask == null || synchronizationTask.isCancelled()){
            return;
        }

        synchronizationTask.cancel(true);
    }
}
