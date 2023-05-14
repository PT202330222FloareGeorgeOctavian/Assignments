package model;

public class Product {
    private int product_id;
    private String product_name;
    private int price;
    private int amount;

    public Product(){

    }

    public Product(int id, String name, int price, int amount){
        this.product_id = id;
        this.product_name = name;
        this.price = price;
        this.amount = amount;
    }

    public Product(String name, int price, int amount){
        this.product_name = name;
        this.price = price;
        this.amount = amount;
    }


    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return
                  product_name  +
                ", price=" + price +
                ", amount=" + amount ;
    }
}
