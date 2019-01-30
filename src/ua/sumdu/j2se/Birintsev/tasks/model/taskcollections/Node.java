package ua.sumdu.j2se.Birintsev.tasks.model.taskcollections;

import ua.sumdu.j2se.Birintsev.tasks.model.Task;

/**
 * This is the class-wrapper for the Task that the LinkedTaskList contains
 * @see LinkedTaskList
 * @see Task
 * */
class Node implements Cloneable {

    private Task task;
    private Node previous;
    private Node next;

    public Node() {
    }

    public Node(Task task){
        this.task = task;
    }

    public Task getTask(){
        return task;
    }

    public Node getNext(){
        return next;
    }
    
    public Node getPrevious(){
        return previous;
    }
    
    public void setNext(Node node){
        next = node;
    }
    
    public void setPrevious(Node node){
        previous = node;
    }

    /**
     * Two nodes are equal if their fields task are equal
     * */
    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || o.getClass() != getClass()){
            return false;
        }
        Node node = (Node) o;
        return task.equals(node.task);
    }

    @Override
    public int hashCode(){
        int coef1 = (next == null ? 31 : 53);
        int coef2 =  (previous == null ? 7 : 11);
        int coef3 = coef1 + coef2;
        return task.hashCode() * coef1 - coef2 + coef3 * coef1;
    }
    
    @Override
    public String toString(){    
        return new StringBuilder().append("\nPrevious: ").append(previous.getTask().toString()).append("\nNext: ").append(next.getTask().toString()).toString();
    }

    /**
     * Returns a node which has a cloned task,
     * but the previous and the next elements
     * stay the same as it's prototype has
     * */
    @Override
    public Node clone() throws CloneNotSupportedException {
        Node clone = (Node)super.clone();
        clone.task = task.clone();
        return clone;
    }
}