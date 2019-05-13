/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author caspe
 */
public class Orequest extends Odetail {
    
    public Orequest(Product product, int order_id, int qty, double amount, String comment) {
        super(product, order_id, qty, amount, comment);
    }

    @Override
    public void setComment(String comment) {
        super.setComment(comment);
    }

    @Override
    public String getComment() {
        return super.getComment();
    }

    @Override
    public double getAmount() {
        return super.getAmount();
    }

    @Override
    public int getQty() {
        return super.getQty();
    }

    @Override
    public int getOrder_id() {
        return super.getOrder_id();
    }

    @Override
    public Product getProduct() {
        return super.getProduct();
    }

    @Override
    public int getId() {
        return super.getId();
    }
    
    
    
}
