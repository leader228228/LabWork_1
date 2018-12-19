package ua.sumdu.j2se.Birintsev.tasks;

import java.io.*;
import java.util.Date;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main {

    public static void main(String[] args) throws Exception {
        /*Task task1 = new Task("123" ,new Date());
        Task task2 = new Task("old task",new Date(1));
        Task task3 = new Task("some info", new Date(33333333));
        TaskList list = new LinkedTaskList();
        list.add(task1);
        list.add(task2);
        list.add(task3);
        File file = new File("res/file.txt");
        TaskIO.writeText(list,file);*/
        /*Writer writer = new FileWriter("res/tasklists/file.txt");
        writer.write(list.toString());
        writer.close();*/
        System.out.println("");

    }

    /*
    Button createTask;

    @Override
    public void start(Stage primaryStage) throws Exception {

        Controller controller = Controller.controller();

        Scene mainScene;
        VBox vBoxLeft = new VBox();
        VBox vBoxRight = new VBox();
        HBox hBox = new HBox(vBoxLeft,vBoxRight);
        hBox.setMargin(vBoxLeft,new Insets(10));
        hBox.setMargin(vBoxRight,vBoxLeft.getInsets());

        TextField inputTaskTxtFld = new TextField();

        Label status = new Label();

        Button newTaskBtn = new Button("New task");
        newTaskBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(controller.createNewTask(inputTaskTxtFld.getText(), new Date())){
                    status.setStyle("-fx-text-fill: #097c00");
                    status.setText("Successfully added");
                }else {
                    status.setStyle("-fx-text-fill: #ff0006");
                    status.setText("Task details must be set");
                }
            }
        });

        Button myTasksBtn = new Button("My Tasks");

        vBoxLeft.getChildren().addAll(newTaskBtn, myTasksBtn);
        vBoxRight.getChildren().addAll(inputTaskTxtFld);

        *//*hBox.setMargin(newTaskBtn,vBoxLeft.getInsets() );
        hBox.setMargin(myTasksBtn,vBoxLeft.getInsets() );
        hBox.setMargin(inputTaskTxtFld,vBoxLeft.getInsets() );*//*

        vBoxLeft.autosize();





        mainScene = new Scene(hBox, 300,150);
        primaryStage.setScene(mainScene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }*/


}