package ua.sumdu.j2se.Birintsev.tasks;

import java.util.*;

public class Tasks{
    
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end){
        if (start == null) {
            throw new IllegalArgumentException("From time must not be null"); // logger
        }
        if (end == null) {
            throw new IllegalArgumentException("To time must not be null"); // logger
        }
        if (start.after(end)) {
            throw new IllegalArgumentException("Start time can not be less than end time"); // logger
        }
        if (tasks == null) {
            throw new IllegalArgumentException("Tasks must not be null"); // logger
        }

        List <Task> subset = new ArrayList<>();
        Iterator<Task> iter = tasks.iterator();
        while(iter.hasNext()){
            Task temp = iter.next();
            Date nextTime = temp.nextTimeAfter(start);
            if(nextTime == null){
                continue;
            }
            if(nextTime.after(start) && nextTime.compareTo(end) <= 0){
                subset.add(temp);
            }
        }
        return subset;
    }
    
    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end){
        if (start == null) {
            throw new IllegalArgumentException("Start time must not be null");
        }
        if (end == null) {
            throw new IllegalArgumentException("End time must not be null");
        }
        if (start.after(end)) {
            throw new IllegalArgumentException("Start time can not be less than end time");
        }
        if (tasks == null) {
            throw new IllegalArgumentException("Tasks must not be null");
        }
        SortedMap<Date, Set<Task>> result = new TreeMap<Date, Set<Task>>();
        Iterable<Task> subset = incoming(tasks,start,end);
        for (Task temp : subset) {
            Date happening = temp.nextTimeAfter(start);
            while (happening != null && !happening.after(end)){
                if (happening.after(start) && !happening.after(end)) {
                    if (result.containsKey(happening)) {
                        result.get(happening).add(temp);
                    } else {
                        result.put(happening, new HashSet<Task>());
                        result.get(happening).add(temp);
                    }
                }
                happening = temp.nextTimeAfter(happening);
            }
        }
        return result;
    }
}