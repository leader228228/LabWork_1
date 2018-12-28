package ua.sumdu.j2se.Birintsev.tasks.Model;

class Node implements Cloneable{

    private Task task;
    private Node previous;
    private Node next;

    public Node(){

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
    
    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(this == null || o.getClass() != getClass()){
            return false;
        }
        Node node = (Node) o;        
        if(task.equals(node.task)) return true;            
        else return false;
    }

    /*@Override
    public int hashCode() {
        return Objects.hash(task, previous, next);
    }*/

    @Override
    public int hashCode(){
        /*int coef1 = (previous == null && next == null ?
            (task.hashCode() * 3 / 7) : (333 + task.hashCode()));
        int coef2 = (previous != null && next == null ?
            (task.hashCode() * 43 / (7 * previous.getTask().hashCode())) : (previous.getTask().hashCode() - 5 * task.hashCode() + 1));
        int coef3 = (previous == null && next != null ?
            (next.getTask().hashCode() * coef1 / (coef2 * task.hashCode())) : (previous.getTask().hashCode() - coef1 * next.getTask().hashCode() + coef2));
        int coef4 = (previous != null && next != null ?
        (coef1 * task.hashCode() + coef2 * next.getTask().hashCode() + coef3 / previous.getTask().hashCode()) : (coef1 * coef3 + coef2 * coef1 - coef1 - coef2 + coef3));*/
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
     * Returns a node which has a cloned task, but the previous and the next elements stay the same as
     * it's prototype has
     * */
    @Override
    public Node clone() throws CloneNotSupportedException {
        Node clone = (Node)super.clone();
        clone.task = task.clone();
        return clone;
    }
}