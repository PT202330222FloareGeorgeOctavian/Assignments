package presentation;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class HeaderRenderer extends JLabel implements TableCellRenderer {
    public HeaderRenderer(){
        setFont(new Font("Consolas", Font.BOLD, 14));
        setBorder(BorderFactory.createEtchedBorder());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText(value.toString());
        return this;
    }
}
