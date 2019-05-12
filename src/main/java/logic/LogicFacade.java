/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.google.gson.Gson;
import data.FOGException;
import entity.Order;
import entity.Employee;
import data.LogInException;
import data.UpdateException;
import entity.Category;
import entity.Customer;
import entity.Odetail;
import entity.Product;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vl48
 */
public class LogicFacade {

    public boolean createOrder(Order order, Customer customer) {
        return new data.OrderMapper().createOrder(order, customer);
    }

    public Order getOrder(int id) {
        return new data.OrderMapper().getOrder(id);
    }

    public List<Order> getOrders() {
        return new data.OrderMapper().getOrders();
    }

    public List<Order> getOrdersUnassigned() {
        return new data.OrderMapper().getOrdersUnassigned();
    }

    public List<Order> getUnfinishedOrders() {
        return new data.OrderMapper().getUnfinishedOrders();
    }

    public void assignOrder(Employee user, Order order) {
        new data.OrderMapper().assignOrder(user, order);
    }

    public void assignOrder(int orderID, int employeeID) {
        new data.OrderMapper().assignOrder(orderID, employeeID);
    }
    
    public Order updateOrder(Order order) throws UpdateException {
        return new data.OrderMapper().updateOrder(order);
    }

    public Employee logIn(String username, String password) throws LogInException {
        return new data.UserMapper().logIn(username, password);
    }

    public List<Employee> getEmployees() {
        return new data.UserMapper().getEmployees();
    }

    public int createCustomer(Customer customer) {
        return new data.UserMapper().createCustomer(customer);
    }
    
    public Customer getCustomer(int customerID) {
        return new data.UserMapper().getCustomer(customerID);
    }

    public String getCategories() {
        return new Gson().toJson(new data.ProductMapper().getCategories());
    }

    public String getProductList(String category_id) {
        try {
            int cat_id = Integer.parseInt(category_id);
            return new Gson().toJson(new data.ProductMapper().getProductList(cat_id));
        } catch (NumberFormatException e) {
            return "error";
        }
    }

    public String getProduct(String product_id) {
        try {
            int prod_id = Integer.parseInt(product_id);
            return new Gson().toJson(new data.ProductMapper().getProduct(prod_id));
        } catch (NumberFormatException e) {
            return "error";
        }
    }

    public String saveProduct(String product) {
        try {
            Product prod = new Gson().fromJson(product, Product.class);
            boolean success = new data.ProductMapper().saveProduct(prod);
            if (!success) {
                throw new Exception();
            }
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }
    //Testing something
    
    public Employee getEmployee(int id) {
        return new data.UserMapper().getEmployee(id);
    }
    
    public List<Odetail> buildCarport(Order order) throws FOGException {
        return new data.DevMapper().buildCarport(order);
    }
    
    public List<Product> getRoofTypes() {
        return new data.ProductMapper().getRoofTypes();
    }
    
    public List<Order> getOwnOrders(int emplId) {
        return new data.OrderMapper().getOwnOrders(emplId);
    }
    
    public ArrayList<Category> getCategorieslist() {
        return new data.ProductMapper().getCategories();
    }

}
