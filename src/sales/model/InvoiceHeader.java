
package sales.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvoiceHeader {
    private int id;
    private String customerName;
    private String date;
    private ArrayList<InvoiceLine> lines;
    
    public InvoiceHeader(int id, String customerName, String date) {
        this.id = id;
        this.customerName = customerName;
        this.date = date;
    }
    
    @Override
    public String toString()
    {
        return "\n" + this.id + "," + this.getDate() + "," + this.customerName ;
        
  
    }

    public String getDate() {
        try {
            
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = sdf.parse(this.date);
        String stringDate= sdf.format(date);
            System.out.println(">>>>>>>" + stringDate);
           return stringDate;
        } catch (Exception ex) {
                System.out.println("date formate is wrong");
                
        return null;
        }
       
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getInvoiceTotal() {
        double total = 0;
        for (InvoiceLine line : getLines()) {
            total += line.getLineTotal();
        }
        return total;
    }

  

    public ArrayList<InvoiceLine> getLines() {
        if (lines == null) {
            lines = new ArrayList<>();
        }
        return lines;
    }
    
    
    
    
}
