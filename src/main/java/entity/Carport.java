/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

/**
 *
 * @author caspe
 */
public class Carport {

    int height;
    int length;
    int width;
    double price;
    List<Odetail> items;

    public Carport(List<Odetail> items) {
        this.items = items;
        this.price = calcPrice(items);
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
        return Double.valueOf(d.format(total)); 
    }

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
    
    public int getCountCategory(int categoryID) {
        try {
            if (items == null) {
                throw new Exception();
            }
            int count = 0;
            for (Odetail o : items) {
                if (o.getProduct().getCategory().getId() == categoryID) {
                    count++;
                }
            }
            return count;
        } catch (Exception ex) {
            return 0;
        }
    }
}
