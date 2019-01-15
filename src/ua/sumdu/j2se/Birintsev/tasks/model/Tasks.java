package ua.sumdu.j2se.Birintsev.tasks.model;

import java.util.*;

public class Tasks{

    /**
     * @param tasks Must not be null. This is the source for the sublist
     * @param start Must not be null. Must be earlier than end
     * @param end Must not be null. Must be later than start
     * @return all the active tasks from the tasks which will happen at least once
     *         from start to end
     * */
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date start, Date end){
        if (start == null) {
            System.err.println("Tasks.incoming: From time must not be null");
            throw new IllegalArgumentException("From time must not be null"); // logger
        }
        if (end == null) {
            System.err.println("Tasks.incoming: To time must not be null");
            throw new IllegalArgumentException("To time must not be null"); // logger
        }
        if (start.after(end)) {
            System.err.println("Tasks.incoming: Start time can not be less than end time");
            throw new IllegalArgumentException("Start time can not be less than end time"); // logger
        }
        if (tasks == null) {
            System.err.println("Tasks.incoming: Tasks must not be null");
            throw new IllegalArgumentException("Tasks must not be null"); // logger
        }
        System.out.println("Tasks.incoming: creating the subset starts");
        List <Task> subset = new ArrayList<>();
        Iterator<Task> iter = tasks.iterator();
        while(iter.hasNext()) {
            Task temp = iter.next();
            Date nextTime = temp.nextTimeAfter(start);
            if(nextTime == null) {
                continue;
            }
            if(nextTime.after(start) && nextTime.compareTo(end) <= 0){
                subset.add(temp);
            }
        }
        System.out.println("Tasks.incoming: creating the subset finished");
        return subset;
    }

    /**
     * @param tasks Must not be null. This is the source for the map
     * @param start Must not be null. Must be earlier than end
     * @param end Must not be null. Must be later than start
     * @return all the happenings of the tasks elements, sorted by their time
     * */
    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end){
        if (start == null) {
            System.err.println("Tasks.incoming: Start time must not be null");
            throw new IllegalArgumentException("Start time must not be null");
        }
        if (end == null) {
            System.err.println("Tasks.incoming: End time must not be null");
            throw new IllegalArgumentException("End time must not be null");
        }
        if (start.after(end)) {
            System.err.println("Tasks.incoming: Start time can not be less than end time");
            throw new IllegalArgumentException("Start time can not be less than end time");
        }
        if (tasks == null) {
            System.err.println("Tasks.incoming: Tasks must not be null");
            throw new IllegalArgumentException("Tasks must not be null");
        }
        System.out.println("Tasks.calendar: creating the calendar starts");
        SortedMap<Date, Set<Task>> result = new TreeMap<>();
        Iterable<Task> subset = incoming(tasks,start,end);
        for (Task temp : subset) {
            Date happening = temp.nextTimeAfter(start);
            while (happening != null && !happening.after(end)) {
                if (happening.after(start) && !happening.after(end)) {
                    if (result.containsKey(happening)) {
                        result.get(happening).add(temp);
                    } else {
                        result.put(happening, new HashSet<>());
                        result.get(happening).add(temp);
                    }
                }
                happening = temp.nextTimeAfter(happening);
            }
        }
        System.out.println("Tasks.calendar: creating the calendar finished");
        return result;
    }
}