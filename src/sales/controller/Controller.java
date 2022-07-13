package sales.controller;

import com.sun.tools.jdeprscan.CSV;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import sales.model.InvoiceHeader;
import sales.model.InvoiceLine;
import sales.model.InvoiceLineTableModel;
import sales.view.SalesFrame;

public class Controller implements ActionListener, ListSelectionListener {

    private final SalesFrame frame;
    private String hfilePath;
    private String ifilePath;
    private String selectedRow;
    private InvoiceLine item;
    private InvoiceHeader invoice;
    private ArrayList<InvoiceHeader> invoiceHeadersList;
    private ArrayList<InvoiceLine> invoiceItemsList;
    private InvoiceHeader header;
 
    
   

    public Controller(SalesFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        System.out.println(actionCommand + "done");
        switch (actionCommand) {
            case "Load Files":
            {
                try {
                    loadFiles();
                } catch (FileException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
            case "Ok":
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
        this.invoiceItemsList=lines;
        frame.getInvLineTable().setModel(new InvoiceLineTableModel(lines));
        
        
        ///////////display value in text fields /////////
        int i=frame.getInvHeaderTable().getSelectedRow();
        TableModel model =frame.getInvHeaderTable().getModel();
        frame.getInvNumlbl().setText(model.getValueAt(i, 0).toString());
        frame.getInvDatetxt().setText(model.getValueAt(i, 1).toString());
        frame.getNametxt().setText(model.getValueAt(i, 2).toString());
        frame.getInvTotaltxt().setText(model.getValueAt(i, 3).toString());
         
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
        System.out.println(this.frame.selecteditem);
        ArrayList<InvoiceLine> newItemList=new ArrayList<InvoiceLine>();
        
        for(int i=0 ; i< this.invoiceItemsList.size() ; i++)
        {
            if (invoiceItemsList==null)
            break;
            InvoiceLine in = this.invoiceItemsList.get(i);
                if(i != Integer.parseInt(this.frame.selecteditem) ){
                    newItemList.add(in);}
        }
        this.invoiceItemsList = newItemList;
        frame.setinvoiceItemsList(this.invoiceItemsList); //birsm l table mnl awel
        

    }

    private void Save() {
         invoice = new InvoiceHeader(Integer.parseInt(this.frame.getInvoiceNumber()),
                this.frame.getCustomerName(),
                 this.frame.getDate());
         this.invoiceHeadersList.add(invoice);
         frame.setInvoiceHeadersList(this.invoiceHeadersList); 
    }
   
    private void addItem() {
        System.out.println(this.invoiceItemsList);
        System.out.println("add item clicked");
      item = new InvoiceLine(Integer.parseInt(this.frame.getID()),
              this.frame.getItemName(),
              Double.parseDouble(this.frame.getItemPrice()),
              Integer.parseInt(this.frame.getCount()),
              header);
        this.invoiceItemsList.add(item);
         frame.setinvoiceItemsList(this.invoiceItemsList); 
    }
    
    private void loadFiles() throws FileException {
        try {
            JFileChooser fc = new JFileChooser();
            ////adding file format filter////
            /*fc.setFileSelectionMode(JFileChooser.FILES_ONLY);//
            fc.setDialogTitle("open test");//
            fc.removeChoosableFileFilter(fc.getFileFilter());//remove the default file filter*/
             // FileNameExtensionFilter filter=new FileNameExtensionFilter("CSV files",".csv");//
              // fc.addChoosableFileFilter(filter);
             int result = fc.showOpenDialog(frame);
               if (result == JFileChooser.APPROVE_OPTION) {
                File headerFile = fc.getSelectedFile();
                String headerStrPath = headerFile.getAbsolutePath();
                   System.out.println(">>>>>>>>>>>>>>"+ headerStrPath);
                   if(headerStrPath.endsWith("csv"))
                   {
                       System.out.println("accepted format");
                   }else
                   {
                    this.frame.Showerror();

                       System.out.println("Not accepted format");
                       throw new FileException();
                   }
                hfilePath = headerStrPath;
                Path headerPath = Paths.get(headerStrPath);
                List<String> headerLines = Files.lines(headerPath).collect(Collectors.toList());
                // ["1,22-11-2020,Ali", "2,13-10-2021,Saleh"]
                this.invoiceHeadersList = new ArrayList<>();
                //if (fc.getSelectedFile()!= 
                for (String headerLine : headerLines) {
                    String[] parts = headerLine.split(",");
                    // parts = ["1", "22-11-2020", "Ali"]
                    // parts = ["2", "13-10-2021", "Saleh"]
                    if(parts[0].equals(""))
                        continue;
                    int id = Integer.parseInt(parts[0]);
                    InvoiceHeader invHeader = new InvoiceHeader(id, parts[2], parts[1]);
                    invoiceHeadersList.add(invHeader);
                }}
                        
                //System.out.println("check");
                result = fc.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String lineStrPath = fc.getSelectedFile().getAbsolutePath();
                    ifilePath=lineStrPath;
                    Path linePath = Paths.get(lineStrPath);
                    List<String> lineLines = Files.lines(linePath).collect(Collectors.toList());
                    System.out.println(">>>>>>>>>>>>>>"+ lineStrPath);
                   if(lineStrPath.endsWith("csv"))
                   {
                       System.out.println("accepted line format");
                   }else
                   {
                    this.frame.Showerror();
                    System.out.println("Not accepted line format");
                       throw new FileException();
                   }
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
                        InvoiceLine invLine = new InvoiceLine(invId,parts[1], price, count, header);
                        header.getLines().add(invLine);
                    }}
                    frame.setInvoiceHeadersList(invoiceHeadersList);
                      }
             }
        catch (IOException ex) {
            this.frame.Showerror();
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
            this.header=invoice;
            bwh.write(invoice.toString());
            bwh.newLine();
        } bwh.close();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
       FileWriter fwi;
        try {
             File x = new File(ifilePath);
            if(x.exists())
            x.delete();
            
            fwi = new FileWriter(this.ifilePath, true);
            BufferedWriter bwi = new BufferedWriter(fwi);
          
            for(int i=0 ; i< this.invoiceItemsList.size() ; i++)
        {
            InvoiceLine InvWriter = this.invoiceItemsList.get(i);
            InvWriter.setHeader(header);
            System.out.println(header.getId() + "ID is ");
            
                 bwi.write(InvWriter.toString());
                 bwi.newLine();
        }
            bwi.close();
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }

    private Object extension(File headerFile) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
