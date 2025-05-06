package Model;

public class Category {
    private int id;
    private String categoryItem;

    public Category() {
        // empty constructor
    }

    public Category(int id, String categoryItem) {
        this.id = id;
        this.categoryItem = categoryItem;
    }

    public Category(String categoryItem) {
        this.categoryItem = categoryItem;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getCategoryItem() {
        return categoryItem;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryItem(String categoryItem) {
        this.categoryItem = categoryItem;
    }

    @Override
    public String toString() {
        return categoryItem;
    }
}