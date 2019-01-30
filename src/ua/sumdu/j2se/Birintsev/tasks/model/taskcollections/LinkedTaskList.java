package ua.sumdu.j2se.Birintsev.tasks.model.taskcollections;

import ua.sumdu.j2se.Birintsev.tasks.model.Task;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class LinkedTaskList extends TaskList {

    /**
     * The first element of the LinkedTaskList
     * */
    private Node head;

    /**
     * The last element of the LinkedTaskList
     * */
    private Node tail;

    /**
     * The real number of the tasks are contained in the ArrayTaskList
     * */
    private int amOfTasks;

    public LinkedTaskList() {
    }

    public void add(Task task) {
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

    public boolean remove(Task task) {
        if(task == null){
            throw new IllegalArgumentException("Try to remove an empty task");
        }
        Node temp = head;
        while(temp != null && !temp.getTask().equals(task)) {
            temp = temp.getNext();
        }
        if(temp != null && temp.getTask().equals(task)){
            if(temp.getPrevious() != null) {
                temp.getPrevious().setNext(temp.getNext());
                if(tail == temp){
                    tail = temp.getPrevious();
                }
            }
            if(temp.getNext() != null) {
                temp.getNext().setPrevious(temp.getPrevious());
                if(head == temp){
                    head = temp.getNext();
                }
            }
            if(temp.getNext() == null && temp.getPrevious() == null) {
                head = null;
                tail = null;
            }
            amOfTasks--;
            return true;
        }
        return false;
    }
    
    public int size() {
        return amOfTasks;
    } 

    public Task getTask(int index) {
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

    public Iterator<Task> iterator() {
        return new LinkedTaskListIterator();
    }

    /**
     * Iterator implementation of the LinkedTaskList
     * */
    private class LinkedTaskListIterator implements Iterator<Task>{
        private boolean wasMoved;
        private int indexCurrentElement = -1;
        private Node temp = new Node();

        {
            temp.setNext(head);
        }

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

        public boolean hasNext() {
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

    /**
     * Creates a clone of a LinkedTaskList. Both objects and their tasks are not dependent on each other's changes
     * */
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
}