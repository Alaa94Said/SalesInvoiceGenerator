
package sales.model;

import java.util.ArrayList;

public class InvoiceLine {
    private String itemName;
    private int id;
    private double unitPrice;
    private int count;
    private InvoiceHeader header;
    private ArrayList<InvoiceLine> item;
   private ArrayList<InvoiceLine> invoiceItemsList;
    

    public InvoiceLine( int id, String itemName, double unitPrice, int count, InvoiceHeader header) {
        this.id=id;
        this.itemName = itemName;
        this.unitPrice = unitPrice;
        this.count = count;
        this.header = header;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public InvoiceHeader getHeader() {
        return header;
    }

    public void setHeader(InvoiceHeader header) {
        this.header = header;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getLineTotal() {
        return count * unitPrice;
    }
    public ArrayList<InvoiceLine> getItemLine() {
        if (item == null) {
            item = new ArrayList<>();
        }
        return item;
    }
    public ArrayList<InvoiceLine> getInvoiceItemsList() {
        return invoiceItemsList;
    }

    @Override
    public String toString() { 
        

         return  this.header.getId() + "," + itemName + "," + unitPrice+ "," + count + "," + getLineTotal();

         
         }

    
    
    
    
}
