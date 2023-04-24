package business;

import model.Server;
import model.Task;
import view.SimulationFrame;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {
    public static int currentTime = 0;
    private final int maxProcessingTime;
    private final int minProcessingTime;
    private final int maxArrivalTime;
    private final int minArrivalTime;
    private final int timeLimit;
    private final int noOfServers;
    private final int noOfClients;
    private Scheduler scheduler;
    private List<Task> generated;
    private double averageWaitingTime = 0.0;
    private double averageServiceTime = 0.0;
    private int peakHour = 0;
    private SimulationFrame gui;
    private boolean error = false;

    public SimulationManager(SimulationFrame gui) {
        this.gui = gui;
        ArrayList<Integer> values = gui.retrieveData();

        this.noOfServers = values.get(0);
        this.noOfClients = values.get(1);
        this.timeLimit = values.get(2);
        this.minArrivalTime = values.get(3);
        this.maxArrivalTime = values.get(4);
        this.minProcessingTime = values.get(5);
        this.maxProcessingTime = values.get(6);

        generated = new LinkedList<>();
        generateRandomTasks();
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Simulation Time: ").append(currentTime).append("\n");
        sb.append("Waiting clients: \n");
        for (Task t : generated) {
            if (currentTime < t.getArrivalTime())
                sb.append("( ").append(t.getId()).append(" ").append(t.getArrivalTime()).append(" ").append(t.getServiceTime()).append(" ) ");
        }

        sb.append("\n");
        for (int i = 0; i < noOfServers; i++) {
            sb.append("Server ").append(i + 1).append(" -> ");
            if (scheduler.getServers().get(i).getServer().isEmpty() && scheduler.getServers().get(i).getCurrentTask() == null) {
                sb.append("\tempty\t\n");
            } else {
                if (scheduler.getServers().get(i).getCurrentTask() != null) {
                    sb.append("(")
                            .append(scheduler.getServers().get(i).getCurrentTask().getId()).append(" ")
                            .append(scheduler.getServers().get(i).getCurrentTask().getArrivalTime()).append(" ")
                            .append(scheduler.getServers().get(i).getCurrentTask().getServiceTime())
                            .append(") ");
                }
                for (Task t : scheduler.getServers().get(i).getServer()) {
                    sb.append("(")
                            .append(t.getId()).append(" ").append(t.getArrivalTime()).append(" ")
                            .append(t.getServiceTime()).append(") ");
                }
                sb.append("\n");
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    public void generateRandomTasks() {
        Random r = new Random();
        for (int i = 0; i < noOfClients; i++) {
            generated.add(new Task(i, r.nextInt(minArrivalTime, maxArrivalTime), r.nextInt(minProcessingTime, maxProcessingTime)));
        }
    }

    public boolean checkGeneratedClients(){
        int size = 0;
        for(Task t : generated){
            if(t.getServiceTime().get() <= 0){
                size ++ ;
            }
        }
        if(size == generated.size()){
            return false;
        }
        return true;
    }

    public double computeAverageServiceTime(){
        for(Task t: generated){
            this.averageServiceTime+=t.getServiceTime().get();
        }
        return averageServiceTime;
    }

    @Override
    public void run() {
        if (!error) {
            scheduler = new Scheduler(this.noOfServers, this.noOfClients);
            currentTime = 0;
            int maxWaitingTime = 0;
            int iteration = 0;
            averageServiceTime = computeAverageServiceTime();
            LogHandler lh = new LogHandler(new File("src/main/java/log.txt"));
            lh.overWrite();
            while (currentTime < timeLimit) {
                if(!checkGeneratedClients()){
                   break;
                }
                for (Task t : generated) {
                    if (currentTime == t.getArrivalTime()) {
                        scheduler.dispatchTask(t);
                    }
                }
                lh.insertLogs(toString());
                for (Server s : scheduler.getServers()) {
                    if (s.getWaitingTime().get() != 0) {
                        averageWaitingTime += s.getWaitingTime().get();
                        iteration++;
                    }
                    if (s.getWaitingTime().get() > maxWaitingTime) {
                        maxWaitingTime = s.getWaitingTime().get();
                        peakHour = currentTime;
                    }
                    if (s.getCurrentTask() != null) {
                        s.getCurrentTask().decrementServiceTime();
                        if (s.getCurrentTask().getServiceTime().get() == 0) {
                            s.setTaskNull();
                        }
                    }
                }
                currentTime++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Average Waiting Time: ").append(averageWaitingTime / iteration).append("\n").append("Average Service Time: ").append(averageServiceTime/ noOfClients).append("\n").append("Peak Hour: ").append(peakHour);
            lh.insertLogs(String.valueOf(sb));
            lh.close();
            List<Server> serverList = scheduler.getServers();
            for (Server s : serverList) {
                s.stop();
            }
            gui.endSimulation();
        }
    }
}


