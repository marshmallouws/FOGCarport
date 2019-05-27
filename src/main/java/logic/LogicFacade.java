/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import com.google.gson.Gson;
import data.BuildException;
import data.BuilderMapper;
import data.Connector;
import data.ConnectorInterface;
import entity.Order;
import entity.Employee;
import data.LogInException;
import data.OrderMapper;
import data.ProductMapper;
import data.UpdateException;
import data.UserMapper;
import entity.Blueprint;
import entity.Category;
import entity.Customer;
import entity.Odetail;
import entity.Orequest;
import entity.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vl48
 */
public class LogicFacade {
    //Should instanciate all Mappers in a constructor to avoid unnessecary object creation at each method call
    //          ^^^^^^ Why create instances of all mappers if only one is needed?
    private ConnectorInterface conn = Connector.getInstance();
    private OrderMapper ordermapper = new OrderMapper(conn);
    private UserMapper usermapper = new UserMapper(conn);
    private ProductMapper productmapper = new ProductMapper(conn);
    private BuilderMapper builderMapper = new BuilderMapper(conn);
    private Builder builder = new Builder(conn);

//    public int createOrder(Order order, Customer customer) {
//        return new data.OrderMapper(conn).createOrder(order, customer);
//    }

    /**
     * Inserts a new order using an object object and the given customer information. 
     * @param order the complete order object.
     * @param name
     * @param email
     * @param address
     * @param zip
     * @param phone
     * @return the generated id of the order
     */ 
    public int createOrder(Order order, String name, String email, String address, int zip, int phone) {
        return new data.OrderMapper(conn).createOrder(order, name, email, address, zip, phone);
    }

    /**
     * Gets a specific order object
     * @param id the order id
     * @return order object with given id
     */
    public Order getOrder(int id) {
        return ordermapper.getOrder(id);
    }

    /**
     * Gets a list of all stored orders
     * @return List of order objects
     */
    public List<Order> getOrders() {
        return ordermapper.getOrders();
    }

    /**
     * Gets a list of all stored orders that hasn't been assigned to an employee.
     * @return List of order objects.
     */
    public List<Order> getOrdersUnassigned() {
        return ordermapper.getOrdersUnassigned();
    }

    /**
     * Gets all orders where the order status hasn't been set to 'delivered'.
     * @return List of order objects.
     */
    public List<Order> getUnfinishedOrders() {
        return ordermapper.getUnfinishedOrders();
    }

    /**
     * Assigns an order to a specific employee.
     * @param user the employee to assign the order to
     * @param order the order to be assigned
     */
    public void assignOrder(Employee user, Order order) {
        ordermapper.assignOrder(user, order);
    }

    /**
     * Assigns an order to a specific employee
     * @param orderID the id of the order to be assigned
     * @param employeeID the id of the employee to assign the order to
     */
    public void assignOrder(int orderID, int employeeID) {
        ordermapper.assignOrder(orderID, employeeID);
    }

    /**
     * Updates the stored order information with the new order information
     * @param order order object with new information
     * @return the updated order object
     * @throws UpdateException
     */
    /*
    public Order updateOrder(Order order) throws UpdateException {
        return ordermapper.updateOrder(order);
    } */
    
    /**
     * Updates the stored order information and buildlist/odetail list with the new order information
     * @param order order object with new information
     * @param carport new odetail list
     * @return true if success, otherwise false
     * @throws UpdateException
     */
    public boolean updateOrderFull(Order order, List<Odetail> carport) throws UpdateException {
        return ordermapper.updateOrder(order, carport);
    }

    /**
     * Attemps to log in an employee with given username and password
     * @param username
     * @param password
     * @return an employee object on successful login
     * @throws LogInException
     */
    public Employee logIn(String username, String password) throws LogInException {
        return usermapper.logIn(username, password);
    }

    /**
     * Gets a list of all employees
     * @return List of employee objects
     */
    public List<Employee> getEmployees() {
        return usermapper.getEmployees();
    }

    /**
     * Inserts a customer with given information in db
     * @param customer a complete customer object.
     * @return the generated id
     */
    public int createCustomer(Customer customer) {
        return usermapper.createCustomer(customer);
    }

    /**
     * Gets a specific customer by given id
     * @param customerID
     * @return Customer object
     */
    public Customer getCustomer(int customerID) {
        return usermapper.getCustomer(customerID);
    }

    /**
     * Gets all product categories in JSON format.
     * @return JSON object
     */
    public String getCategories() {
        return new Gson().toJson(productmapper.getCategories());
    }
    
    /**
     * Gets all models in JSON format.
     * @return JSON object
     */
    public String getModels() {
        try {
            return new Gson().toJson(builderMapper.getModels());
        } catch (BuildException ex) {
            return "error";
        }
    }
    
    /**
     * Gets a blueprint in JSON format from a specific model ID .
     * @param modelID
     * @return JSON object
     */
    public String getBlueprint(String modelID) {
        try {
            int id = Integer.parseInt(modelID);
            return new Gson().toJson(builderMapper.getBlueprint(id));
        } catch (BuildException | NumberFormatException ex) {
            return "error";
        }
    }

    /**
     * Gets a list of all product variants in JSON format for a given category and product combination.
     * @param categoryID
     * @param productID
     * @return JSON object
     */
    public String getProductVariantsList(String categoryID, String productID) {
        try {
            int catID = Integer.parseInt(categoryID);
            int prodID = Integer.parseInt(productID);
            return new Gson().toJson(productmapper.getProductVariantsList(catID, prodID));
        } catch (NumberFormatException ex) {
            return "error";
        }
    }

    /**
     * Gets a list of all products in JSON format in a given category.
     * @param categoryID
     * @return JSON object
     */
    public String getProductsInCategories(String categoryID) {
        try {
            int catID = Integer.parseInt(categoryID);
            return new Gson().toJson(productmapper.getProductsInCategories(catID));
        } catch (NumberFormatException ex) {
            return "error";
        }

    }

    /**
     * Gets a specific product variant in JSON format by a given ID.
     * @param product_id
     * @return JSON object
     */
    public String getProductVariant(String product_id) {
        try {
            int prod_id = Integer.parseInt(product_id);
            return new Gson().toJson(productmapper.getProductVariant(prod_id));
        } catch (NumberFormatException e) {
            return "error";
        }
    }

    /**
     * Gets a specific product variant in JSON format by a given ID.
     * @param product_id
     * @return JSON object
     */
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
    
    /**
     * Gets a specific product in JSON format by a given ID.
     * @param product_id
     * @return JSON object
     */
    public String getProductMain(String product_id) {
        try {
            int prod_id = Integer.parseInt(product_id);
            return new Gson().toJson(productmapper.getProductMain(prod_id));
        } catch(NumberFormatException ex) {
            return "error";
        }
    }

    /**
     * Updates a product stored in DB.
     * @param product as JSON object
     * @return success or error message
     */
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

    /**
     * Inserts a new product in the DB
     * @param product as JSON object
     * @return success or error message
     */
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

    /**
     * Inserts a new product variant in DB
     * @param product as JSON object
     * @return success or error message
     */
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

    /**
     * Gets a specific employee by a given ID
     * @param id
     * @return Employee object
     */
    public Employee getEmployee(int id) {
        return usermapper.getEmployee(id);
    }
    
    /**
     *
     * @param order
     * @return
     * @throws BuildException
     */
    public List<Odetail> buildCarport(Order order) throws BuildException {
        List<Blueprint> blueprint = builderMapper.getBlueprint(1);
        List<Orequest> car = builder.carportBlueprint(order, blueprint);
        return builderMapper.carportBuilder(car, order);    
    }

    /**
     * Gets a list of all products with category Roof
     * @return List of Product objects
     */
    public List<Product> getRoofTypes() {
        return productmapper.getRoofTypes();
    }

    /**
     *
     * @param emplId
     * @return
     */
    public List<Order> getOwnOrders(int emplId) {
        return ordermapper.getOwnOrders(emplId);
    }
    
    /**
     *
     * @return
     */
    public ArrayList<Category> getCategorieslist() {
        return productmapper.getCategories();
    }

    /**
     *
     * @param orderID
     * @return
     */
    public List<Odetail> getOdetails(int orderID) {
        return ordermapper.getOdetails(orderID);
    }

    /**
     *
     * @param odetails
     */
    public void createOdetail(List<Odetail> odetails) {
        ordermapper.createOdetail(odetails);
    }

    /**
     *
     * @param odetails
     */
    public void editOdetails(List<Odetail> odetails) {
        ordermapper.editOdetails(odetails);
    }
    
}
