package business;

import model.Server;
import model.Task;

import java.util.LinkedList;
import java.util.List;


public class Scheduler {
    private int maxNoServers;
    private List<Server> servers;
    private int maxTaskPerServer;
    private Strategy strategy;


    public Scheduler(int maxNoServers, int maxTasksPerServers){
        this.maxTaskPerServer = maxTasksPerServers;
        this.maxNoServers = maxNoServers;
        servers = new LinkedList<>();
        strategy = new Strategy();
       for(int i=0;i<maxNoServers;i++){
           Server s = new Server();
           servers.add(s);
       }

       for(Server s: servers){
           Thread serverSideThread = new Thread(s);
           serverSideThread.start();
           if(!s.isRunning()){
               serverSideThread.interrupt();
           }
       }
    }

    public void dispatchTask(Task t) {
        strategy.addTask(servers, t);
    }

    public List<Server> getServers() {
        return servers;
    }
}
