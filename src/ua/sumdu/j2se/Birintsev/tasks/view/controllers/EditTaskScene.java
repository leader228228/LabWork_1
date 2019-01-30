package ua.sumdu.j2se.Birintsev.tasks.view.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ua.sumdu.j2se.Birintsev.tasks.model.Task;
import ua.sumdu.j2se.Birintsev.tasks.Utill;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.regex.Matcher;
import static ua.sumdu.j2se.Birintsev.tasks.Utill.*;
import static ua.sumdu.j2se.Birintsev.tasks.Utill.integerString;

/**
 * The class that represents a scene which handles with
 * changing the properties of some task is given to it constructor
 * @see View
 * @see Task
 * */
public class EditTaskScene {

    @FXML
    private TextField taskDetails;

    @FXML
    private Button saveChangesBtn;

    @FXML
    private JFXDatePicker startDate;

    @FXML
    private JFXTimePicker startTime;

    @FXML
    private Text fromLabel;

    @FXML
    private JFXDatePicker endDate;

    @FXML
    private JFXTimePicker endTime;

    @FXML
    private Text toLabel;

    @FXML
    private JFXCheckBox isActive;

    @FXML
    private JFXCheckBox isRepetative;

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

    private Task task;

    private View view;

    public EditTaskScene(Task task, View view){
        this.task = task;
        this.view = view;
        System.out.println(new StringBuilder(getClass().getName()).append(": controller created. Task: ").append(task).append("View: ").append(view));
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        System.out.println(new StringBuilder(getClass().getName()).append(": saving the changes starts"));
        String title = taskDetails.getText();
        Date start = Utill.localDateTimeToDate(LocalDateTime.of(startDate.getValue(),startTime.getValue()));
        if(isRepetative.isSelected()){
            Date end = Utill.localDateTimeToDate(LocalDateTime.of(endDate.getValue(),endTime.getValue()));
            int interval = Utill.getIntervalFromStrings(daysTxt.getText(), hoursTxt.getText(), minutesTxt.getText());
            task.setTime(start, end, interval);
        }else {
            task.setTime(start);
        }
        task.setTitle(title);
        task.setActive(isActive.isSelected());
        view.saveTasks();
        view.refreshSublist();
        System.out.println(new StringBuilder(getClass().getName()).append(": saving the changes finished"));
    }

    @FXML
    private void changeEditionMode(){
        if(isRepetative.isSelected()){
            fromLabel.setText("From");
            showNodes(toLabel,endDate,endTime,everyLabel,daysLabel,daysTxt,hoursLabel,hoursTxt,minutesLabel,minutesTxt);
            if(!task.getEndLocalDateTime().isAfter(LocalDateTime.of(startDate.getValue(), startTime.getValue()))){
                endDate.setValue(startDate.getValue().plusDays(7));
            }else{
                endDate.setValue(task.getEndLocalDateTime().toLocalDate());
            }
            endTime.setValue(task.getEndLocalDateTime().toLocalTime());
        } else {
            fromLabel.setText("Time");
            hideNodes(toLabel,endDate,endTime,everyLabel,daysLabel,daysTxt,hoursLabel,hoursTxt,minutesLabel,minutesTxt);
        }
        System.out.println(new StringBuilder(getClass().getName()).append(": edition mode ").append(isRepetative.isSelected() ? "repetative" : "non-repetative"));
    }

    @FXML
    public void initialize() {
        System.out.println(new StringBuilder(getClass().getName()).append(": initalizing starts"));
        taskDetails.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue == null || "".equals(newValue) || newValue.length() == 0 || "\uFEFF".equals(newValue)){
                    saveChangesBtn.setVisible(false);
                } else {
                    saveChangesBtn.setVisible(true);
                }
            }
        });

        daysTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Matcher matcher = integerString.matcher(newValue);
                if(!matcher.matches()){
                    daysTxt.setText(oldValue);
                }
                if(Integer.parseInt(daysTxt.getText()) > 24854){
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

        refreshValues();


        startDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if(!isRepetative.isSelected()){
                    return;
                }
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
                if(!isRepetative.isSelected()){
                    return;
                }
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
                if(!isRepetative.isSelected()){
                    return;
                }
                if(newValue == null){
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
                if(!isRepetative.isSelected()){
                    return;
                }
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
        System.out.println(new StringBuilder(getClass().getName()).append(": initalizing finished"));
    }

    private void refreshValues(){
        System.out.println(new StringBuilder(getClass().getName()).append(": refreshing the edition form starts"));
        taskDetails.setText(task.getTitle());
        isActive.setSelected(task.isActive());
        isRepetative.setSelected(task.isRepeated());
        if(task.isRepeated()){
            fromLabel.setText("From");
            startDate.setValue(task.getStartLocalDateTime().toLocalDate());
            startTime.setValue(task.getStartLocalDateTime().toLocalTime());
            endDate.setValue(task.getEndLocalDateTime().toLocalDate());
            endTime.setValue(task.getEndLocalDateTime().toLocalTime());
            showNodes(toLabel,endDate,endTime,everyLabel,daysLabel,daysTxt,hoursLabel,hoursTxt,minutesLabel,minutesTxt);
        } else {
            fromLabel.setText("Time");
            startDate.setValue(task.getTimeLocalDateTime().toLocalDate());
            startTime.setValue(task.getTimeLocalDateTime().toLocalTime());
            endDate.setValue(task.getEndLocalDateTime().toLocalDate());
            endTime.setValue(task.getEndLocalDateTime().toLocalTime());
            hideNodes(toLabel,endDate,endTime,everyLabel,daysLabel,daysTxt,hoursLabel,hoursTxt,minutesLabel,minutesTxt);
        }
        System.out.println(new StringBuilder(getClass().getName()).append(": refreshing the edition form finished"));
    }
}
