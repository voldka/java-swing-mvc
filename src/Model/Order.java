package Model;

import java.util.Date;
import java.util.List;

public class Order {
    private int id;
    private String customerName;
    private Date orderDate;
    private String status; // "Pending", "Completed", "Cancelled"
    private double totalAmount;
    private String note;
    private List<Transaction> transactions;

    public Order() {
        this.orderDate = new Date(); // Default to current date
        this.status = "Pending"; // Default status
    }

    public Order(String customerName, Date orderDate, String status, double totalAmount, String note) {
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.note = note;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "Order #" + id + ": " + customerName + " (" + status + ") - $" + totalAmount;
    }
}