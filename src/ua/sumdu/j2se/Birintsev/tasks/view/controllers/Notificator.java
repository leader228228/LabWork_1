package ua.sumdu.j2se.Birintsev.tasks.view.controllers;

import javafx.collections.ObservableList;
import ua.sumdu.j2se.Birintsev.tasks.model.Task;
import ua.sumdu.j2se.Birintsev.tasks.Utill;
import java.awt.*;
import java.util.Date;

/**
 * The class that notifies the user that some event is going to happen
 * in less than 5 minutes.
 * Shows a tray-notification every minute
 * @see View
 * */
public class Notificator extends Thread {

    private ObservableList <Task> list;
    private TrayIcon trayIcon;

    public Notificator(ObservableList<Task> list, TrayIcon trayIcon) {
        this.trayIcon = trayIcon;
        this.list = list;
    }

    @Override
    public void run() {
        System.out.println(new StringBuilder("Notificator thread starts: ").append(currentThread().toString()));
        while (true) {
            for (Task task : list){
                Date nextTimeAfterNow = task.nextTimeAfter(new Date());
                if(nextTimeAfterNow == null){
                    continue;
                }
                if(nextTimeAfterNow.getTime() < new Date(System.currentTimeMillis() + 300000).getTime()){
                    trayIcon.displayMessage(task.getTitle(), new StringBuilder("At ").append(Utill.dateToLocalDateTime(task.nextTimeAfter(new Date())).toLocalTime()).toString(), TrayIcon.MessageType.INFO);
                    System.out.println(new StringBuilder("Notified: ").append(task.getTitle()).append(", incoming time: "). append(task.nextTimeAfter(new Date())));
                }
            }
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}