package entity;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Represents a Carport.
 */
public class Carport {

    int height;
    int length;
    int width;
    /**
     * Represents the total material price for this Carport.
     * Not the sales price
     */
    double price;
    /**
     * Represents the items used to build this Carport.
     */
    List<Odetail> items;
    /**
     * Represents the blueprint for this Carport.
     */
    List<Blueprint> blueprint;

    public Carport(List<Odetail> items) {
        this.items = items;
        this.price = calcPrice(items);
    }
    
    public Carport(List<Odetail> items, List<Blueprint> blueprint) {
        this.items = items;
        this.price = calcPrice(items);
        this.blueprint = blueprint;
    }

    public double getPrice() {
        return price;
    }

    public List<Odetail> getItems() {
        return items;
    }

    private double calcPrice(List<Odetail> items) {
        double total = 0;
        for (Odetail o : items) {
            total += o.getAmount();
        }
        DecimalFormat d = new DecimalFormat("#.##");
        NumberFormat nf = NumberFormat.getInstance();
        try {
            return nf.parse(d.format(total)).doubleValue();
        } catch(ParseException e){
            return 0.0;
        }
    }

    /**
     * Gets the list of products without screws
     * @return List of Odetail
     */
    public List<Odetail> getWoodsList() {
        List<Odetail> woods = new ArrayList();
        for (Odetail o : items) {
            // screws
            if (o.getProduct().getCategory().getId() != 11) {
                woods.add(o);
            }
        }
        return woods;
    }

    /**
     * Gets the list of products with just screws
     * @return List of Odetail
     */
    public List<Odetail> getScrewsList() {
        List<Odetail> screws = new ArrayList();
        for (Odetail o : items) {
            // screws
            if (o.getProduct().getCategory().getId() == 11) {
                screws.add(o);
            }
        }
        return screws;
    }
    
    /**
     * Gets the total number of products by a categoryID
     * @param categoryID
     * @return int
     */
    public int getCountCategory(int categoryID) {
        try {
            if (items == null) {
                throw new Exception();
            }
            int count = 0;
            for (Odetail o : items) {
                if (o.getProduct().getCategory().getId() == categoryID) {
                    count+= o.getQty();
                }
            }
            return count;
        } catch (Exception ex) {
            return 0;
        }
    }
    
    /**
     * Gets the product id used to build this Carport by a usageID in the Blueprint
     * @param usageID
     * @return productID
     */
    public int getProductUsed(int usageID) {
        int id = 0;
        for (Blueprint b : blueprint) {
            if (b.getUsage() == usageID) {
                id = b.getProduct_id();
            }
        }
        return id;
    }
}
