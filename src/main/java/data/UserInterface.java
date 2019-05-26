/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Customer;
import entity.Employee;
import java.util.List;

/**
 *
 * @author Annika
 */
public interface UserInterface {

    /**
     * Attemps to log in an employee with given username and password
     * @param username
     * @param password
     * @return an employee object on successful login
     * @throws LogInException
     */
    public Employee logIn(String username, String password) throws LogInException;

    /**
     * Gets a list of all employees
     * @return List of employee objects
     */
    public List<Employee> getEmployees();

    /**
     * Gets a specific customer by given id
     * @param customerID
     * @return Customer object
     */
    public Customer getCustomer(int customerID);
}
