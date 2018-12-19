package ua.sumdu.j2se.Birintsev.tasks;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.beans.value.ObservableStringValue;

import javax.management.JMRuntimeException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ua.sumdu.j2se.Birintsev.tasks.TaskIO.dateCellFormater;


public class Task implements Cloneable, Serializable {

    private String details;
    private boolean isActive;
    private boolean isRepeated;
    private Date time;
    private int interval;
    private Date start;
    private Date end;

    /**
     * Property variables to show task info in the TableView
     */
    private StringProperty observableDetails;
    private StringProperty observableTime;
    private StringProperty observableInterval;
    private StringProperty observableStart;
    private StringProperty observableEnd;
    private StringProperty observableIsActive;

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
        setObservableStart(dateCellFormater.format(time));
        return observableTime.get();
    }

    public StringProperty observableTimeProperty() {
        return observableTime;
    }

    private void setObservableTime(String observableTime) {
        this.observableTime.set(observableTime);
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

    private void setObservableInterval(String observableInterval) {
        this.observableInterval.set(observableInterval);
    }

    public String getObservableStart() {
        setObservableStart(dateCellFormater.format(start));
        return observableStart.get();
    }

    public StringProperty observableStartProperty() {
        return observableStart;
    }

    private void setObservableStart(String observableStart) {
        this.observableStart.set(observableStart);
    }

    public String getObservableEnd() {
        setObservableEnd(dateCellFormater.format(end));
        return observableEnd.get();
    }

    public StringProperty observableEndProperty() {
        return observableEnd;
    }

    private void setObservableEnd(String observableEnd) {
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

    public Task(String title, Date time) {
        if (title == null || title.equals("")) {
            throw new IllegalArgumentException("The details of task must be set");
        }
        details = title;
        this.time = this.start = this.end = time;

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
        /*-----------------------------------------*/

    }

    public Task(String title, Date start, Date end, int interval) {
        if (start.after(end)) {
            throw new IllegalArgumentException("Start time can not be less than end time");
        }
        if (interval <= 0) {
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
        /*-----------------------------------------*/
    }

    public String getTitle() {
        return details;
    }

    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("The details of task must be set");
        }
        details = title;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
        getObservableIsActive();
    }

    public Date getTime() {
        if (isRepeated) {
            return start;
        } else {
            return time;
        }
    }

    public void setTime(Date time) {
        isRepeated = false;
        this.time = this.start = this.end = time;
        getObservableTime();
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

    public void setTime(Date start, Date end, int interval) {
        if(start == null){
            throw new IllegalArgumentException("Parameter start must not be null");
        }
        if(end == null){
            throw new IllegalArgumentException("Parameter end must not be null");
        }
        if (start.after(end)) {
            throw new IllegalArgumentException("End time must be later than end time");
        }
        if (interval <= 0) {
            throw new IllegalArgumentException("Interval time must be greater than 0");
        }
        isRepeated = true;
        this.start = start;
        this.end = end;
        this.interval = interval;
        getObservableEnd();
        getObservableStart();
    }

    public boolean isRepeated() {
        return isRepeated;
    }

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

   /* @Override
    public String toString(){
        String string;
        string = (isActive ? "Active " : "Unactive ");
        string = string + "task: " + details;
        string = string + ",time is: " + (isRepeated ? (start + "-" + end + "(" + interval + ")") : time);
        return string;
    }*/

    /*@Override
    public String toString(){
        StringBuilder string = new StringBuilder();
        string.append(isActive ? "Active " : "Unactive ").append("task: ").append(details).append(",time is: ");
        if(isRepeated){
            string.append(start).append("-").append(end).append("(").append(interval).append(")");
        }else{
            string.append(time);
        }
        return string.toString();
    }*/

    @Override
    public String toString() {
        StringBuilder taskInfo = new StringBuilder("\"");
        taskInfo.append(details.replace("\"", "\"\"")).append('"');
        SimpleDateFormat dateFormat = new SimpleDateFormat(new StringBuilder("[").append(TaskIO.dateFormate).append("]").toString());
        if(isRepeated){
            taskInfo.append(" from ").append(dateFormat.format(start)).append(" to ").append(dateFormat.format(end)).append(" every [");
            //int interval = this.interval; // in seconds
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

    /*StringBuilder taskInfo = new StringBuilder("\"");
            taskInfo.append(task.getTitle().replace("\"","\"\"")).append('"');
            SimpleDateFormat dateFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss.S]");
            if(task.isRepeated()){
                taskInfo.append(" from ").append(dateFormat.format(task.getStartTime())).append(" to ").append(dateFormat.format(task.getEndTime())).append(" every [");
                int interval = task.getRepeatInterval(); // in seconds
                int days = interval / 86400;
                int hours = (interval - days * 86400) / 3600;
                int minutes = (interval - days * 86400 - hours * 3600) / 60;
                int seconds = interval - days * 86400 - hours * 3600 - minutes * 60;
                StringBuilder intervalFormat = new StringBuilder();
                if(days != 0){
                    intervalFormat.append(days).append((days > 1 ? " days " : " day "));
                }
                if(hours != 0){
                    intervalFormat.append(hours).append((hours > 1 ? " hours" : " hour "));
                }
                if(minutes != 0){
                    intervalFormat.append(minutes).append((minutes > 1 ? " minutes " : " minute "));
                }
                if(seconds != 0){
                    intervalFormat.append(seconds).append((seconds > 1 ? " seconds " : " second "));
                }
                taskInfo.append(intervalFormat.substring(0,intervalFormat.length()-1)).append(']');
            }else{
                taskInfo.append(" at ").append(dateFormat.format(task.getTime()));
            }
            if(!task.isActive()){
                taskInfo.append(" inactive");
            }*/

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
                if(start.compareTo(task.start) == 0 && end.compareTo(task.end) == 0 && interval == task.interval) return true;
            }else{
                if(time.compareTo(task.time) == 0) return true;
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
    public Task clone() throws CloneNotSupportedException { // clone переписать ???
        Task temp = (Task) super.clone();
        temp.time = (Date) time.clone();
        temp.start = (Date) start.clone();//!!!!!!!!!!!!!!
        temp.end = (Date) end.clone();
        return temp;
    }

    public String getDetails() {
        return details;
    }

    public Date getStart() {
        return start;
    }
}