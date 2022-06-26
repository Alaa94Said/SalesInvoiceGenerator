package sales.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import sales.model.InvoiceHeader;
import sales.model.InvoiceLine;
import sales.model.InvoiceLineTableModel;
import sales.view.SalesFrame;

public class Controller implements ActionListener, ListSelectionListener {

    private SalesFrame frame;
    private String hfilePath;
    private String ifilePath;
    private String selectedRow;
    private String selecteditem;
    private InvoiceLine item;
    private InvoiceHeader invoice;
    private ArrayList<InvoiceHeader> invoiceHeadersList;
    private ArrayList<InvoiceLine> invoiceItemsList;
  private DefaultTableModel tablemodel;
  private static final int TABLE_COUNT = 2;
    
   

    public Controller(SalesFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "Load Files":
                loadFiles();
                break;
            case "Save Files":
                saveFiles();
                break;
            case "Create New Invoice":
                createNewInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "New item":
                newItem();
                break;
            case "Cancel":
                Cancel();
                break;
            case "Save":
                Save();
                break;
            case "ok":
               addItem();
               break;
                
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println(" Row Selected");
        this.selectedRow = frame.getInvHeaderTable().getSelectedRow()+"";

        System.out.println(selectedRow);
        if(this.selectedRow.equals("") || this.selectedRow.equals("-1")
                || frame.getInvoiceHeadersList().get(Integer.parseInt(selectedRow)).getLines() == null)
            return;
        //bigebo l related invoice items
        ArrayList<InvoiceLine> lines = frame.getInvoiceHeadersList().get(Integer.parseInt(selectedRow)).getLines();
        frame.getInvLineTable().setModel(new InvoiceLineTableModel(lines));
        ///////////display value in text fields /////////
        int i=frame.getInvHeaderTable().getSelectedRow();
        TableModel model =frame.getInvHeaderTable().getModel();
        frame.getInvNumlbl().setText(model.getValueAt(i, 0).toString());
        frame.getInvDatetxt().setText(model.getValueAt(i, 1).toString());
        frame.getNametxt().setText(model.getValueAt(i, 2).toString());
        frame.getTotallbl().setText(model.getValueAt(i, 3).toString());
        ////////////////////////////
        
        ListSelectionGroup listSelectionGroup = new ListSelectionGroup();
        //setLayout(new GridLayout(1, 0));
        for (int j = 0; j < TABLE_COUNT; j++) {
            TableModel modele = frame.getInvLineTable().getModel();
            ListSelectionModel selectionModel = frame.getInvLineTable().getSelectionModel();
            listSelectionGroup.register(selectionModel);
            
        }
        
       /* System.out.println(" item Selected");
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
        
     return;*/
        
    }
  
    
    private InvoiceHeader getInvoiceHeaderById(ArrayList<InvoiceHeader> invoices, int id) {
        for (InvoiceHeader invoice : invoices) {
            if (invoice.getId() == id) {
                return invoice;
            }
        }

        return null;
    }

    private void createNewInvoice() {
        System.out.println("Create new invoice dialog");
        this.frame.showNewInvoiceDialogue();
    }
    private void deleteInvoice() {
        
       System.out.println(this.selectedRow);
        ArrayList<InvoiceHeader> newList=new ArrayList<InvoiceHeader>();
        for(int i=0 ; i< this.invoiceHeadersList.size() ; i++)
        {
            InvoiceHeader invoice = this.invoiceHeadersList.get(i);
                if(i != Integer.parseInt(this.selectedRow) ){
                    newList.add(invoice);}
        }
        this.invoiceHeadersList = newList;
        frame.setInvoiceHeadersList(this.invoiceHeadersList); //birsm l table mnl awel
        }

    private void newItem() {
        System.out.println("new item dialog");
        this.frame.showNewItemDialogue();
         }

    private void Cancel() {
    }

    private void Save() {
         invoice = new InvoiceHeader(Integer.parseInt(this.frame.getInvoiceNumber()),
                this.frame.getCustomerName(),
                 (new Date()).toString());
         this.invoiceHeadersList.add(invoice);
         frame.setInvoiceHeadersList(this.invoiceHeadersList); 
    }
   
    private void addItem() {
         item = new InvoiceLine(this.frame.getItemName(),
               Integer.parseInt(this.frame.getItemPrice()),
                 Integer.parseInt(this.frame.getCount()),
                 invoice );
         
        this.invoiceItemsList.add(item);
         frame.setinvoiceItemsList(this.invoiceItemsList); 
    }
    private void loadFiles() {
        try {
            JFileChooser fc = new JFileChooser();
            int result = fc.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                String headerStrPath = headerFile.getAbsolutePath();
                hfilePath = headerStrPath;
                Path headerPath = Paths.get(headerStrPath);
                List<String> headerLines = Files.lines(headerPath).collect(Collectors.toList());
                // ["1,22-11-2020,Ali", "2,13-10-2021,Saleh"]
                this.invoiceHeadersList = new ArrayList<>();
                for (String headerLine : headerLines) {
                    String[] parts = headerLine.split(",");
                    // parts = ["1", "22-11-2020", "Ali"]
                    // parts = ["2", "13-10-2021", "Saleh"]
                    if(parts[0].equals(""))
                        continue;
                    int id = Integer.parseInt(parts[0]);
                    InvoiceHeader invHeader = new InvoiceHeader(id, parts[2], parts[1]);
                    invoiceHeadersList.add(invHeader);
                }
                System.out.println("check");
                result = fc.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String lineStrPath = fc.getSelectedFile().getAbsolutePath();
                    ifilePath=lineStrPath;
                    Path linePath = Paths.get(lineStrPath);
                    List<String> lineLines = Files.lines(linePath).collect(Collectors.toList());
                    // ["1,Mobile,3200,1", "1,Cover,20,2", "1,Headphone,130,1", "2,Laptop,4000,1", "2,Mouse,35,1"]
                    for (String lineLine : lineLines) {
                        String[] parts = lineLine.split(",");
                        // ["1","Mobile","3200","1"]
                        // ["1","Cover","20","2"]
                        // ["1","Headphone","130","1"]
                        // ["2","Laptop","4000","1"]
                        // ["2","Mouse","35","1"]
                        if(parts.length<2)
                            break;
                          if(parts[0].equals(""))
                        break;
                          if(parts[1].equals(""))
                        break;
                          if(parts[2].equals(""))
                        break;
                          if(parts[3].equals(""))
                        break;
                        int invId = Integer.parseInt(parts[0]);
                        double price = Double.parseDouble(parts[2]);
                        int count = Integer.parseInt(parts[3]);
                        InvoiceHeader header = getInvoiceHeaderById(invoiceHeadersList, invId);
                        if(header!=null){
                        InvoiceLine invLine = new InvoiceLine(parts[1], price, count, header);
                        header.getLines().add(invLine);
                    }}
                    frame.setInvoiceHeadersList(invoiceHeadersList);

                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

 private void saveFiles() {
        FileWriter fw;
        try {
            File f = new File(hfilePath);
            if(f.exists())
            f.delete();
            
            fw = new FileWriter(this.hfilePath, true);
            BufferedWriter bwh = new BufferedWriter(fw);
            for(int i=0 ; i< this.invoiceHeadersList.size() ; i++)
        {
            InvoiceHeader invoice = this.invoiceHeadersList.get(i);
            bwh.write(invoice.toString());
            bwh.newLine();
        } bwh.close();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
       FileWriter fwi;
        try {
            fwi = new FileWriter(this.ifilePath, true);
            BufferedWriter bwi = new BufferedWriter(fwi);
          
            for(int i=0 ; i< this.invoiceHeadersList.size() ; i++)
        {
            InvoiceHeader InvWriter = this.invoiceHeadersList.get(i);
                 bwi.write(InvWriter.toString());
                 bwi.newLine();
        }
            bwi.close();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
}
