package Model;

import java.util.Date;

public class Transaction {
    private int id;
    private int orderId;
    private int productId;
    private String productName; // For display purposes
    private int quantity;
    private double price;
    private double total;
    private Date transactionDate;

    public Transaction() {
        this.transactionDate = new Date(); // Default to current date
    }

    public Transaction(int id, int orderId, int productId, String productName, int quantity, double price, double total,
            Date transactionDate) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
        this.transactionDate = transactionDate;
    }

    public Transaction(int orderId, int productId, int quantity, double price) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.total = quantity * price;
        this.transactionDate = new Date(); // Default to current date
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return total;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        // Auto-update total when quantity changes
        this.total = this.quantity * this.price;
    }

    public void setPrice(double price) {
        this.price = price;
        // Auto-update total when price changes
        this.total = this.quantity * this.price;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "Transaction #" + id + ": " + productName + " x" + quantity + " @ $" + price + " = $" + total;
    }
}