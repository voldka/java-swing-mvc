package Model;

import java.util.Date;

public class History {
    private int id;
    private int productId;
    private String productName; // For display purposes
    private String action; // "Added", "Removed", "Updated"
    private int quantityChange;
    private int newQuantity;
    private Date changeDate;
    private String username; // User who made the change

    public History() {
        this.changeDate = new Date(); // Default to current date
    }

    public History(int productId, String productName, String action, int quantityChange, int newQuantity,
            String username) {
        this.productId = productId;
        this.productName = productName;
        this.action = action;
        this.quantityChange = quantityChange;
        this.newQuantity = newQuantity;
        this.changeDate = new Date(); // Default to current date
        this.username = username;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(int quantityChange) {
        this.quantityChange = quantityChange;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "History #" + id + ": " + action + " " + productName + " by " + (quantityChange >= 0 ? "+" : "")
                + quantityChange + " to " + newQuantity;
    }
}