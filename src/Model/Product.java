package Model;

public class Product {
    private int id;
    private String name;
    private int price;
    private int stock;
    private int unit; // Giữ lại để đảm bảo tương thích với cơ sở dữ liệu hiện tại
    private String category;

    public Product() {
        // empty constructor
        this.unit = 1; // Đặt giá trị mặc định là 1
    }

    public Product(int id, String name, int price, int stock, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.unit = 1; // Đặt giá trị mặc định là 1
        this.category = category;
    }

    // Constructor có unit cho tương thích ngược
    public Product(int id, String name, int price, int stock, int unit, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.unit = unit;
        this.category = category;
    }

    // Constructor không có id (dùng khi thêm mới)
    public Product(String name, int price, int stock, String category) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.unit = 1; // Đặt giá trị mặc định là 1
        this.category = category;
    }

    // Constructor có unit cho tương thích ngược
    public Product(String name, int price, int stock, int unit, String category) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.unit = unit;
        this.category = category;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getUnit() {
        return unit;
    }

    public String getCategory() {
        return category;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", name=" + name + ", price=" + price + ", stock=" + stock +
                ", unit=" + unit + ", category=" + category + "]";
    }
}