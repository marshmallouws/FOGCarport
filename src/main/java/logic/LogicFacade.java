/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.google.gson.Gson;
import data.BuildException;
import data.Builder;
import data.Connector;
import data.ConnectorInterface;
import data.FOGException;
import entity.Order;
import entity.Employee;
import data.LogInException;
import data.OrderMapper;
import data.ProductMapper;
import data.UpdateException;
import data.UserMapper;
import entity.Category;
import entity.Customer;
import entity.Odetail;
import entity.Orequest;
import entity.Product;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vl48
 */
public class LogicFacade {
    //Should instanciate all Mappers in a constructor to avoid unnessecary object creation at each method call
    
    private ConnectorInterface conn = Connector.getInstance();
    private OrderMapper ordermapper = new OrderMapper(conn);
    private UserMapper usermapper = new UserMapper(conn);
    private ProductMapper productmapper = new ProductMapper(conn);

    public int createOrder(Order order, Customer customer) {
        return ordermapper.createOrder(order, customer);
    }

    public Order getOrder(int id) {
        return ordermapper.getOrder(id);
    }

    public List<Order> getOrders() {
        return ordermapper.getOrders();
    }

    public List<Order> getOrdersUnassigned() {
        return ordermapper.getOrdersUnassigned();
    }

    public List<Order> getUnfinishedOrders() {
        return ordermapper.getUnfinishedOrders();
    }

    public void assignOrder(Employee user, Order order) {
        ordermapper.assignOrder(user, order);
    }

    public void assignOrder(int orderID, int employeeID) {
        ordermapper.assignOrder(orderID, employeeID);
    }

    public Order updateOrder(Order order) throws UpdateException {
        return ordermapper.updateOrder(order);
    }

    public Employee logIn(String username, String password) throws LogInException {
        return usermapper.logIn(username, password);
    }

    public List<Employee> getEmployees() {
        return usermapper.getEmployees();
    }

    public int createCustomer(Customer customer) {
        return usermapper.createCustomer(customer);
    }

    public Customer getCustomer(int customerID) {
        return usermapper.getCustomer(customerID);
    }

    public String getCategories() {
        return new Gson().toJson(productmapper.getCategories());
    }

    public String getProductVariantsList(String categoryID, String productID) {
        try {
            int catID = Integer.parseInt(categoryID);
            int prodID = Integer.parseInt(productID);
            return new Gson().toJson(productmapper.getProductVariantsList(catID, prodID));
        } catch (NumberFormatException ex) {
            return "error";
        }
    }

    public String getProductsInCategories(String categoryID) {
        try {
            int catID = Integer.parseInt(categoryID);
            return new Gson().toJson(productmapper.getProductsInCategories(catID));
        } catch (NumberFormatException ex) {
            return "error";
        }

    }

    public String getProductVariant(String product_id) {
        try {
            int prod_id = Integer.parseInt(product_id);
            return new Gson().toJson(productmapper.getProductVariant(prod_id));
        } catch (NumberFormatException e) {
            return "error";
        }
    }

    public String getProductVariant(int product_id) {
        try {
            if (product_id == 0) {
                throw new Exception();
            }

            return new Gson().toJson(productmapper.getProductVariant(product_id));
        } catch (Exception e) {
            return "error";
        }

    }
    
    public String getProductMain(String product_id) {
        try {
            int prod_id = Integer.parseInt(product_id);
            return new Gson().toJson(productmapper.getProductMain(prod_id));
        } catch(NumberFormatException ex) {
            return "error";
        }
    }

    public String updateProductVariant(String product) {
        try {
            Product prod = new Gson().fromJson(product, Product.class);
            boolean success = productmapper.updateProductVariant(prod);

            if (!success) {
                throw new Exception();
            }
            return "succes";

        } catch (Exception ex) {
            return "error";
        }

    }

    public String createProduct(String product) {
        try {
            Product prod = new Gson().fromJson(product, Product.class);
            int id = productmapper.createProduct(prod);

            if (id == 0) {
                throw new Exception();
            }
            return "succes";

        } catch (Exception ex) {
            return "error";
        }
    }

    public String createProductVariant(String product) {
        try {
            Product prod = new Gson().fromJson(product, Product.class);
            int id = productmapper.createProductVariant(prod);
            
            if (id == 0) {
                throw new Exception();
            }

            return "succes";
        } catch (Exception ex) {
            return "error";
        }

    }

    //Testing something
    public Employee getEmployee(int id) {
        return usermapper.getEmployee(id);
    }
    
    public List<Odetail> buildCarport(Order order) throws BuildException {
        Builder b = new data.Builder(conn);
        List<Orequest> car = b.carportBlueprint(order);
        return b.carportBuilder(car, order);
    }

    public List<Product> getRoofTypes() {
        return productmapper.getRoofTypes();
    }

    public List<Order> getOwnOrders(int emplId) {
        return ordermapper.getOwnOrders(emplId);
    }
    
    public ArrayList<Category> getCategorieslist() {
        return productmapper.getCategories();
    }

    public List<Odetail> getOdetails(int orderID) {
        return ordermapper.getOdetails(orderID);
    }

    public void createOdetail(List<Odetail> odetails) {
        ordermapper.createOdetail(odetails);
    }

    public void editOdetails(List<Odetail> odetails) {
        ordermapper.editOdetails(odetails);
    }
}
