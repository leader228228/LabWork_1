package ua.sumdu.j2se.Birintsev.tasks.model;

import ua.sumdu.j2se.Birintsev.tasks.model.taskcollections.TaskList;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import static ua.sumdu.j2se.Birintsev.tasks.Utill.*;

/**
 *  The class that operates with the binary and text input/output of the TaskList
 *  and List&lt;Task&gt;
 * */
public class TaskIO {

    public static void write(TaskList tasks, OutputStream out) throws IOException {
        DataOutputStream dataOutputStream = null;
        try{
            dataOutputStream = new DataOutputStream(out);
            dataOutputStream.writeInt(tasks.size());
            for(Task task : tasks){
                dataOutputStream.writeInt(task.getTitle().length());
                for(int i = 0; i < task.getTitle().length(); i++){
                    dataOutputStream.writeChar(task.getTitle().charAt(i));
                }
                dataOutputStream.writeBoolean(task.isActive());
                dataOutputStream.writeInt(task.getRepeatInterval());
                if(task.isRepeated()){
                    dataOutputStream.writeLong(task.getStartTime().getTime());
                    dataOutputStream.writeLong(task.getEndTime().getTime());
                }else{
                    dataOutputStream.writeLong(task.getTime().getTime());
                }
            }
        }finally {
            if(dataOutputStream != null){
                dataOutputStream.close();
            }
        }
    }

    public static void read(TaskList tasks, InputStream in) throws IOException {
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(in);
            int amOfTasks = dataInputStream.readInt();
            for (int i = 0; i < amOfTasks; i++) {
                int detailsLength = dataInputStream.readInt();
                StringBuilder taskDetails = new StringBuilder();
                for (int j = 0; j < detailsLength; j++) {
                    taskDetails.append(dataInputStream.readChar());
                }
                boolean isActive = dataInputStream.readBoolean();
                int interval = dataInputStream.readInt();
                if (interval != 0) {
                    long startTime = dataInputStream.readLong();
                    long endTime = dataInputStream.readLong();
                    Task task = new Task(taskDetails.toString(), new Date(startTime), new Date(endTime), interval);
                    task.setActive(isActive);
                    tasks.add(task);
                } else {
                    long time = dataInputStream.readLong();
                    Task task = new Task(taskDetails.toString(), new Date(time));
                    task.setActive(isActive);
                    tasks.add(task);
                }
            }
        } finally {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            in.close();
        }
    }

    public static void writeBinary(TaskList tasks, File file) throws IOException{
        BufferedOutputStream bufferedOutputStream = null;
        try{
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            write(tasks,bufferedOutputStream);
        }finally {
            if(bufferedOutputStream != null){
                bufferedOutputStream.close();
            }
        }
    }

    public static void readBinary(TaskList tasks, File file) throws IOException{
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(file);
            read(tasks,fileInputStream);
        }finally {
            if(fileInputStream != null){
                fileInputStream.close();
            }
        }
    }

    public static void write(TaskList tasks, Writer out){
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(out);
            printWriter.write(tasks.toString());
        }finally {
            if(printWriter != null){
                printWriter.close();
            }
        }
    }

    public static void read(TaskList tasks, Reader in) throws IOException, ParseException{
        BufferedReader bufferedReader = null;
        try{
            bufferedReader = new BufferedReader(in);
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormate);
            Task task;
            String stringToParse = bufferedReader.readLine();
            while (stringToParse != null){
                Matcher matcher = patternNotRepetative.matcher(stringToParse);
                if (matcher.find()){
                    String details = matcher.group(1).replace("\"\"", "\"");
                    Date time = dateFormat.parse(matcher.group(2));
                    boolean isActive = matcher.group(3).length() == 0;
                    task = new Task(details,time);
                    task.setActive(isActive);
                    tasks.add(task);
                }else{
                    matcher = patternRepetative.matcher(stringToParse);
                    if(matcher.find()) {
                        String details = matcher.group(1).replace("\"\"", "\"");
                        Date start = dateFormat.parse(matcher.group(2));
                        Date end = dateFormat.parse(matcher.group(3));
                        int interval = parseInterval(matcher.group(4));
                        // if seconds == 0 -> logger warning???
                        boolean isActive = matcher.group(5).length() == 0;
                        task = new Task(details, start, end, interval);
                        task.setActive(isActive);
                        tasks.add(task);
                    } else if(stringToParse.length() == 0){
                        return; // logger : the file is empty
                    } else {
                        throw new IOException(new StringBuilder("Wrong stringToParse value : ").append(stringToParse).toString());
                    }
                }
                stringToParse = bufferedReader.readLine();
            }
        }finally {
            if(bufferedReader != null){
                bufferedReader.close();
            }
        }
    }

    public static void writeText(TaskList tasks, File file) throws IOException{
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            write(tasks, bufferedWriter);
        } finally {
            bufferedWriter.close();
        }
    }

    public static void readText(TaskList tasks, File file) throws IOException{
        BufferedReader bufferedReader =  null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            read(tasks, bufferedReader);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IOException(e);
        }finally {
            if(bufferedReader != null){
                bufferedReader.close();
            }

        }
    }

    public static void write(List<Task> tasks, OutputStream out) throws IOException {
        DataOutputStream dataOutputStream = null;
        try{
            dataOutputStream = new DataOutputStream(out);
            dataOutputStream.writeInt(tasks.size());
            for(Task task : tasks){
                dataOutputStream.writeInt(task.getTitle().length());
                for(int i = 0; i < task.getTitle().length(); i++){
                    dataOutputStream.writeChar(task.getTitle().charAt(i));
                }
                dataOutputStream.writeBoolean(task.isActive());
                dataOutputStream.writeInt(task.getRepeatInterval());
                if(task.isRepeated()){
                    dataOutputStream.writeLong(task.getStartTime().getTime());
                    dataOutputStream.writeLong(task.getEndTime().getTime());
                }else{
                    dataOutputStream.writeLong(task.getTime().getTime());
                }
            }
        }finally {
            if(dataOutputStream != null){
                dataOutputStream.close();
            }
        }
    }

    public static void read(List <Task> tasks, InputStream in) throws IOException {
        DataInputStream dataInputStream = null;
        try {
            dataInputStream = new DataInputStream(in);
            int amOfTasks = dataInputStream.readInt();
            for (int i = 0; i < amOfTasks; i++) {
                int detailsLength = dataInputStream.readInt();
                StringBuilder taskDetails = new StringBuilder();
                for (int j = 0; j < detailsLength; j++) {
                    taskDetails.append(dataInputStream.readChar());
                }
                boolean isActive = dataInputStream.readBoolean();
                int interval = dataInputStream.readInt();
                if (interval != 0) {
                    long startTime = dataInputStream.readLong();
                    long endTime = dataInputStream.readLong();
                    Task task = new Task(taskDetails.toString(), new Date(startTime), new Date(endTime), interval);
                    task.setActive(isActive);
                    tasks.add(task);
                } else {
                    long time = dataInputStream.readLong();
                    Task task = new Task(taskDetails.toString(), new Date(time));
                    task.setActive(isActive);
                    tasks.add(task);
                }
            }
        } finally {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            in.close();
        }
    }

    public static void writeBinary(List <Task> tasks, File file) throws IOException{
        BufferedOutputStream bufferedOutputStream = null;
        try{
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            write(tasks,bufferedOutputStream);
        }finally {
            if(bufferedOutputStream != null){
                bufferedOutputStream.close();
            }
        }
    }

    public static void readBinary(List <Task> tasks, File file) throws IOException{
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(file);
            read(tasks,fileInputStream);
        }finally {
            if(fileInputStream != null){
                fileInputStream.close();
            }
        }
    }

    public static void write(List <Task> tasks, Writer out) {
        Iterator<Task> iter = tasks.iterator();
        StringBuilder taskListInfo = new StringBuilder();
        while (iter.hasNext()) {
            taskListInfo.append(iter.next().toString());
            if(iter.hasNext()){
                taskListInfo.append(";\n");
            }else{
                taskListInfo.append('.');
            }
        }
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(out);
            printWriter.write(taskListInfo.toString());
        }finally {
            if(printWriter != null){
                printWriter.close();
            }
        }
    }

    public static void read(List <Task> tasks, Reader in) throws IOException, ParseException{
        BufferedReader bufferedReader = null;
        try{
            bufferedReader = new BufferedReader(in);
            SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormate);
            Task task;
            String stringToParse = bufferedReader.readLine();
            if(stringToParse == null || stringToParse.charAt(0) == (char)65279){ // logger ? null-string(empty file) || ZERO WIDTH NO-BREAK SPACE character (new file)
                return;
            }
            while (stringToParse != null){
                Matcher matcher = patternNotRepetative.matcher(stringToParse);
                if (matcher.find()){
                    String details = matcher.group(1).replace("\"\"", "\"");
                    Date time = dateFormat.parse(matcher.group(2));
                    boolean isActive = matcher.group(3).length() == 0;
                    task = new Task(details,time);
                    task.setActive(isActive);
                    tasks.add(task);
                }else{
                    matcher = patternRepetative.matcher(stringToParse);
                    if(matcher.find()) {
                        String details = matcher.group(1).replace("\"\"", "\"");
                        Date start = dateFormat.parse(matcher.group(2));
                        Date end = dateFormat.parse(matcher.group(3));
                        int interval = parseInterval(matcher.group(4));
                        // if seconds == 0 -> logger warning???
                        boolean isActive = matcher.group(5).length() == 0;
                        task = new Task(details, start, end, interval);
                        task.setActive(isActive);
                        tasks.add(task);
                    } else {
                        throw new IOException(new StringBuilder("Wrong stringToParse value :").append(stringToParse).toString()); // logger
                    }
                }
                stringToParse = bufferedReader.readLine();
            }
        }finally {
            if(bufferedReader != null){
                bufferedReader.close();
            }
        }
    }

    public static void writeText(List <Task> tasks, File file) throws IOException{
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file));
            write(tasks, bufferedWriter);
        } finally {
            bufferedWriter.close();
        }
    }

    public static void readText(List <Task> tasks, File file) throws IOException{
        BufferedReader bufferedReader =  null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            read(tasks, bufferedReader);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IOException(e);
        }finally {
            if(bufferedReader != null){
                bufferedReader.close();
            }
        }
    }
}