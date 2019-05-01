/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Order;
import entity.Employee;
import java.util.ArrayList;

/**
 *
 * @author Annika
 */
public class OneMapperToRuleThemAll {
    private OrderMapper order;
    private ProductMapper product;
    private UserMapper user;
    
    public OneMapperToRuleThemAll(OrderMapper order, ProductMapper product, UserMapper user) {
        this.order = order;
        this.product = product;
        this.user = user;
    }
    
    //Insert all mapper methods. Makes it easier to call mapper-methods from other classes.
    
    public ArrayList<Order> getUnfinishedOrders() {
        return order.getUnfinishedOrders();
    }
    
    public ArrayList<Order> getOldOrders() {
        return order.getOldOrders();
    }
    
    public boolean createOrder(Order o) {
        return order.createOrder(o);
    }
    
    public Order getOrder(int id) {
        return order.getOrder(id);
    }
    
    public void assignOrder(Employee u, Order o) {
        order.assignOrder(u, o);
    }
    
    public Order updateOrder(Order o) {
        return order.updateOrder(o);
    }
    
}
