package ua.sumdu.j2se.Birintsev.tasks;


import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTimePicker;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableListValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




public class View extends Application {

    private static final String tasklistsDir = "res/tasklists/";

    private static final String savedTasksFileName = "tasks.txt";

    private File tasksFile;

    private ObservableList<Task> observableWeekList;

    private ObservableList<Task> observableList;

    @FXML
    private TitledPane allTasksTitledPane;

    @FXML
    private TitledPane weekTasksTitledPane;

    @FXML
    private TitledPane newTaskTitledPane;

    @FXML
    private TextField taskDetails;

    @FXML
    private Button addTaskBtn;

    @FXML
    private JFXDatePicker startDate;

    @FXML
    private JFXDatePicker endDate;

    @FXML
    private Text testLabel3;

    @FXML
    private JFXTimePicker startTime;

    @FXML
    private JFXTimePicker endTime;

    @FXML
    private JFXCheckBox isActive;

    @FXML
    private JFXCheckBox isRepetative;

    @FXML
    private Text toLabel;

    @FXML
    private Text fromLabel;

    @FXML
    private Text everyLabel;

    @FXML
    private TextField daysTxt;

    @FXML
    private TextField minutesTxt;

    @FXML
    private TextField hoursTxt;

    @FXML
    private Text daysLabel;

    @FXML
    private Label hoursLabel;

    @FXML
    private Label minutesLabel;

    @FXML
    private TableView<Task> tableAll;

    @FXML
    private TableColumn<Task, String> taskDetailClmn_all;

    @FXML
    private TableColumn<Task, String> timeClmn_all;

    @FXML
    private TableColumn<Task, String> fromClmn_all;

    @FXML
    private TableColumn<Task, String> toClmn_all;

    @FXML
    private TableColumn<Task, String> intervalClmn_all;

    @FXML
    private TableColumn<Task, String> isActiveClmn_all;

    @FXML
    private TableView<Task> tableWeek;

    @FXML
    private TableColumn<Task, String> taskDetailClmn_week;

    @FXML
    private TableColumn<Task, String> timeClmn_week;

    @FXML
    private TableColumn<Task, String> fromClmn_week;

    @FXML
    private TableColumn<Task, String> toClmn_week;

    @FXML
    private TableColumn<Task, String> intervalClmn_week;

    @FXML
    private TableColumn<Task, String> isActiveClmn_week;


    /**
     * Turns the newTaskTitledPane
     * to mode of creation a repetative or non-repetative task
     */
    @FXML
    private void changeCreationMode(){
        if(isRepetative.isSelected()){
            fromLabel.setText("From");
            toLabel.setVisible(true);
            endDate.setVisible(true);
            endTime.setVisible(true);
            everyLabel.setVisible(true);
            daysLabel.setVisible(true);
            daysTxt.setVisible(true);
            hoursLabel.setVisible(true);
            hoursTxt.setVisible(true);
            minutesLabel.setVisible(true);
            minutesTxt.setVisible(true);
        } else {
            fromLabel.setText("Time");
            toLabel.setVisible(false);
            endDate.setVisible(false);
            endTime.setVisible(false);
            everyLabel.setVisible(false);
            daysLabel.setVisible(false);
            daysTxt.setVisible(false);
            hoursLabel.setVisible(false);
            hoursTxt.setVisible(false);
            minutesLabel.setVisible(false);
            minutesTxt.setVisible(false);
        }
    }

    @FXML
    public void createNewTask(){
        Task task;
        String details = taskDetails.getText();
        if(details == "" || details == null){
            return; // logger + notification
        }
        Date start =  java.sql.Date.valueOf(startDate.getValue());
        start.setTime(start.getTime() + startTime.getValue().getHour() * 3600000 + startTime.getValue().getMinute() * 60000);
        if(isRepetative.isSelected()){
            Date end =  java.sql.Date.valueOf(endDate.getValue());
            end.setTime(end.getTime() + endTime.getValue().getHour() * 3600000 + endTime.getValue().getMinute() * 60000);
            int interval = Integer.parseInt(daysTxt.getText()) * 86400 + Integer.parseInt(hoursTxt.getText()) * 3600 + Integer.parseInt(minutesTxt.getText()) * 60;
            task = new Task(details, start, end, interval);
        }else{
            task = new Task(details, start);
        }
        task.setActive(isActive.isSelected());
        System.out.println(task);
        observableList.add(task);
        updateWeekList();
    }

    @FXML
    public void controllDayTyped(){
        Pattern pattern = Pattern.compile("^\\d*$");
        Matcher matcher = pattern.matcher(daysTxt.getText());
        if(!matcher.matches()){
            daysTxt.setText(daysTxt.getText().replaceAll("[^\\d]",""));
            //daysTxt.setText(daysTxt.getText().replaceFirst("^0.+$", "0")); не работает
        }
        pattern = Pattern.compile("^0(.+)$");
        matcher = pattern.matcher(daysTxt.getText());
        if(matcher.matches()){
            daysTxt.setText(matcher.group(1));
        }
        try{
            if(Integer.parseInt(daysTxt.getText()) > 24855){
                daysTxt.setText("24855");
            }
        }catch (java.lang.NumberFormatException e){
            daysTxt.setText("0");
        }
    }

    @FXML
    public void controllHourTyped(){
        Pattern pattern = Pattern.compile("^\\d*$");
        Matcher matcher = pattern.matcher(hoursTxt.getText());
        if(!matcher.matches()){
            hoursTxt.setText(hoursTxt.getText().replaceAll("[^\\d]",""));
            //hoursTxt.setText(hoursTxt.getText().replaceFirst("^0.+$", "0")); не работает
        }
        pattern = Pattern.compile("^0(.+)$");
        matcher = pattern.matcher(hoursTxt.getText());
        if(matcher.matches()){
            hoursTxt.setText(matcher.group(1));
        }
        try{
            if(Integer.parseInt(hoursTxt.getText()) > 23){
                hoursTxt.setText("23");
            }
        }catch (java.lang.NumberFormatException e){
            hoursTxt.setText("0");
        }
    }

    @FXML
    public void controllMinuteTyped(){
        Pattern pattern = Pattern.compile("^\\d*$");
        Matcher matcher = pattern.matcher(minutesTxt.getText());
        if(!matcher.matches()){
            minutesTxt.setText(minutesTxt.getText().replaceAll("[^\\d]",""));
            //minutesTxt.setText(minutesTxt.getText().replaceFirst("^0.+$", "0")); не работает
        }
        pattern = Pattern.compile("^0(.+)$");
        matcher = pattern.matcher(minutesTxt.getText());
        if(matcher.matches()){
            minutesTxt.setText(matcher.group(1));
        }
        try {
            if (Integer.parseInt(minutesTxt.getText()) > 59) {
                minutesTxt.setText("59");
            }
        }catch (java.lang.NumberFormatException e){
            minutesTxt.setText("0");
        }
    }

    /**
     * Was written to block the context menu for an element
     */
    @FXML
    public void blockContextMenu(){

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Task manager");
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                //saveTasks();
                primaryStage.close();
            }
        });
        primaryStage.setScene(new Scene(root, primaryStage.getWidth(), primaryStage.getHeight()));
        primaryStage.show();
    }

    @FXML
    private void initialize() throws IOException {
        tasksFile = new File(new StringBuilder(tasklistsDir).append(savedTasksFileName).toString());
        if(!tasksFile.canRead()){
            tasksFile.createNewFile();
        }
        /*
        * List initializing
        * */
        ObservableList<Task> observableList = loadTaskList();
        ObservableList<Task> observableWeekList = updateWeekList();
        observableList.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> c) {
                updateWeekList();
            }
        });
        /*
        * "All-task" table building
        * */
        taskDetailClmn_all.setCellValueFactory(new PropertyValueFactory<>("observableDetails"));
        timeClmn_all.setCellValueFactory(new PropertyValueFactory<>("observableTime"));
        fromClmn_all.setCellValueFactory(new PropertyValueFactory<>("observableStart"));
        toClmn_all.setCellValueFactory(new PropertyValueFactory<>("observableEnd"));
        intervalClmn_all.setCellValueFactory(new PropertyValueFactory<>("observableInterval"));
        isActiveClmn_all.setCellValueFactory(new PropertyValueFactory<>("observableIsActive"));
        tableAll.setItems(observableList);
        /*
         * "Week-task" table building
         * */
        taskDetailClmn_week.setCellValueFactory(new PropertyValueFactory<>("observableDetails"));
        timeClmn_week.setCellValueFactory(new PropertyValueFactory<>("observableTime"));
        fromClmn_week.setCellValueFactory(new PropertyValueFactory<>("observableStart"));
        toClmn_week.setCellValueFactory(new PropertyValueFactory<>("observableEnd"));
        intervalClmn_week.setCellValueFactory(new PropertyValueFactory<>("observableInterval"));
        isActiveClmn_week.setCellValueFactory(new PropertyValueFactory<>("observableIsActive"));
        tableWeek.setItems(updateWeekList());
        /*
        * Saving
        * */
        observableList.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> c) {
                saveTasks();
            }
        });

    }

    /*@Override
    public void stop() throws Exception {
        controller.saveTasks();
    }*/

    private ObservableList<Task> loadTaskList() throws IOException{
        observableList = FXCollections.observableArrayList();
        TaskIO.readText(observableList,tasksFile);
        return observableList;
    }

    private void saveTasks () {
        try {
            TaskIO.writeText(observableList,tasksFile);
        }catch (IOException e){
            System.out.println(e.getCause());
        }
    }

    @SuppressWarnings("Unchecked assignment: 'javafx.collections.ObservableList' to 'javafx.collections.ObservableList<ua.sumdu.j2se.Birintsev.tasks.Task>'")
    private ObservableList<Task> updateWeekList(){
        observableWeekList = FXCollections.observableArrayList((List)Tasks.incoming(observableList, new Date(), new Date(System.currentTimeMillis() + 31536000000L)));
        return observableWeekList;
    }
}
