/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sales.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sales.model.InvoiceHeader;
import sales.model.InvoiceLine;
import sales.view.SalesFrame;

/**
 *
 * @author Dahab
 */
public class Controller1 implements ActionListener, ListSelectionListener  {
    private SalesFrame frame;
    private String selecteditem;
    private InvoiceLine item;
    private ArrayList<InvoiceLine> invoiceItemsList;
    
    @Override
    public void actionPerformed(ActionEvent e) {
      }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println(" item Selected");
        this.selecteditem = frame.getInvLineTable().getSelectedRow()+""; 
        System.out.println(selecteditem);
      
        ArrayList<InvoiceLine> newList=new ArrayList<InvoiceLine>();
        for(int i=0 ; i< this.invoiceItemsList.size() ; i++)
        {
            InvoiceLine invoice = this.invoiceItemsList.get(i);
                if(i != Integer.parseInt(this.selecteditem) ){
                    newList.add(invoice);}
        }
        this.invoiceItemsList = newList;
        frame.setinvoiceItemsList(this.invoiceItemsList); //birsm l table mnl awel
        
     return;
    }
}
