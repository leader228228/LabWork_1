package ua.sumdu.j2se.Birintsev.tasks.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ua.sumdu.j2se.Birintsev.tasks.Utill;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static ua.sumdu.j2se.Birintsev.tasks.Utill.dateCellFormater;
import static ua.sumdu.j2se.Birintsev.tasks.Utill.dateFormate;

public class Task implements Cloneable, Serializable {

    private String details;
    private boolean isActive;
    private boolean isRepeated;
    private Date time;
    private int interval;
    private Date start;
    private Date end;

    /**
     * Property variables to show task info in the TableView in the text form
     * Each method that gets their properties refreshes them before according
     * to the corresponding parameter of the task
     */
    private StringProperty observableDetails;
    private StringProperty observableTime;
    private StringProperty observableInterval;
    private StringProperty observableStart;
    private StringProperty observableEnd;
    private StringProperty observableIsActive;

    private LocalDateTime timeLocalDateTime;
    private LocalDateTime startLocalDateTime;
    private LocalDateTime endLocalDateTime;

    public LocalDateTime getTimeLocalDateTime(){
        return Utill.dateToLocalDateTime(time);
    }

    public LocalDateTime getStartLocalDateTime(){
        return Utill.dateToLocalDateTime(start);
    }

    public LocalDateTime getEndLocalDateTime(){
        return Utill.dateToLocalDateTime(end);
    }

    public String getObservableDetails() {
        observableDetails.set(details);
        return observableDetails.get();
    }

    public StringProperty observableDetailsProperty() {
        return observableDetails;
    }

    private void setObservableDetails(String observableDetails) {
        this.observableDetails.set(observableDetails);
    }

    public String getObservableTime() {
        if(isRepeated){
            setObservableTime("");
        } else {
            setObservableTime(dateCellFormater.format(time));
        }
        return observableTime.get();
    }

    public StringProperty observableTimeProperty() {
        return observableTime;
    }

    private void setObservableTime(String observableTime) {
        this.observableTime.set(observableTime);
    }

    public StringProperty getObservableIntervalProperty(){
        return observableInterval;
    }

    public StringProperty getObservableEndProperty(){
        return observableEnd;
    }

    public StringProperty getObservableStartProperty(){
        return observableStart;
    }


    public String getObservableInterval() {
        String interval;
        if(isRepeated){
            Pattern pattern = Pattern.compile("every \\[(.*)]");
            Matcher matcher = pattern.matcher(toString());
            matcher.find();
            interval =  new StringBuilder("Every ").append(matcher.group(1)).toString();
        }else{
            interval = "";
        }
        setObservableInterval(interval);
        return observableInterval.get();
    }

    public StringProperty observableIntervalProperty() {
        return observableInterval;
    }

    public void setObservableInterval(String observableInterval) {
        this.observableInterval.set(observableInterval);
    }

    public String getObservableStart() {
        if(isRepeated){
            setObservableStart(dateCellFormater.format(start));
        }else{
            setObservableStart("");
        }
        return observableStart.get();
    }

    public StringProperty observableStartProperty() {
        return observableStart;
    }

    private void setObservableStart(String observableStart) {
        this.observableStart.set(observableStart);
    }

    public String getObservableEnd() {
        if(isRepeated){
            setObservableEnd(dateCellFormater.format(end));
        }else{
            setObservableEnd("");
        }
        return observableEnd.get();
    }

    public StringProperty observableEndProperty() {
        return observableEnd;
    }

    public void setObservableEnd(String observableEnd) {
        this.observableEnd.set(observableEnd);
    }

    public String getObservableIsActive() {
        if(isActive){
            setObservableIsActive("Active");
        }else{
            setObservableIsActive("Disabled");
        }
        return observableIsActive.get();
    }

    public StringProperty observableIsActiveProperty() {
        return observableIsActive;
    }

    private void setObservableIsActive(String observableIsActive) {
        this.observableIsActive.set(observableIsActive);
    }

    /**
     * Creates a non-repetitive task
     * @param title must not be empty or null
     * @param time must not be null
     * */
    public Task(String title, Date time) {
        if (title == null || "".equals(title) || title.equals("\uFEFF")) {
            System.err.println(new StringBuilder(getClass().getName()).append("The details of a task must be set"));
            throw new IllegalArgumentException("The details of task must be set");
        }
        details = title;
        this.time = this.start = this.end = time;
        observableDetails = new SimpleStringProperty();
        observableDetails.set(details);
        observableTime = new SimpleStringProperty();
        observableStart = new SimpleStringProperty();
        observableEnd = new SimpleStringProperty();
        observableInterval = new SimpleStringProperty();
        observableIsActive = new SimpleStringProperty();
        /* Initialization the SimpleStringProperty */
        getObservableDetails();
        getObservableTime();
        getObservableStart();
        getObservableEnd();
        getObservableInterval();
        getObservableIsActive();
    }

    /**
     * Creates a repetitive task
     * @param title must not be empty or null
     * @param start must not be null
     * @param end must not be null
     * @param interval must be a positive integer
     * */
    public Task(String title, Date start, Date end, int interval) {
        if (title == null || "".equals(title) || title.equals("\uFEFF")) {
            System.err.println(new StringBuilder(getClass().getName()).append("The details of a task must be set"));
            throw new IllegalArgumentException("The details of task must be set");
        }
        if (start.after(end)) {
            System.err.println(new StringBuilder(getClass().getName()).append("Start time can not be less than end time"));
            throw new IllegalArgumentException("Start time can not be less than end time");
        }
        if (interval <= 0) {
            System.err.println(new StringBuilder(getClass().getName()).append("Interval time must be greater than 0"));
            throw new IllegalArgumentException("Interval time must be greater than 0");
        }
        isRepeated = true;
        details = title;
        this.time = start;
        this.start = start;
        this.end = end;
        this.interval = interval;
        observableDetails = new SimpleStringProperty();
        observableTime = new SimpleStringProperty();
        observableStart = new SimpleStringProperty();
        observableEnd = new SimpleStringProperty();
        observableInterval = new SimpleStringProperty();
        observableIsActive = new SimpleStringProperty();
        /* Initialization the SimpleStringProperty */
        getObservableDetails();
        getObservableTime();
        getObservableStart();
        getObservableEnd();
        getObservableInterval();
        getObservableIsActive();
    }

    public String getTitle() {
        return details;
    }

    public StringProperty getObservableTimeProperty(){
        return observableTime;
    }

    /**
     * Changes the title of a task
     * @param title must not be empty or null
     * @throws IllegalArgumentException if title is null or empty
     * */
    public void setTitle(String title) {
        if (title == null || "".equals(title)) {
            System.err.println(new StringBuilder(getClass().getName()).append("The details of task must be set"));
            throw new IllegalArgumentException("The details of task must be set");
        }
        details = title;
        observableDetailsProperty().set(title);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        getObservableIsActive();
    }

    /**
     * @return the start time if a task is repititive
     *          either time if it is not
     * */
    public Date getTime() {
        if (isRepeated) {
            return start;
        } else {
            return time;
        }
    }

    /**
     * @param time must not be null
     * @throws IllegalArgumentException if parameter time is null
     * Sets the time of the task. Turns it kind to non-repetitive if it was a repetitive one.
     * */
    public void setTime(Date time) {
        if(time == null){
            System.err.println(new StringBuilder(getClass().getName()).append("Parameter time must not be null"));
            throw new IllegalArgumentException("Parameter time must not be null");
        }
        isRepeated = false;
        this.time = this.start = this.end = time;
        getObservableTime();
        getObservableStart();
        getObservableEnd();
        getObservableInterval();
    }

    public Date getStartTime() {
        if (isRepeated) {
            return start;
        } else {
            return time;
        }
    }

    public Date getEndTime() {
        if (isRepeated) {
            return end;
        } else {
            return time;
        }
    }

    public int getRepeatInterval() {
        if (isRepeated) {
            return interval;
        } else {
            return 0;
        }
    }

    public void setRepeated(boolean repeated) {
        isRepeated = repeated;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    /**
     * @param start must not be null
     * @param end must not be null
     * @param interval must be a positive integer
     * @throws IllegalArgumentException if start/end is null or start is later than end or interval is less than or
     *         equal to zero.
     * Sets the time of the task. Turns it kind to repetitive if it was a non-repetitive one.
     * */
    public void setTime(Date start, Date end, int interval) {
        if(start == null){
            System.err.println(new StringBuilder(getClass().getName()).append("Parameter start must not be null"));
            throw new IllegalArgumentException("Parameter start must not be null");
        }
        if(end == null){
            System.err.println(new StringBuilder(getClass().getName()).append("Parameter end must not be null"));
            throw new IllegalArgumentException("Parameter end must not be null");
        }
        if (start.after(end)) {
            System.err.println(new StringBuilder(getClass().getName()).append("End time must be later than end time"));
            throw new IllegalArgumentException("End time must be later than end time");
        }
        if (interval <= 0) {
            System.err.println(new StringBuilder(getClass().getName()).append("Interval time must be greater than 0"));
            throw new IllegalArgumentException("Interval time must be greater than 0");
        }
        isRepeated = true;
        this.start = start;
        this.end = end;
        this.interval = interval;
        getObservableTime();
        getObservableStart();
        getObservableEnd();
        getObservableInterval();
    }

    public boolean isRepeated() {
        return isRepeated;
    }

    /**
     * @return the Date of the following happening of the Task. null if the currentDate is later than the endTime/time
     * (repetative task/ unrepetetive task) or if the task is inactive
     * */
    public Date nextTimeAfter(Date current) {
        if (isActive) {
            if (isRepeated) {
                if (current.after(end)) {
                    return null;
                }
                Date temp = new Date(start.getTime());
                for (long i = start.getTime(); temp.compareTo(end) <= 0; i += interval*1000, temp.setTime(i)) {
                    if (i > current.getTime()) {
                        return new Date(i);
                    }
                }
            } else {
                if (time.after(current)) {
                    return time;
                }
            }
        } 
        return null;        
    }

    /**
     * Fits the demands of the text output of a TaskList and a Task to a text file
     * @see ua.sumdu.j2se.Birintsev.tasks.model.TaskIO
     * @see ua.sumdu.j2se.Birintsev.tasks.model.taskcollections.TaskList
     * */
    @Override
    public String toString() {
        StringBuilder taskInfo = new StringBuilder("\"");
        taskInfo.append(details.replace("\"", "\"\"")).append('"');
        SimpleDateFormat dateFormat = new SimpleDateFormat(new StringBuilder("[").append(dateFormate).append("]").toString());
        if(isRepeated){
            taskInfo.append(" from ").append(dateFormat.format(start)).append(" to ").append(dateFormat.format(end)).append(" every [");
            int days = interval / 86400;
            int hours = (interval - days * 86400) / 3600;
            int minutes = (interval - days * 86400 - hours * 3600) / 60;
            int seconds = interval - days * 86400 - hours * 3600 - minutes * 60;
            StringBuilder intervalFormat = new StringBuilder();
            if (days != 0) {
                intervalFormat.append(days).append((days > 1 ? " days " : " day "));
            }
            if (hours != 0) {
                intervalFormat.append(hours).append((hours > 1 ? " hours " : " hour "));
            }
            if (minutes != 0) {
                intervalFormat.append(minutes).append((minutes > 1 ? " minutes " : " minute "));
            }
            if (seconds != 0) {
                intervalFormat.append(seconds).append((seconds > 1 ? " seconds " : " second "));
            }
            taskInfo.append(intervalFormat.substring(0, intervalFormat.length() - 1)).append(']');
        } else {
            taskInfo.append(" at ").append(dateFormat.format(time));
        }
        if (!isActive) {
            taskInfo.append(" inactive");
        }
        return taskInfo.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }
        Task task = (Task) o;
        if(details.equals(task.details) && isActive == task.isActive && isRepeated == task.isRepeated){
            if(isRepeated){
                return start.compareTo(task.start) == 0 && end.compareTo(task.end) == 0 && interval == task.interval;
            }else{
                return time.compareTo(task.time) == 0;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int coef1 = (isActive ? 17 + (time == null ? 23 :time.hashCode()) : 23 + (end == null ? 79 : end.hashCode()));
        int coef2 = (isRepeated ? (start.hashCode() / interval + end.hashCode() - interval * interval) : time.hashCode() * coef1);
        int coef3 = (details.hashCode() % 2 == 0 ? (coef1 % 3) : (coef2 % 7));
        return coef1 * coef2 + coef3 * details.hashCode();
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        Task temp = (Task) super.clone();
        temp.time = (Date) time.clone();
        temp.start = (Date) start.clone();
        temp.end = (Date) end.clone();
        return temp;
    }

}