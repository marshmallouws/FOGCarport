package data;

import logic.Builder;
import entity.Carport;
import entity.Odetail;
import entity.Order;
import entity.Orequest;
import entity.Product;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author caspe
 */
public class BuilderTest {

    private static BuilderMapper mapper;
    private static Builder builder;
    private static Order order;

    @BeforeClass
    public static void setUpClass() {
        ConnectorMock m = ConnectorMock.getInstance();
        mapper = new BuilderMapper(m);
        builder = new Builder(m);
        order = new Order(270, 640, 640, 300, 300, 20, 12);
    }

    /**
     * Test of getProductsAllForBuild method, of class Builder.
     */
    @Test
    public void testGetProductsAllForBuild() {
        System.out.println("getProductsAllForBuild - Stolper");
        int categoryID = 1;
        int productID = 7;
        BuilderMapper instance = mapper;
        int expResult = categoryID;
        List<Product> result = instance.getProductsAllForBuild(categoryID, productID);
        assertNotNull(result);

        for (Product p : result) {
            assertEquals(expResult, p.getCategory().getId());
        }

    }
    
    /**
     * Test of validateCarport method, of class Builder.
     */
//    @Test
//    public void testValidateCarport() {
//        System.out.println("validateCarport");
//        Carport carport = null;
//        Order order = null;
//        Builder instance = builder;
//        boolean expResult = false;
//        boolean result = instance.validateCarport(carport, order);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        //fail("The test case is a prototype.");
//    }
    
    @Test
    public void testCalcWoodsMap() throws BuildException {
        System.out.println("calcWoodsMap - remme");
        int categoryID = 2;
        int productID = 5;
        int length = order.getLenght();
        Builder instance = builder;
        int expResult = length;
        Map<Integer, Integer> result = instance.calcWoodsMap(categoryID, productID, length);
        int sum = 0;
        
        for (Map.Entry<Integer, Integer> entry : result.entrySet()) {
            sum += entry.getKey() * entry.getValue();
        }
        
        assertEquals(true, sum > length);
        
    }
    
    @Test
    public void testCalcRoofMap() {
        System.out.println("calcRoofMap");
        Order o = order;
        Builder instance = builder;
        int categoryID = 7;
        int productID = o.getRoofType();
        Map<Integer, Integer> expResult = new HashMap();
        expResult.put(210, 16);
        expResult.put(150, 16);
        Map<Integer, Integer> result = instance.calcRoofMap(categoryID, productID, o);
        
        assertEquals(expResult, result);
    }
    
    @Test
    public void testCalcSurfaceMap() {
        System.out.println("calcSurfaceMap");
        Order o = order;
        Builder instance = builder;
        int x = o.getShedLength();
        int y = o.getHeight();
        int categoryID = 5;
        int productID = 8;
        int sides = 2;
        Map<Integer, Integer> expResult = new HashMap();
        expResult.put(270, 60);
        Map<Integer, Integer> result = instance.calcSurfaceMap(x, y, categoryID, productID, o, sides);
        
        assertEquals(expResult, result);
    }

    /**
     * Test of calcSpaerMap method, of class Builder.
     */
    @Test
    public void testCalcSpaerMap() throws BuildException {
        System.out.println("calcSpaerMap - flat");
        Order o = order;
        int categoryID = 8;
        int productID = 5;
        int x = o.getLenght();
        int y = o.getWidth();
        boolean angled = false;
        Builder instance = builder;
        int expResult = 15;
        Map<Integer, Integer> result = instance.calcSpaerMap(categoryID, productID, x, y, angled);
        assertEquals(true, result.containsValue(expResult));
    }

    /**
     * Test of calcStolerMap method, of class Builder.
     */
    @Test
    public void testCalcStolperMap() throws BuildException {
        System.out.println("calcStolperMap");
        int categoryID = 1;
        int productID = 7;
        Order o = order;
        Builder instance = builder;
        int expResult = 11;
        Map<Integer, Integer> result = instance.calcStolperMap(categoryID, productID, o);
        assertEquals(true, result.containsValue(expResult));
    }
    
    @Test
    public void testCalcRoofAngledLength() {
        System.out.println("calcRoofAngledLength");
    }

}
