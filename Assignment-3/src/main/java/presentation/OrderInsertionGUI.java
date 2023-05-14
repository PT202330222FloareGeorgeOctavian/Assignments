package presentation;

import data.BillDAO;
import data.ClientDAO;
import data.OrderDAO;
import data.ProductDAO;
import model.Bill;
import model.Client;
import model.Orders;
import model.Product;
import model.validators.NumericalValidator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * GUI for creating a new {@link Orders} object and for inserting it in the database.
 * It builds two JCombobox objects for storing all the existing {@link Product} and {@link Client}.
 * It also builds a JTextField for inserting the amount of the product and a JButton for inserting computing that data.
 */
public class OrderInsertionGUI extends JFrame {
    private JButton confirm = new JButton("Confirm");
    private JComboBox<Product> products = new JComboBox<>();
    private JComboBox<Client> clients = new JComboBox<>();
    private JLabel amountLabel = new JLabel("Amount");
    private JTextField amountTF = new JTextField(15);
    private int amount = 0;
    private Product product = new Product();
    private Client client = new Client();

    public OrderInsertionGUI() {
        buildProductComboBox();
        buildClientComboBox();
        JPanel mainPanel = new JPanel();
        JPanel cbPanel = new JPanel();
        JPanel othersPanel = new JPanel();
        cbPanel.add(products);
        cbPanel.add(clients);

        othersPanel.setLayout(new BorderLayout());
        JPanel innerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        innerPanel.add(amountLabel);
        innerPanel.add(amountTF);
        othersPanel.add(innerPanel, BorderLayout.CENTER);

        JPanel confirmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        confirmPanel.add(confirm);
        othersPanel.add(confirmPanel, BorderLayout.SOUTH);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(cbPanel);
        mainPanel.add(othersPanel);
        setSize(600, 200);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        confirmListener();
    }

    /**
     * Retrieve data from GUI and check if it's valid. If the entry in the Orders table is created, it also creates a new
     * instance of the Bill record, and it inserts it in the "bill" table. `
     */
    private void confirmListener() {
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                product = (Product) products.getSelectedItem();
                client = (Client) clients.getSelectedItem();
                if (NumericalValidator.containsOnlyNumbers(amountTF.getText())) {
                    amount = Integer.parseInt(amountTF.getText());
                    int productId = product.getProduct_id();
                    int clientId = client.getClient_id();
                    ProductDAO temp = new ProductDAO();

                    if(temp.checkAmount(productId, amount)){
                        int size = Orders.class.getDeclaredFields().length;
                        String data[] = new String[size];
                        data[0] = String.valueOf(clientId);
                        data[1] = String.valueOf(productId);
                        data[2] = String.valueOf(temp.totalPrice(productId, amount));
                        data[3] = String.valueOf(amount);
                        OrderDAO dao = new OrderDAO();
                        if(dao.insertIntoDB(data)){
                            JOptionPane.showMessageDialog(null, "Order placed successfully",
                                    "Success", JOptionPane.INFORMATION_MESSAGE);
                            temp.decreaseAmount(productId, amount);
                            BillDAO billManager = new BillDAO();
                            billManager.insertIntoDB(new Bill(clientId,productId, temp.totalPrice(productId, amount)));
                            dispose();
                        }else{
                            JOptionPane.showMessageDialog(null, "Order couldn't be placed",
                                    "Failed", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Wrong amount, try again",
                            "Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    private void buildProductComboBox() {
        ArrayList<Product> productArrayList = (ArrayList<Product>) new ProductDAO().findAll();
        for (Product p : productArrayList) {
            if(p.getAmount() > 0) {
                products.addItem(p);
            }
        }
        products.setPreferredSize(new Dimension(250, 30));
    }

    private void buildClientComboBox() {
        ArrayList<Client> clientArrayList = (ArrayList<Client>) new ClientDAO().findAll();
        for (Client c : clientArrayList) {
            clients.addItem(c);
        }
        clients.setPreferredSize(new Dimension(250, 30));
    }


}
