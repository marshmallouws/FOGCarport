/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

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
        o = new OrderMapper();
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
        List<Order> orders = o.getOldOrders();
        
        assertNotNull(orders);
        int id = 1;
        int height = 300;
        int length = 660;
        int width = 420;
        int shedLength = 420;
        int shedWidth = 510;
        int roofAngle = 25;
        String date = "2019-05-13 22:27:22";
        String status = "recieved";
        boolean found = false;
       
        for(Order or: orders) {
            if(id == or.getId()) {
                assertEquals(height, or.getHeight());
                assertEquals(length, or.getLenght());
                assertEquals(width, or.getWidth());
                assertEquals(shedLength, or.getShedLength());
                assertEquals(shedWidth, or.getShedWidth());
                assertEquals(roofAngle, or.getRoofAngle());
                assertEquals(date, or.getDate());
                assertEquals(status, or.getStatus());
                found = true;
                break;
            }
        }
        
        if(!found) {
            fail(String.format("Expected id %d was not in database", id));
        }
    }
    
}
