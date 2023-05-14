package presentation;

import model.Client;
import model.Orders;
import model.Product;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main GUI which has 3 buttons for each type T except {@link model.Bill}
 */
public class MainGUI extends JFrame {
    private JButton client = new JButton("Client");
    private JButton product = new JButton("Product");
    private JButton order = new JButton("Order");
    public MainGUI(){
        this.setSize(500, 100);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Control");
        JPanel panel = new JPanel();
        panel.add(client);
        panel.add(product);
        panel.add(order);
        this.setContentPane(panel);

        clientListener();
        productListener();
        orderListener();
    }

    private void clientListener(){
        this.client.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenericGUI<Client> gui = new GenericGUI<>(Client.class);
            }
        });
    }

    private void productListener(){
        this.product.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenericGUI<Product> gui = new GenericGUI<>(Product.class);
            }
        });
    }

    private void orderListener(){
        this.order.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenericGUI<Orders> gui = new GenericGUI<>(Orders.class);
            }
        });
    }

}
