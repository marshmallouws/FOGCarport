/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entity.Customer;
import entity.Order;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Annika
 */
public class OrdeMapperTest {

    private static OrderMapper o;

    @BeforeClass
    public static void setUpClass() {
        ConnectorMock m = ConnectorMock.getInstance();
        o = new OrderMapper(m);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetOrders() {
        List<Order> orders = o.getOrders();
        assertNotNull(orders);

        int id = 19;
        int height = 200;
        int length = 300;
        int width = 330;
        int shedLength = 0;
        int shedWidth = 0;

        boolean found = false;

        for (Order or : orders) {
            if (id == or.getId()) {
                assertEquals(height, or.getHeight());
                assertEquals(length, or.getLenght());
                assertEquals(width, or.getWidth());
                assertEquals(shedLength, or.getShedLength());
                assertEquals(shedWidth, or.getShedWidth());

                found = true;
                break;
            }
        }

        if (!found) {
            fail(String.format("Expected id %d was not in database", id));
        }
    }

    @Test
    public void testGetOrder() {
        Order order = o.getOrder(19);

        assertNotNull(order);

        int id = 19;
        int height = 200;
        int length = 300;
        int width = 330;
        int shedLength = 0;
        int shedWidth = 0;

        assertEquals(height, order.getHeight());
        assertEquals(length, order.getLenght());
        assertEquals(width, order.getWidth());
        assertEquals(shedLength, order.getShedLength());
        assertEquals(shedWidth, order.getShedWidth());

    }
    
    
    @Test
    public void testUpdateOrder() {
        //Make test
    }

    @Test
    public void testGetOwnOrders() {
        List<Order> orders = o.getOwnOrders(1);

        assertNotNull(orders);
        int id = 11;
        int height = 200;
        int length = 300;
        int width = 330;
        int shedLength = 0;
        int shedWidth = 0;
        int roofAngle = 0;

        String status = "recieved";
        boolean found = false;

        for (Order or : orders) {
            System.out.println(or.getId() + " " + or.getLenght());
            if (id == or.getId()) {
                int orheight = or.getHeight();
                int orlength = or.getLenght();
                int orwidth = or.getWidth();
                int orshedl = or.getShedLength();
                int orshedw = or.getShedWidth();
                int orroof = or.getRoofAngle();
                String orstat = or.getStatus();

                assertEquals(height, orheight);
                assertEquals(length, orlength);
                assertEquals(width, orwidth);
                assertEquals(shedLength, orshedl);
                assertEquals(shedWidth, orshedw);
                assertEquals(roofAngle, orroof);
                assertEquals(status, orstat);
                found = true;
                break;
            }
        }

        if (!found) {
            fail(String.format("Expected id %d was not in database", id));
        }
    }

    @Test
    public void testCreateOrder() {
        int height = 200;
        int width = 330;
        int length = 300;
        int shedL = 0;
        int shedW = 0;
        int roofangle = 0;
        int rooftype = 1;
        double rand = (int) (Math.random() * 10000); //Mail is unique
        String name = "Annika";
        String email = rand + "@mail.dk";
        String address = "annikavej 1";
        int zip = 2750;
        int phone = 12341234;

        Order ord = new Order(height, width, length, shedL, shedW, roofangle, rooftype);

        int id = o.createOrder(ord, name, email, address, zip, phone);
        Order order = o.getOrder(id);

        assertNotNull(order);
        assertEquals(height, order.getHeight());
        assertEquals(length, order.getLenght());
        assertEquals(width, order.getWidth());

        //int height, int width, int length, int shedLength, int shedWidth, int roofAngle, int roofType
    }

    @Test
    public void testGetOrdersUnassigned() {
        List<Order> orders = o.getOrdersUnassigned();
        assertNotNull(orders);

        for (Order o : orders) {
            if (o.getEmpl() != null) {
                fail();
            }
        }
    }
}
