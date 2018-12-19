package ua.sumdu.j2se.Birintsev.tasks;


import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public abstract class TaskList implements Cloneable, Serializable, Iterable<Task>{
    
    abstract public void add(Task task);        
    abstract public boolean remove(Task task);    
    abstract public int size();
    abstract public Task getTask(int index);

    @Override
    public String toString() {
        Iterator<Task> iter = this.iterator();
        StringBuilder taskListInfo = new StringBuilder();
        while (iter.hasNext()) {
            Task task = iter.next();
            taskListInfo.append(task.toString());
            if(iter.hasNext()){
                taskListInfo.append(";\n");
            }else{
                taskListInfo.append('.');
            }
        }
        return taskListInfo.toString();
    }

    /*
    public TaskList incoming(Date from, Date to){
        if (from == null) {
            throw new IllegalArgumentException("From time must not be null");
        }
        if (to == null) {
            throw new IllegalArgumentException("To time must not be null");
        }
        if (from.after(to)) {
            throw new IllegalArgumentException("Start time can not be less than end time");
        }

        LinkedTaskList subset = new LinkedTaskList();                   
        for(int i = 0; i < size(); i++) {            
            if(getTask(i).nextTimeAfter(from).after(from) && getTask(i).nextTimeAfter(from).compareTo(to) <= 0) {                    
                if(getTask(i).isActive()){                    
                    subset.add(getTask(i));                    
                }                    
            }                
        }                
        return subset;
    }
    */
}