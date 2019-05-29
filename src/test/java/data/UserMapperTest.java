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

    @Test
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

    @Test
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

    @Test
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
    
    @Test
    public void testGetCustomer() {
        Customer c = u.getCustomer(13);
        String name = "Peter Petersen";
        String email = "peter@mail.dk";
        int zip = 9990;
        int phone = 12345678;

        assertNotNull(c);
        assertEquals(name, c.getName());
        assertEquals(email, c.getEmail());
        assertEquals(zip, c.getZip());
        assertEquals(phone, c.getPhone());

    }
}
