package ua.sumdu.j2se.Birintsev.tasks.view.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTimePicker;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import ua.sumdu.j2se.Birintsev.tasks.model.Task;
import ua.sumdu.j2se.Birintsev.tasks.model.TaskIO;
import ua.sumdu.j2se.Birintsev.tasks.model.Tasks;
import ua.sumdu.j2se.Birintsev.tasks.Utill;
import static java.lang.Thread.currentThread;
import static ua.sumdu.j2se.Birintsev.tasks.Utill.*;

/**
 * The class that represents the main controller of the application
 * @see RemoveTaskScene
 * @see EditTaskScene
 * */
public class View extends Application {

    public View() {
        mainController = this;
    }

    private static final String tasklistsDir = "res/tasklists/";

    private static final String savedTasksFileName = "tasks.txt";

    private File tasksFile;

    private ObservableList<Task> sublist;

    private ObservableList<Task> observableList;

    private Stage primStage;

    private View mainController;

    private Notificator notificator;

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
    private Label daysLabel;

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
    private TableColumn<Task, String> taskDetailClmn_sublist;

    @FXML
    private TableColumn<Task, String> timeClmn_sublist;

    @FXML
    private TableColumn<Task, String> fromClmn_sublist;

    @FXML
    private TableColumn<Task, String> toClmn_sublist;

    @FXML
    private TableColumn<Task, String> intervalClmn_sublist;

    @FXML
    private TableColumn<Task, String> isActiveClmn_sublist;

    @FXML
    private JFXDatePicker sublistFromDate;

    @FXML
    private JFXDatePicker sublistToDate;

    @FXML
    private JFXTimePicker sublistFromTime;

    @FXML
    private JFXTimePicker sublistToTime;

    @FXML
    private JFXSpinner spinner;

    /**
     * Turns the newTaskTitledPane
     * to mode of creation a repetative or non-repetative task
     */
    @FXML
    private void changeCreationMode() {
        if(isRepetative.isSelected()) {
            fromLabel.setText("From");
            showNodes(toLabel,endDate,endTime,everyLabel,daysLabel,daysTxt,hoursLabel,hoursTxt,minutesLabel,minutesTxt);
        } else {
            fromLabel.setText("Time");
            hideNodes(toLabel,endDate,endTime,everyLabel,daysLabel,daysTxt,hoursLabel,hoursTxt,minutesLabel,minutesTxt);
        }
    }

    @FXML
    public void createNewTask() {
        Task task;
        String details = taskDetails.getText();
        if(details == null) {
            taskDetails.setPromptText("Please, enter the task");
            return;
        }
        if("".equals(details) || details == null) {
            System.err.println(new StringBuilder("Try to add an empty task ").append(getClass()));
            return;
        }
        Date start =  java.sql.Date.valueOf(startDate.getValue());
        start.setTime(start.getTime() + startTime.getValue().getHour() * 3600000 + startTime.getValue().getMinute() * 60000);
        if(isRepetative.isSelected()) {
            Date end =  java.sql.Date.valueOf(endDate.getValue());
            end.setTime(end.getTime() + endTime.getValue().getHour() * 3600000 + endTime.getValue().getMinute() * 60000);
            task = new Task(details, start, end,
                    Utill.getIntervalFromStrings(daysTxt.getText(), hoursTxt.getText(), minutesTxt.getText()));
        }else {
            task = new Task(details, start);
        }
        task.setActive(isActive.isSelected());
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        if(observableList.add(task)){
            refreshAddForm();
            trayIcon.displayMessage(task.getTitle(), new StringBuilder("The task: ").append(task.getTitle()).append(" has been successfully added").toString(), TrayIcon.MessageType.INFO);
            refreshSublist();
        }
    }

    /**
     * Was written to block the context menu for an element
     */
    @FXML
    public void blockContextMenu(){
        //blocks the context menu request on the days/hours/minutes text fields
    }

    void refreshSublist() {
        System.out.println(new StringBuilder(getClass().getName()).append(" refreshing the sublist starts"));
        Date from = Utill.localDateTimeToDate(LocalDateTime.of(sublistFromDate.getValue(),sublistFromTime.getValue()));
        Date to = Utill.localDateTimeToDate(LocalDateTime.of(sublistToDate.getValue(),sublistToTime.getValue()));
        Iterable <Task> nonObservableSublist = Tasks.incoming( observableList, from, to);
        tableWeek.setItems(FXCollections.observableList((List<Task>)nonObservableSublist));
        System.out.println(new StringBuilder(getClass().getName()).append(" refreshing the sublist finished. New sublist: ").append(tableWeek.getItems()));
    }

    public static void main(String[] args) {
        System.out.println(new StringBuilder(View.class.getName()).append(": Application is launching"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println(new StringBuilder(getClass().getName()).append(": Application starts"));
        Parent root = FXMLLoader.load(getClass().getResource("fxml/sample.fxml"));
        System.out.println(new StringBuilder(getClass().getName()).append(": Application starts"));
        primaryStage.setTitle("Task manager");
        primaryStage.setScene(new Scene(root, primaryStage.getWidth(), primaryStage.getHeight()));
        primStage = primaryStage;
        primaryStage.setResizable(false);
        System.out.println(new StringBuilder(getClass().getName()).append(": stage created"));
        primaryStage.show();
    }

    private void notificatorLaunch(){
        System.out.println(new StringBuilder(getClass().getName()).append(": launching the notificator starts"));
        SystemTray tray = SystemTray.getSystemTray();
        Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
        TrayIcon trayIcon = new TrayIcon(image, "Tray Demo");
        trayIcon.setImageAutoSize(true);
        trayIcon.setToolTip("System tray icon demo");
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        Notificator notificator = new Notificator(observableList, trayIcon);
        notificator.setDaemon(true);
        notificator.start();
        System.out.println(new StringBuilder(getClass().getName()).append(": launching the notificator finished"));
    }

    @FXML
    private void initialize() throws IOException {
        System.out.println(new StringBuilder(getClass().getName()).append(": initialization starts"));
        refreshAddForm();
        sublistFromDate.setValue(LocalDate.now());
        sublistFromTime.setValue(LocalTime.now());
        sublistToDate.setValue(LocalDate.now().plusDays(7));
        sublistToTime.setValue(LocalTime.now());
        System.out.println(currentThread());
        tasksFile = new File(new StringBuilder(tasklistsDir).append(savedTasksFileName).toString());
        if(!tasksFile.canRead()){
            tasksFile.createNewFile();
        }
        observableList = loadTaskList();
        notificatorLaunch();
        taskDetails.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null || "".equals(newValue) || newValue.length() == 0 || "\uFEFF".equals(newValue)){
                    addTaskBtn.setVisible(false);
                } else {
                    addTaskBtn.setVisible(true);
                }
            }
        });
        addTaskBtn.setVisible(false);
        startDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(newValue == null){
                    startDate.setValue(oldValue);
                }
                LocalDateTime newLDT = LocalDateTime.of(newValue, startTime.getValue());
                LocalDateTime toLDT = LocalDateTime.of(endDate.getValue(), endTime.getValue());
                if(!newLDT.isBefore(toLDT)){
                    startDate.setValue(oldValue);
                }
            }
        });
        startTime.valueProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                if(newValue == null){
                    startTime.setValue(oldValue);
                }
                LocalDateTime newLDT = LocalDateTime.of(startDate.getValue(),newValue);
                LocalDateTime toLDT = LocalDateTime.of(endDate.getValue(),endTime.getValue());
                if(!newLDT.isBefore(toLDT)){
                    startTime.setValue(oldValue);
                }
            }
        });
        endDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(newValue == null) {
                    endDate.setValue(oldValue);
                }
                LocalDateTime newLDT = LocalDateTime.of(newValue, endTime.getValue());
                LocalDateTime fromLDT = LocalDateTime.of(startDate.getValue(), startTime.getValue());
                if(!newLDT.isAfter(fromLDT)){
                    endDate.setValue(oldValue);
                }
            }
        });
        endTime.valueProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                if(newValue == null){
                    endTime.setValue(oldValue);
                }
                LocalDateTime newLDT = LocalDateTime.of(endDate.getValue(),newValue);
                LocalDateTime fromLDT = LocalDateTime.of(startDate.getValue(),startTime.getValue());
                if(!newLDT.isAfter(fromLDT)){
                    endTime.setValue(oldValue);
                }
            }
        });
        sublist = FXCollections.observableArrayList();
        sublistFromDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(newValue == null){
                    sublistFromDate.setValue(oldValue);
                    return;
                }
                LocalDateTime newLDT = LocalDateTime.of(newValue, sublistFromTime.getValue());
                LocalDateTime toLDT = LocalDateTime.of(sublistToDate.getValue(), sublistToTime.getValue());
                if(!newLDT.isBefore(toLDT)) {
                    sublistFromDate.setValue(oldValue);
                    return;
                }
                refreshSublist();
            }
        });
        sublistFromTime.valueProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                if(newValue == null){
                    sublistFromTime.setValue(oldValue);
                    return;
                }
                LocalDateTime newLDT = LocalDateTime.of(sublistFromDate.getValue(),newValue);
                LocalDateTime toLDT = LocalDateTime.of(sublistToDate.getValue(),sublistToTime.getValue());
                if(!newLDT.isBefore(toLDT)){
                    sublistFromTime.setValue(oldValue);
                    return;
                }
                refreshSublist();
            }
        });
        sublistToDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(newValue == null){
                    sublistToDate.setValue(oldValue);
                    return;
                }
                LocalDateTime newLDT = LocalDateTime.of(newValue, sublistToTime.getValue());
                LocalDateTime fromLDT = LocalDateTime.of(sublistFromDate.getValue(), sublistFromTime.getValue());
                if(!newLDT.isAfter(fromLDT)) {
                    sublistToDate.setValue(oldValue);
                    return;
                }
                refreshSublist();
            }
        });
        sublistToTime.valueProperty().addListener(new ChangeListener<LocalTime>() {
            @Override
            public void changed(ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) {
                if(newValue == null){
                    sublistToTime.setValue(oldValue);
                    return;
                }
                LocalDateTime newLDT = LocalDateTime.of(sublistToDate.getValue(),newValue);
                LocalDateTime fromLDT = LocalDateTime.of(sublistFromDate.getValue(),sublistFromTime.getValue());
                if(!newLDT.isAfter(fromLDT)) {
                    sublistToTime.setValue(oldValue);
                    return;
                }
                refreshSublist();
            }
        });
        daysTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Matcher matcher = integerString.matcher(newValue);
                if(!matcher.matches()){
                    daysTxt.setText(oldValue);
                }
                if(Integer.parseInt(daysTxt.getText()) > 24854) {
                    daysTxt.setText(oldValue);
                }
            }
        });
        hoursTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Matcher matcher = integerString.matcher(newValue);
                if(!matcher.matches()){
                    hoursTxt.setText(oldValue);
                }
                if(Integer.parseInt(hoursTxt.getText()) > 23){
                    hoursTxt.setText(oldValue);
                }
            }
        });
        minutesTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Matcher matcher = integerString.matcher(newValue);
                if(!matcher.matches()){
                    minutesTxt.setText(oldValue);
                }
                if(Integer.parseInt(minutesTxt.getText()) > 59){
                    minutesTxt.setText(oldValue);
                }
            }
        });
        observableList.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> c) {
                c.next();
                if(c.wasAdded()){
                    List <Task> addedSubList = (List <Task>)c.getAddedSubList();
                    for(Task task : addedSubList) {
                        Date date = task.nextTimeAfter(Utill.localDateTimeToDate(LocalDateTime.of(sublistFromDate.getValue(),sublistFromTime.getValue())));
                        if(date == null) {
                            return;
                        }
                        if(!date.after(Utill.localDateTimeToDate(LocalDateTime.of(sublistToDate.getValue(),sublistToTime.getValue())))){
                            sublist.add(task);
                        }
                    }
                }
                if(c.wasRemoved()) {
                    List <Task> removedSublist = (List<Task>)c.getRemoved();
                    sublist.removeAll(removedSublist);
                }
                saveTasks();
            }
        });
        taskDetailClmn_all.setCellValueFactory(new PropertyValueFactory<>("observableDetails"));
        timeClmn_all.setCellValueFactory(new PropertyValueFactory<>("observableTime"));
        fromClmn_all.setCellValueFactory(new PropertyValueFactory<>("observableStart"));
        toClmn_all.setCellValueFactory(new PropertyValueFactory<>("observableEnd"));
        intervalClmn_all.setCellValueFactory(new PropertyValueFactory<>("observableInterval"));
        isActiveClmn_all.setCellValueFactory(new PropertyValueFactory<>("observableIsActive"));
        tableAll.setItems(observableList);
        taskDetailClmn_sublist.setCellValueFactory(new PropertyValueFactory<>("observableDetails"));
        timeClmn_sublist.setCellValueFactory(new PropertyValueFactory<>("observableTime"));
        fromClmn_sublist.setCellValueFactory(new PropertyValueFactory<>("observableStart"));
        toClmn_sublist.setCellValueFactory(new PropertyValueFactory<>("observableEnd"));
        intervalClmn_sublist.setCellValueFactory(new PropertyValueFactory<>("observableInterval"));
        isActiveClmn_sublist.setCellValueFactory(new PropertyValueFactory<>("observableIsActive"));
        tableWeek.setItems(sublist);
        observableList.addListener(new ListChangeListener<Task>() {
            @Override
            public void onChanged(Change<? extends Task> c) {
                saveTasks();
            }
        });
        MenuItem editMenuItemAll = new MenuItem("Edit");
        MenuItem removeMenuItemAll = new MenuItem("Remove");
        removeMenuItemAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Task task = tableAll.getSelectionModel().getSelectedItem();
                if(task == null) {
                    return;
                }
                try {
                    Stage stage = getRemoveTaskStage(task, primStage, mainController);
                    stage.show();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        editMenuItemAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Task task = tableAll.getSelectionModel().getSelectedItem();
                if(task == null) {
                    return;
                }
                try {
                    Stage stage = getEditTaskStage(task, primStage, mainController);
                    stage.show();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        ContextMenu editMenuAll = new ContextMenu(editMenuItemAll, removeMenuItemAll);
        editMenuAll.setAutoHide(true);
        tableAll.setContextMenu(editMenuAll);
        MenuItem editMenuItemWeek = new MenuItem("Edit");
        MenuItem removeMenuItemWeek = new MenuItem("Remove");
        removeMenuItemWeek.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Task task = tableWeek.getSelectionModel().getSelectedItem();
                if(task == null){
                    return;
                }
                try {
                    Stage stage = getRemoveTaskStage(task, primStage, mainController);
                    stage.show();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        editMenuItemWeek.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Task task = tableWeek.getSelectionModel().getSelectedItem();
                if(task == null){
                    return;
                }
                try {
                    Stage stage = getEditTaskStage(task, primStage, mainController);
                    stage.show();
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        ContextMenu editMenuWeek = new ContextMenu(editMenuItemWeek,removeMenuItemWeek);
        editMenuWeek.setAutoHide(true);
        tableWeek.setContextMenu(editMenuWeek);
    }

    /**
     * Loads the tasks are stored in the file
     * and adds them to the observableList
     * @see TaskIO
     * */
    private ObservableList<Task> loadTaskList() throws IOException {
        System.out.println(new StringBuilder(getClass().getName()).append(": loading the task list"));
        List <Task> list = new LinkedList<>();
        observableList = FXCollections.observableArrayList(Collections.synchronizedList(list));
        TaskIO.readText(observableList,tasksFile);
        System.out.println(new StringBuilder(getClass().getName()).append(": the list has benn loaded: ").append(observableList));
        return observableList;
    }

    /**
     * Rewrites the contents of the saving file with the contents of the observableList
     * */
    public void saveTasks () {
        System.out.println(new StringBuilder(getClass().getName()).append(": saving the tasks"));
        try {
            TaskIO.writeText(observableList,tasksFile);
            System.out.println(new StringBuilder(getClass().getName()).append(": tasks have been successfully saved"));
        }catch (IOException e) {
            System.out.println(e.getCause());
        }
    }

    /**
     * Returns the stage was created in the method start()
     * */
    public Stage getPrimStage() {
        return primStage;
    }

    public void removeTask(Task task) {
        if(observableList.removeAll(task) && sublist.removeAll(task)) {
            System.out.println(new StringBuilder(getClass().getName()).append(": ").append(task).append(" has been removed"));
        }
    }

    @Override
    public void stop() throws Exception {
        System.out.println(new StringBuilder(getClass().getName()).append(": application stops"));
        System.exit(1);
    }

    /**
     * The method refreshes the form which operates with constructing and
     * adding a task to the obsevableList
     * */
    private void refreshAddForm() {
        addTaskBtn.setVisible(false);
        spinner.setVisible(true);
        taskDetails.setText("");
        startDate.setValue(LocalDate.now());
        startTime.setValue(LocalTime.now());
        endDate.setValue(LocalDate.now().plusDays(7));
        endTime.setValue(LocalTime.now());
        daysTxt.setText("0");
        hoursTxt.setText("0");
        minutesTxt.setText("5");
        spinner.setVisible(false);
    }
}