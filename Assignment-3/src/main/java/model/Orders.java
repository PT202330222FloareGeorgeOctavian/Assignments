package model;

public class Orders {
    private int orders_id;
    private int client_id;
    private int product_id;
    private int total = 0;
    private int amount = 0;

    public Orders(int client_id, int product_id){
        this.client_id = client_id;
        this.product_id = product_id;
    }

    public Orders(){

    }

    public int getOrders_id() {
        return orders_id;
    }

    public void setOrders_id(int order_id) {
        this.orders_id = order_id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
