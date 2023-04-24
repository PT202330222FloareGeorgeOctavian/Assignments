package model;
import java.util.concurrent.atomic.AtomicInteger;

public class Task {
    private int id;
    private int arrivalTime;
    private AtomicInteger serviceTime;

    public Task(int id, int arrivalTime, int serviceTime){
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = new AtomicInteger(serviceTime);
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public AtomicInteger getServiceTime() {
        return serviceTime;
    }

    public void decrementServiceTime(){
        this.serviceTime.decrementAndGet();
    }
}
