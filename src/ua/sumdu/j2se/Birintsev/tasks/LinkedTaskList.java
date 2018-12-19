package ua.sumdu.j2se.Birintsev.tasks;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedTaskList extends TaskList{

    private Node head;
    private Node tail;
    private int amOfTasks;

    public LinkedTaskList(){
    
    }

    public void add(Task task){
        if(task == null){
            throw new IllegalArgumentException("Try to add an empty task");
        }
        Node newNode = new Node(task);
        if(head == null){
            head = newNode;
            tail = newNode;
            amOfTasks = 1;
            return;
        }
        tail.setNext(newNode);
        newNode.setPrevious(tail);
        tail = newNode;
        amOfTasks++;
    }
        
    public boolean remove(Task task){
        if(task == null){
            throw new IllegalArgumentException("Try to remove an empty task");
        }
        Node temp = head;
        while(temp != null && !temp.getTask().equals(task)){
            temp = temp.getNext();
        }
        if(temp != null && temp.getTask().equals(task)){
            if(temp.getPrevious() != null){
                temp.getPrevious().setNext(temp.getNext());
                if(tail == temp){
                    tail = temp.getPrevious();
                }
            }
            if(temp.getNext() != null){
                temp.getNext().setPrevious(temp.getPrevious());
                if(head == temp){
                    head = temp.getNext();
                }
            }
            if(temp.getNext() == null && temp.getPrevious() == null){
                head = null;
                tail = null;
            }
            amOfTasks--;
            return true;
        }
        return false;
    }
    
    public int size(){
        return amOfTasks;
    } 

    public Task getTask(int index){
        if(index + 1 <= amOfTasks && index >= 0){
            Node temp = head;
            for(int i = 0; i < index; i++){
                temp = temp.getNext();
            }
            return temp.getTask();
        }
        else{
            return null;
        }
    }

    public Iterator<Task> iterator(){
        return new LinkedTaskListIterator();
    }

    private class LinkedTaskListIterator implements Iterator<Task>{
        private boolean wasMoved;
        private int indexCurrentElement = -1;
        private Node temp = new Node();

        {
            temp.setNext(head);
        }

        /*public LinkedTaskListIter() {
            temp.setNext(head);
        }*/

        public Task next() {
            temp = temp.getNext();
            wasMoved = true;
            indexCurrentElement++;
            if(temp == null){
                throw new NoSuchElementException();
            }else{
                return temp.getTask();
            }
        }

        public boolean hasNext(){
            return temp.getNext() != null;
        }

        public void remove() {
            if(!wasMoved){
                throw new IllegalStateException();
            }else{
                if(temp.getPrevious() == null){
                    head = temp.getNext();
                    temp.getNext().setPrevious(null);
                }else if(temp.getNext() == null){
                    tail = temp.getPrevious();
                    temp.getPrevious().setNext(null);
                }else{
                    temp.getPrevious().setNext(temp.getNext());
                    temp.getNext().setPrevious(temp.getPrevious());
                }
                wasMoved = false;
                amOfTasks--;
            }
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedTaskList that = (LinkedTaskList) o;
        if (amOfTasks != that.amOfTasks) return false;
        Iterator<Task> thisIterator = iterator();
        Iterator<Task> thatIterator = that.iterator();
        while(thisIterator.hasNext()){
            if(!thisIterator.next().equals(thatIterator.next())){
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, tail, amOfTasks);
    }

    @Override
    public LinkedTaskList clone() throws CloneNotSupportedException {
        LinkedTaskList clone = (LinkedTaskList) super.clone();
        Iterator<Task> iterator = iterator();
        clone.head = clone.tail = null;
        while(iterator.hasNext()){
            clone.add(iterator.next().clone());
        }
        return clone;
    }

/*    
    public LinkedTaskList incoming(int from, int to){
    
        if(from < 0){
        
            throw new IllegalArgumentException("The execution start can not be less than 0");
        
        }
        if(to < 0){
        
            throw new IllegalArgumentException("The execution finish can not be less than 0");
        
        }
        if(to <= from){
        
            throw new IllegalArgumentException("The execution finish can not be less or equal to execution start");
        
        }

        LinkedTaskList subset = new LinkedTaskList();
                   
            for(int i = 0; i < size(); i++) {
            
                if(getTask(i).nextTimeAfter(from) > from &&  getTask(i).nextTimeAfter(from) <= to) {
                    
                    if(getTask(i).isActive()){
                    
                        subset.add(getTask(i));
                    
                    }
                    
                }
                
            }
            
        return subset;
        
    }
 */   
 


    /*@Override
    public int hashCode() {
        int result = 1937;
        Iterator<Task> iterator = iterator();
        int temp = 1;
        while(iterator.hasNext()){
            result += iterator.next().hashCode() % (temp + 1) + iterator().next().hashCode() % (amOfTasks - temp + 2);
            temp++;
        }
        return result;
    }*/

    /*@Override
    public String toString() {
        Iterator<Task> iterator = iterator();
        String string = "LinkedTaskList[" + amOfTasks + "]:{";
        while(iterator.hasNext()){
            string += "[" + iterator.next().toString() + "] ";
        }
        string += "}";

        return string;
    }*/

    /*@Override
    public String toString() {
        Iterator<Task> iterator = iterator();
        StringBuilder string = new StringBuilder().append("LinkedTaskList[").append(amOfTasks).append("]:{");
        while(iterator.hasNext()){
            string.append("[").append(iterator.next().toString()).append("] ");
        }
        string.append("}");

        return string.toString();
    }*/


}

