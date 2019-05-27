package entity;

/**
 * Represents a Model.
 */
public class Model {
    int id;
    int project_id;
    String title;

    public Model(int id, int project_id, String title) {
        this.id = id;
        this.project_id = project_id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public int getProject_id() {
        return project_id;
    }

    public String getTitle() {
        return title;
    }
    
    
}
