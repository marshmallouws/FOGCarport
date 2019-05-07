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

    // simple with no id and order id
    public Odetail(Product product, int qty, double amount) {
        this.product = product;
        this.qty = qty;
        this.amount = amount;
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
    
    
    
    
}
