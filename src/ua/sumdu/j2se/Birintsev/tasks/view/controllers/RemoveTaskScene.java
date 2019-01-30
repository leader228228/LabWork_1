package ua.sumdu.j2se.Birintsev.tasks.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ua.sumdu.j2se.Birintsev.tasks.model.Task;

/**
 * The class that represents a scene which handles with
 * removing the task is given to it constructor from the observableList of
 * @see View
 * @see Task
 * */
public class RemoveTaskScene {

    private Task task;
    private View view;

    public RemoveTaskScene(Task task, View view){
        this.task = task;
        this.view = view;
    }

    @FXML
    private Button deleteBtn;

    @FXML
    private Label text;

    @FXML
    private void deleteTask(ActionEvent event) {
        view.removeTask(task);
        view.refreshSublist();
        view.saveTasks();
        text.setText("Done");
        deleteBtn.setVisible(false);
    }

    @FXML
    public void initialize(){
        text.setText(task.getTitle());
    }
}