package ua.sumdu.j2se.Birintsev.tasks.model.taskcollections;

import ua.sumdu.j2se.Birintsev.tasks.model.Task;
import java.io.Serializable;
import java.util.Iterator;

public abstract class TaskList implements Cloneable, Serializable, Iterable<Task>{

    /**
     * Adds a task to a TaskList
     * @param task must not be null
     * */
    abstract public void add(Task task);

    /**
     * Removes a task from a TaskList
     * @param task must not be null
     * @return true if there was the task in the list and
     *         it was successfully removed
     * */
    abstract public boolean remove(Task task);
    abstract public int size();

    /**
     * @param index the index of the task is got from the ArrayList
     * @return null if the index value is less than 0 or more than
     *         the amount of the tasksin the TaskList
     * */
    abstract public Task getTask(int index);

    /**
     * Uses the realization of the Task.toString()
     * Fits the demands of the text output of the TaskList to a text file
     * @see ua.sumdu.j2se.Birintsev.tasks.model.TaskIO
     * */
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
}