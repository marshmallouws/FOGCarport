package entity;

/**
 * Represents a blueprint.
 */
public class Blueprint {
    int id;
    int usage;
    int model_id;
    int category_id;
    String category_name;
    int product_id;
    String product_name;
    String message;

    public Blueprint(int id, int usage, int model_id, int category_id, int product_id, String message) {
        this.id = id;
        this.usage = usage;
        this.model_id = model_id;
        this.category_id = category_id;
        this.product_id = product_id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public int getUsage() {
        return usage;
    }

    public int getModel_id() {
        return model_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public String getMessage() {
        return message;
    }
    
    
}
