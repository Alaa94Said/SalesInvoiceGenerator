/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sales.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DELL
 */
public class InvoiceHeaderTableModel extends AbstractTableModel {
    
    private ArrayList<InvoiceHeader> data;
    private String[] cols = {"Id", "Invoice Date","Customer Name"};

    public InvoiceHeaderTableModel(ArrayList<InvoiceHeader> data) {
        this.data = data;
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader header = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return header.getId();
            case 1:
                return header.getDate();
            case 2:
                return header.getCustomerName();
        }
        return "";
    }
     /*@Override
    public Object setValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader headerSet = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return headerSet.setId();
            case 1:
                return headerSet.getDate();
            case 2:
                return headerSet.getCustomerName();
        }
        return "";
    }*/

    @Override
    public String getColumnName(int column) {
        return cols[column];
    }
    
    
    
}
