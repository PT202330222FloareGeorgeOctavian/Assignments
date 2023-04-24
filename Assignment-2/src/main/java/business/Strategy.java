package business;

import model.Server;
import model.Task;

import java.util.List;

public class Strategy {
    public void addTask(List<Server> servers, Task t) {
        int minWaitingTime = Integer.MAX_VALUE;
        Server temp = servers.get(0);


        //get server with min waiting time
        for(Server server:servers){
            if(server.getWaitingTime().get() <= minWaitingTime){
                temp =server;
                minWaitingTime = server.getWaitingTime().get();
            }
        }

        //call addTask method from server
        temp.addTask(t);
    }

}
