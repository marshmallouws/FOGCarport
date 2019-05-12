package entity;

/**
 *
 * @author Casper
 */
public class Odetail {
    private int id;
    private Product product;
    private int order_id;
    private int qty;
    private double amount;
    private String comment;

    // simple with no id
    public Odetail(Product product, int order_id, int qty, double amount, String comment) {
        this.product = product;
        this.order_id = order_id;
        this.qty = qty;
        this.amount = amount;
        this.comment = comment;
    }
    
    public Odetail(int id, int prod_id, int order_id, int qty) {
        this.product = product;
        this.order_id = order_id;
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getQty() {
        return qty;
    }

    public double getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }
    
    
    
    
    
    
}
