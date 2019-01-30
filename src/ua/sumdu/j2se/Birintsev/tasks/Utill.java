package ua.sumdu.j2se.Birintsev.tasks;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ua.sumdu.j2se.Birintsev.tasks.view.controllers.EditTaskScene;
import ua.sumdu.j2se.Birintsev.tasks.view.controllers.RemoveTaskScene;
import ua.sumdu.j2se.Birintsev.tasks.view.controllers.View;
import ua.sumdu.j2se.Birintsev.tasks.model.Task;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is the class which contains helping methods.
 * */
public class Utill {

    public static final String dateFormate = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String dateCellFormate = "yyyy-MM-dd HH:mm";
    public static final String regexpRepetative = "^\"(.*)\" from \\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})] to \\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})] every \\[(.*)](.*)[.;]$";
    public static final String regexpNotRepetative = "^\"(.*)\" at \\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})](.*)[.;]$";
    public static final SimpleDateFormat dateCellFormater = new SimpleDateFormat(dateCellFormate);
    public static final Pattern patternRepetative = Pattern.compile(regexpRepetative);
    public static final Pattern patternNotRepetative = Pattern.compile(regexpNotRepetative);
    public static final Pattern integerString = Pattern.compile("(^0$)|(^[1-9]{1}\\d*$)");

    /**
     * The method parses the specified string value. If it finds an integer with [day(s)/hour(s)/minute(s)]
     * occurrence it converts the value into seconds.
     * @return 0 if no one occurrence has been found or the number of seconds in the time interval
     * @param string is the string to be parsed
     * */
    public static int parseInterval(String string) {
        if (string == null) {
            System.err.println(new StringBuilder("Utill.parseInterval : The string must not be null "));
            throw new IllegalArgumentException("The string must not be null");
        }
        int seconds = 0;
        Pattern pattern = Pattern.compile("(\\d*) day");
        Matcher matcher = pattern.matcher(string);
        if(matcher.find()) {
            seconds += Integer.parseInt(matcher.group(1)) * 86400;
        }
        pattern = Pattern.compile("(\\d*) hour");
        matcher = pattern.matcher(string);
        if(matcher.find()) {
            seconds += Integer.parseInt(matcher.group(1)) * 3600;
        }
        pattern = Pattern.compile("(\\d*) minute");
        matcher = pattern.matcher(string);
        if(matcher.find()) {
            seconds += Integer.parseInt(matcher.group(1)) * 60;
        }
        pattern = Pattern.compile("(\\d*) second");
        matcher = pattern.matcher(string);
        if(matcher.find()) {
            seconds += Integer.parseInt(matcher.group(1));
        }
        return seconds;
    }

    /**
     * @param seconds number of seconds to be parsed
     * @param seconds is positive
     * @throws IllegalArgumentException if seconds is negative
     * @return an integer value from 0 to 24855
     */
    public static int secondsToIntegerDays(int seconds) {
        if(seconds < 0){
            System.err.println(new StringBuilder("Variable seconds must not be less than 0. seconds = ").append(seconds));
            throw new IllegalArgumentException(new StringBuilder("Variable seconds must not be less than 0. seconds = ").append(seconds).toString());
        }
        return seconds / 86400;
    }

    /**
     * @param seconds number of seconds to be parsed
     * @param seconds is positive
     * @throws IllegalArgumentException if seconds param is negative
     * @return an integer value from 0 to 23
     */
    public static int secondsToIntegerHours(int seconds) {
        if(seconds < 0){
            System.err.println(new StringBuilder("Variable seconds must not be less than 0. seconds = ").append(seconds));
            throw new IllegalArgumentException(new StringBuilder("Variable seconds must not be less than 0. seconds = ").append(seconds).toString());
        }
        return (seconds - secondsToIntegerDays(seconds) * 86400) / 3600;
    }

    /**
     * @param seconds number of seconds to be parsed
     * @param seconds is positive
     * @throws IllegalArgumentException if seconds is negative
     * @return an integer value from 0 to 59
     */
    public static int secondsToIntegerMinutes(int seconds) {
        if(seconds < 0){
            System.err.println(new StringBuilder("Variable seconds must not be less than 0. seconds = ").append(seconds));
            throw new IllegalArgumentException(new StringBuilder("Variable seconds must not be less than 0. seconds = ").append(seconds).toString());
        }
        return (seconds - secondsToIntegerDays(seconds) * 86400 - secondsToIntegerHours(seconds) * 3600) / 60;
    }

    /**
     * Hides the specified nodes of a scene
     * */
    public static void hideNodes(Node...nodes) {
        for (Node node : nodes){
            node.setVisible(false);
        }
    }

    /**
     * Showes the specified nodes of a scene
     * */
    public static void showNodes(Node...nodes) {
        for (Node node : nodes){
            node.setVisible(true);
        }
    }

    /**
     * @param task task to be edited. Must not be null.
     * @param owner is is the stage which the stage we will place the result will be dependent on. Must not be null.
     * @param view is the main controller. Must not be null.
     * @NotNull task
     * @NotNull owner
     * @NotNull view
     * @return stage
     */
    public static Stage getEditTaskStage(Task task, Stage owner, View view) throws IOException {
        System.out.println(new StringBuilder("Utill.getEditTask.Stage: ").append("creating the task edition stage starts"));
        FXMLLoader loader = new FXMLLoader();
        EditTaskScene controller = new EditTaskScene(task, view);
        loader.setController(controller);
        loader.setLocation(EditTaskScene.class.getResource("fxml/EditTaskScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        System.out.println(new StringBuilder("Utill.getEditTask.Stage: ").append("creating the task edition stage finished"));
        return stage;
    }

    /**
     * @param task to be removed
     * @param owner is is the stage which the stage we will place the result will be dependent on
     * @param view is the main controller
     * @NotNull task
     * @NotNull owner
     * @NotNull view
     * @return stage
     */
    public static Stage getRemoveTaskStage(Task task, Stage owner, View view) throws IOException {
        System.out.println(new StringBuilder("Utill.getEditTask.Stage: ").append("creating the task removing stage starts"));
        FXMLLoader loader = new FXMLLoader();
        RemoveTaskScene controller = new RemoveTaskScene(task, view);
        loader.setController(controller);
        loader.setLocation(View.class.getResource("fxml/RemoveTaskScene.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        System.out.println(new StringBuilder("Utill.getEditTask.Stage: ").append("creating the task removing stage finished"));
        return stage;
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormate);
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormate);
        return LocalDateTime.parse(dateFormat.format(date),formatter);
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static int getIntervalFromStrings(String days, String hours, String minutes) {
        return Integer.parseInt(days) * 86400 + Integer.parseInt(hours) * 3600 + Integer.parseInt(minutes) * 60;
    }
}