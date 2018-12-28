package ua.sumdu.j2se.Birintsev.tasks.Model;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ArrayTaskList extends TaskList{

    private Task [] taskList = new Task [5];
    private int amOfTasks;

    public ArrayTaskList(){

    }

    public ArrayTaskList(int amount){
        if(amount < 1){
            throw new IllegalArgumentException("The length of the list can not be less than 0");
        }
        taskList = new Task[amount];
    }

    public void add(Task task){
        if(task == null){
            throw new IllegalArgumentException("Try to add an empty task");
        }
        if(amOfTasks == taskList.length){
            Task [] temp = new Task[(int)(taskList.length * 1.2) + 1];
            for(int i = 0; i < /*taskList.length*/amOfTasks; i++){
                temp[i] = taskList[i];
            }
            taskList = temp;
        }
        taskList[amOfTasks] = task;
        amOfTasks++;
    }

    public boolean remove(Task task){
        if(task == null){
            throw new IllegalArgumentException("Try to remove an empty task");
        }
        if(amOfTasks >= 1){
            for(int i = 0; i < amOfTasks; i++){
                if(taskList[i].equals(task)){
                    taskList[i] = taskList[amOfTasks - 1];
                    taskList[amOfTasks - 1] = null;
                    amOfTasks--;
                    return true;
                }
            }
        }/*else if(taskList[0] == task){
            taskList[0] = null;
            amOfTasks--;
            return true;
        }*/
        return false;
    }

    public int size(){
        return amOfTasks;
    }

    public Task getTask(int index){
        if(index < amOfTasks && index >= 0){
            return taskList[index];
        }
        else{
            return null;
        }
    }
    /*
        public ArrayTaskList incoming(int from, int to){
            if(from < 0){
                throw new IllegalArgumentException("The execution start can not be less than 0");
            }
            if(to < 0){
                throw new IllegalArgumentException("The execution finish can not be less than 0");
            }
            if(to <= from){
                throw new IllegalArgumentException("The execution finish can not be less or equal to execution start");
            }
            ArrayTaskList subset = new ArrayTaskList();
                for(int i = 0; i < amOfTasks; i++) {
                    if(taskList[i].nextTimeAfter(from) > from &&  taskList[i].nextTimeAfter(from) <= to) {
                        if(taskList[i].isActive){
                            subset.add(taskList[i]);
                        }
                    }
                }
            return subset;
        }
     */
    public Iterator<Task> iterator(){
        return new ArrayTaskListIterator();
    }

    private class ArrayTaskListIterator implements Iterator<Task>{

        private boolean wasMoved;
        private int indexCurrentElement = -1;

        public Task next() {
            if(indexCurrentElement + 1 > amOfTasks - 1){
                throw new NoSuchElementException();
            }else{
                wasMoved = true;
                return taskList[++indexCurrentElement];
            }
        }

        public boolean hasNext(){
            return indexCurrentElement + 1 <= amOfTasks - 1;
        }

        public void remove() {
            if(!wasMoved){
                throw new IllegalStateException();
            }else{
                for(int i = indexCurrentElement; i <= amOfTasks - 2; i++){
                    taskList[i] = taskList[i + 1];
                }
                taskList[amOfTasks - 1] = null;
                /*taskList[indexCurrentElement] = taskList[amOfTasks - 1];
                taskList[amOfTasks - 1] = null;*/
                wasMoved = false;
                amOfTasks--;
                indexCurrentElement--;
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayTaskList that = (ArrayTaskList) o;
        if (amOfTasks != that.amOfTasks) return false;
        for(int i = 0; i < amOfTasks; i++){
            if(!taskList[i].equals(that.getTask(i))){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(amOfTasks);
        result = 31 * result + Arrays.hashCode(taskList);
        return result;
    }

    /*@Override
    public int hashCode() {
        int result = 0;
        for(int i = 0; i < amOfTasks; i++){
            result += taskList[i].hashCode() * (amOfTasks - 1 - i);
        }
        result *= (amOfTasks % 3) + 1;
        return result;
    }*/

    /*@Override
    public String toString() {
        String string = "ArrayTaskList[" + amOfTasks + "]:{";
        for(int i = 0; i < amOfTasks; i++) {
            string += "[ " + taskList[i].toString() + " ] ";
        }
        string += "}";
        return string;
    }*/

    /*@Override
    public String toString() {
        StringBuilder string = new StringBuilder().append("ArrayTaskList[").append(amOfTasks).append("]:{");
        for(int i = 0; i < amOfTasks; i++) {
            string.append("[ ").append(taskList[i].toString()).append(" ] ");
        }
        string.append("}");
        return string.toString();
    }*/

    @Override
    public ArrayTaskList clone() throws CloneNotSupportedException {
        ArrayTaskList clone = (ArrayTaskList) super.clone();
        clone.taskList = new Task[amOfTasks];
        for(int i = 0; i < amOfTasks; i++){
            clone.taskList[i] = taskList[i].clone();
        }
        return clone;
    }

}
