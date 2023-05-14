package presentation;

import data.*;
import model.Orders;
import model.Product;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * GUI generic class used for representing visually all the data and all the buttons for calling
 * CRUD operations on the database
 * @param <T> the type T used for calling {@link AbstractDAO} methods
 */
public class GenericGUI<T> extends JFrame {
    private JTable table;
    private final Class<T> type;
    private AbstractDAO dao;
    private JButton delete = new JButton("Delete");
    private JButton edit = new JButton("Edit");
    private JButton create = new JButton("Create");
    private JButton find = new JButton("Find");
    private int clickedRow = 0;
    private int clickedColumn = 0;
    private String[] header;
    private String[][] row;

    ArrayList<String> givenFields = new ArrayList<>();
    ArrayList<String> values = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public GenericGUI(Class<T> type) {
        this.type = type;
        switch (type.getSimpleName()) {
            case "Product" -> {
                dao = new ProductDAO();
                this.setTitle("Products");
            }
            case "Client" -> {
                dao = new ClientDAO();
                this.setTitle("Clients");
            }
            case "Orders" -> {
                dao = new OrderDAO();
                this.setTitle("Orders");
            }
            default -> {
            }
        }
        JPanel panel = new JPanel();
        JPanel mainPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        deleteListener();
        findListener();
        createListener();
        editListener();
        buttonPanel.add(create);
        buttonPanel.add(find);
        if(type!=Orders.class) {
            buttonPanel.add(edit);
            buttonPanel.add(delete);
        }
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);
        header = (String[]) dao.getTableHeader().toArray(new String[0]);
        row = convertListToMatrix(dao.findAll());
        table = new JTable(row, header);
        table.getTableHeader().setDefaultRenderer(new HeaderRenderer());
        table.setDefaultEditor(Object.class, new DefaultCellEditor(new JTextField()));
        panel.add(new JScrollPane(table));
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panel);
        mainPanel.add(buttonPanel);
        setContentPane(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        mouseEditor();
        tableUpdate();
    }

    /**
     * Mouse listener used for selecting a cell inside the JTable
     */
    private void mouseEditor() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clickedRow = table.rowAtPoint(e.getPoint());
                clickedColumn = table.columnAtPoint(e.getPoint());
                table.setRowSelectionInterval(clickedRow, clickedRow);
                table.setColumnSelectionInterval(clickedColumn, clickedColumn);
            }
        });
    }

    /**
     * Table listener used for changing the values inside the JTable if it has been recorded a change
     */
    private void tableUpdate() {
        table.getModel().addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE) {
                givenFields.add(header[clickedColumn]);
                values.add(table.getValueAt(clickedRow, clickedColumn).toString());
            }
        });

    }

    private void deleteListener() {
        this.delete.addActionListener(e -> {
            DataRemove_FindGUI remove = new DataRemove_FindGUI("delete");
            int id = remove.getData();
            if (id != -1) {
                if (dao.deleteFromDB(dao.findById(id))) {
                    JOptionPane.showMessageDialog(null, "The entry has been removed",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new GenericGUI<>(type);
                } else {
                    JOptionPane.showMessageDialog(null, "The entry has not been found, try again",
                            "Failed", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
    }

    /**
     * The edit listener updates the entry with the values that have been changed in the table.
     *  Multiple changed can be made but only for one entry at a time.
     */
    private void editListener() {
        this.edit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            int selectedColumn = table.getSelectedColumn();
            if (selectedRow >= 0) {
                table.editCellAt(clickedRow, clickedColumn);
                table.requestFocusInWindow();
                int id = Integer.parseInt(table.getValueAt(clickedRow, 0).toString());
                String[] auxFields = new String[givenFields.size()];
                String[] auxValues = new String[values.size()];
                if (dao.update(id, givenFields.toArray(auxFields), values.toArray(auxValues))) {
                    JOptionPane.showMessageDialog(null, "The entry has been updated successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new GenericGUI(type);
                } else {
                    JOptionPane.showMessageDialog(null, "The entry couldn't be updated, try again",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a cell to edit");
            }
        });
    }

    private void createListener() {
        this.create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!type.getSimpleName().equals("Orders")) {
                    DataInsertionGUI<Product> gui = new DataInsertionGUI<>(dao.getType());
                    gui.getConfirm().addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            String[] data = gui.getData();
                            if (dao.insertIntoDB(data)) {
                                JOptionPane.showMessageDialog(null, "The entry has been created with success",
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
                                gui.dispose();
                                dispose();
                                new GenericGUI(type);
                            } else {
                                JOptionPane.showMessageDialog(null, "The entry couldn't be created, try again",
                                        "Failed", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    });
                }
                else{
                    OrderInsertionGUI gui = new OrderInsertionGUI();
                }
            }
        });
    }

    private void findListener() {
        this.find.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DataRemove_FindGUI find = new DataRemove_FindGUI("find");
                int id = find.getData();
                if (id != -1) {
                    Constructor[] ctors = type.getDeclaredConstructors();
                    Constructor ctor = null;
                    for (Constructor constructor : ctors) {
                        ctor = constructor;
                        if (ctor.getGenericParameterTypes().length == 0)
                            break;
                    }
                    Object instance;
                    instance = dao.findById(id);
                    if (instance != null) {
                        JOptionPane.showMessageDialog(null, instance.toString(),
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "The entry has not been found, try again",
                                "Failed", JOptionPane.INFORMATION_MESSAGE);
                    }

                }
            }
        });
    }

    /**
     *  Retrieve all the values of the fields of an instance
     * @param instance the instance from which the data is extracted
     * @return the list of values inside the instance
     */
    private String[] getDataFromType(T instance) {
        String[] data = new String[type.getDeclaredFields().length];
        Field[] fields = instance.getClass().getDeclaredFields();
        PropertyDescriptor pd;
        try {
            for (int i = 0; i < fields.length; i++) {
                pd = new PropertyDescriptor(fields[i].getName(), instance.getClass());
                data[i] = String.valueOf(pd.getReadMethod().invoke(instance));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * Convert the result of the "findAll" method to a matrix of Strings
     * which can be displayed inside a JTable
     * @param list the resulted list of "findAll" method
     * @return a matrix of Strings
     */
    private String[][] convertListToMatrix(List<T> list) {
        if (list != null) {
            String[][] rowData = new String[list.size()][];

            for (int i = 0; i < list.size(); i++) {
                rowData[i] = getDataFromType(list.get(i));
            }
            return rowData;
        } else {
            String[][] rowData = new String[1][];
            int noOfFields = type.getDeclaredFields().length;
            rowData[0] = new String[noOfFields];
            for (int i = 0; i < noOfFields; i++) {
                rowData[0][i] = " ";
            }
            return rowData;
        }
    }
}
