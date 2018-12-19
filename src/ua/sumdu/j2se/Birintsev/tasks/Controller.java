package ua.sumdu.j2se.Birintsev.tasks;

import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

import java.io.*;
import java.util.Date;
import java.util.List;

public class Controller {

    private View view;

    private File tasksFile;

    private ObservableList <Task> observableList;

    private ObservableList <Task> observableWeekList;

    public ObservableList<Task> loadTaskList() throws IOException{
        observableList = FXCollections.observableArrayList();
        TaskIO.readText(observableList,tasksFile);
        return observableList;
    }

    public void setTasksFile(File tasksFile) {
        this.tasksFile = tasksFile;
    }

    public Controller(View view) {
        this.view = view;
    }

    public void addNewTask (String details, Date time, boolean isActive) {
        Task task = new Task(details, time);
        task.setActive(isActive);
        observableList.add(task);
        saveTasks();
    }

    public void addNewTask(String details, Date start, Date end, int interval, boolean isActive) {
        Task task = new Task(details, start, end, interval);
        task.setActive(isActive);
        observableList.add(task);
        saveTasks();
    }

    public void saveTasks () {
        try {
            TaskIO.writeText(observableList,tasksFile);
        }catch (IOException e){
            System.out.println(e.getCause());
        }
        /*for (Task task : tasks) {
            tasks.add(task);
        }
        try {
            TaskIObservable.writeText(tasks,tasksFile);
        }catch (IOException e){
            System.out.println(e.getCause());
        }*/

    }

    public ObservableList<Task> updateWeekList(){
        observableWeekList = FXCollections.observableArrayList((List)Tasks.incoming(observableList, new Date(), new Date(System.currentTimeMillis() + 31536000000L)));
        return observableWeekList;
    }
}