package model;

public class Client {
    private int client_id;
    private String name;
    private String email;

    public Client(int id, String email, String name) {
        this.email = email;
        this.name = name;
        this.client_id = id;
    }

    public Client( String email, String name) {
        this.email = email;
        this.name = name;
    }

    public Client() {

    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return name + ", email=" + email;
    }
}
