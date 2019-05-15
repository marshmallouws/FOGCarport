/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.google.gson.Gson;
import data.Builder;
import data.Connector;
import data.ConnectorInterface;
import data.FOGException;
import entity.Order;
import entity.Employee;
import data.LogInException;
import data.UpdateException;
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

    public int createOrder(Order order, Customer customer) {
        return new data.OrderMapper(conn).createOrder(order, customer);
    }

    public Order getOrder(int id) {
        return new data.OrderMapper(conn).getOrder(id);
    }

    public List<Order> getOrders() {
        return new data.OrderMapper(conn).getOrders();
    }

    public List<Order> getOrdersUnassigned() {
        return new data.OrderMapper(conn).getOrdersUnassigned();
    }

    public List<Order> getUnfinishedOrders() {
        return new data.OrderMapper(conn).getUnfinishedOrders();
    }

    public void assignOrder(Employee user, Order order) {
        new data.OrderMapper(conn).assignOrder(user, order);
    }

    public void assignOrder(int orderID, int employeeID) {
        new data.OrderMapper(conn).assignOrder(orderID, employeeID);
    }

    public Order updateOrder(Order order) throws UpdateException {
        return new data.OrderMapper(conn).updateOrder(order);
    }

    public Employee logIn(String username, String password) throws LogInException {
        return new data.UserMapper(conn).logIn(username, password);
    }

    public List<Employee> getEmployees() {
        return new data.UserMapper(conn).getEmployees();
    }

    public int createCustomer(Customer customer) {
        return new data.UserMapper(conn).createCustomer(customer);
    }

    public Customer getCustomer(int customerID) {
        return new data.UserMapper(conn).getCustomer(customerID);
    }

    public String getCategories() {
        return new Gson().toJson(new data.ProductMapper(conn).getCategories());
    }

    public String getProductVariantsList(String categoryID, String productID) {
        try {
            int catID = Integer.parseInt(categoryID);
            int prodID = Integer.parseInt(productID);
            return new Gson().toJson(new data.ProductMapper(conn).getProductVariantsList(catID, prodID));
        } catch (NumberFormatException ex) {
            return "error";
        }
    }

    public String getProductsInCategories(String categoryID) {
        try {
            int catID = Integer.parseInt(categoryID);
            return new Gson().toJson(new data.ProductMapper(conn).getProductsInCategories(catID));
        } catch (NumberFormatException ex) {
            return "error";
        }

    }

    public String getProductVariant(String product_id) {
        try {
            int prod_id = Integer.parseInt(product_id);
            return new Gson().toJson(new data.ProductMapper(conn).getProductVariant(prod_id));
        } catch (NumberFormatException e) {
            return "error";
        }
    }

    public String getProductVariant(int product_id) {
        try {
            if (product_id == 0) {
                throw new Exception();
            }

            return new Gson().toJson(new data.ProductMapper(conn).getProductVariant(product_id));
        } catch (Exception e) {
            return "error";
        }

    }
    
    public String getProductMain(String product_id) {
        try {
            int prod_id = Integer.parseInt(product_id);
            return new Gson().toJson(new data.ProductMapper(conn).getProductMain(prod_id));
        } catch(NumberFormatException ex) {
            return "error";
        }
    }

    public String updateProductVariant(String product) {
        try {
            Product prod = new Gson().fromJson(product, Product.class);
            boolean success = new data.ProductMapper(conn).updateProductVariant(prod);

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
            int id = new data.ProductMapper(conn).createProduct(prod);

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
            int id = new data.ProductMapper(conn).createProductVariant(prod);
            
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
        return new data.UserMapper(conn).getEmployee(id);
    }
    
    public List<Odetail> buildCarport(Order order) throws FOGException {
        Builder b = new data.Builder(conn);
        List<Orequest> car = b.carportBlueprint(order);
        return b.carportBuilder(car, order);
    }

    public List<Product> getRoofTypes() {
        return new data.ProductMapper(conn).getRoofTypes();
    }

    public List<Order> getOwnOrders(int emplId) {
        return new data.OrderMapper(conn).getOwnOrders(emplId);
    }
    
    public ArrayList<Category> getCategorieslist() {
        return new data.ProductMapper(conn).getCategories();
    }

    public List<Odetail> getOdetails(int orderID) {
        return new data.OrderMapper(conn).getOdetails(orderID);
    }

    public void createOdetail(List<Odetail> odetails) {
        new data.OrderMapper(conn).createOdetail(odetails);
    }

    public void editOdetails(List<Odetail> odetails) {
        new data.OrderMapper(conn).editOdetails(odetails);
    }
}
