/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Customer;
import entity.Employee;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Annika
 */
public class UserMapperTest {

    private static UserMapper u;

    @BeforeClass
    public static void setUpClass() {
        ConnectorMock m = ConnectorMock.getInstance();
        u = new UserMapper(m);
    }


    public void testLogin() {
        String username = "aaa";
        String password = "aaa";
        Employee e = null;
        try {
            e = u.logIn(username, password);
        } catch (LogInException ex) {
            fail("test failed with exception: " + ex.getMessage());
        }

        assertNotNull(e);
        assertEquals(e.getInitials(), username);
        assertEquals(e.getPassword(), password);
    }


    public void negativeTestLogin() {
        String username = null;
        String password = null;
        boolean success = false;
        try {
            u.logIn(username, password);
        } catch (Exception ex) { 
            //Makes it possible to test for more exceptions
            if (ex instanceof LogInException) {
                //Expected
                success = true;
            }
        }

        assertTrue(success);
    }


    public void testGetEmployee() {
        List<Employee> employees = u.getEmployees();

        int id = 1;
        String initials = "aaa";
        boolean found = false;
        assertNotNull(employees);

        for (Employee e : employees) {
            if (e.getId() == id) {
                assertEquals(initials, e.getInitials());
                found = true;
                break;
            }
        }

        if (!found) {
            fail(String.format("Expected id %d was not in database", id));
        }
    }
    

    public void testGetCustomer() {
        Customer c = u.getCustomer(4);
        String name = "Annika";
        String email = "2591.0@mail.dk";
        int zip = 2750;
        int phone = 12341234;

        assertNotNull(c);
        assertEquals(name, c.getName());
        assertEquals(email, c.getEmail());
        assertEquals(zip, c.getZip());
        assertEquals(phone, c.getPhone());

    }
    
    //Might fail if random number for email is already in db.

    public void testCreateCustomer() {
        double rand = (int)(Math.random()*10000); //Mail is unique
        String name = "Annika";
        String email = rand + "@mail.dk";
        String address = "annikavej 1";
        int zip = 2750;
        int phone = 12341234;
        Customer c = new Customer(name, email, address, zip, phone);
        
        int id = u.createCustomer(c);
        
        Customer customer = u.getCustomer(id);
        
        assertNotNull(customer);
        assertEquals(name, customer.getName());
        assertEquals(address, customer.getAddress());
        assertEquals(zip, customer.getZip());
        
    }
}
