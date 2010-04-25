package akerigan.fb2.gui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 18.04.2010
 * Time: 16:58:51
 *
 * @author Vlad Vinichenko (akerigan@gmail.com)
 */
public class BookInfoTableModel implements TableModel {

    private List<BookInfoEntry> entries = new LinkedList<BookInfoEntry>();
    protected EventListenerList listenerList = new EventListenerList();
    private Log log = LogFactory.getLog(getClass());

    public int getRowCount() {
        return entries.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Name";
            case 1:
                return "Value";
            default:
                return "";
        }
    }

    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < entries.size()) {
            BookInfoEntry entry = entries.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return entry.getName();
                case 1:
                    return entry.getValue();
            }
        }
        return "";
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        BookInfoEntry entry;
        if (rowIndex >= 0 && rowIndex < entries.size()) {
            entry = entries.get(rowIndex);
        } else {
            entry = new BookInfoEntry();
            entries.add(entry);
        }
        switch (columnIndex) {
            case 0:
                entry.setName(value.toString());
                break;
            case 1:
                entry.setValue(value.toString());
                break;
        }
    }

    public void addTableModelListener(TableModelListener l) {
        listenerList.add(TableModelListener.class, l);
    }

    public void removeTableModelListener(TableModelListener l) {
        listenerList.remove(TableModelListener.class, l);
    }

    public void addEntry(String name, String value) {
        BookInfoEntry entry = new BookInfoEntry();
        entry.setName(name);
        entry.setValue(value);
        entries.add(entry);
        log.info("Entry added: name=" + name + ",value=" + value);
    }

    public void fireTableRowsInserted(int firstRow, int lastRow) {
        fireTableChanged(new TableModelEvent(this, firstRow, lastRow,
                             TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT));
    }

    public void fireTableChanged(TableModelEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TableModelListener.class) {
                ((TableModelListener) listeners[i + 1]).tableChanged(e);
            }
        }
    }
}
