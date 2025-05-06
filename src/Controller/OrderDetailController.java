package Controller;

import DAO.OrderDAO;
import DAO.ProductDAO;
import DAO.TransactionDAO;
import Model.Order;
import Model.Product;
import Model.Transaction;
import View.OrderDetailView;

import javax.swing.*;
import java.util.List;

public class OrderDetailController {
    private OrderDAO orderDAO;
    private ProductDAO productDAO;
    private TransactionDAO transactionDAO;
    private OrderDetailView orderDetailView;
    private Order currentOrder;

    public OrderDetailController(OrderDetailView orderDetailView) {
        this.orderDAO = new OrderDAO();
        this.productDAO = new ProductDAO();
        this.transactionDAO = new TransactionDAO();
        this.orderDetailView = orderDetailView;

        // Thiết lập event listeners
        this.orderDetailView.setAddItemButtonListener(e -> addItem());
        this.orderDetailView.setRemoveItemButtonListener(e -> removeItem());
        this.orderDetailView.setSaveOrderButtonListener(e -> saveOrder());
        this.orderDetailView.setBackButtonListener(e -> fireBackToOrdersEvent());
    }

    // Tải thông tin đơn hàng
    public void loadOrder(Order order) {
        this.currentOrder = order;
        orderDetailView.setOrderInfo(order.getId(), order.getCustomerName());
        loadOrderItems();
        loadAvailableProducts();
    }

    // Tải các sản phẩm có sẵn để thêm vào đơn hàng
    private void loadAvailableProducts() {
        List<Product> products = productDAO.getAllProducts();
        orderDetailView.loadProducts(products);
    }

    // Tải các mục trong đơn hàng
    private void loadOrderItems() {
        List<Transaction> transactions = transactionDAO.getTransactionsByOrderId(currentOrder.getId());
        orderDetailView.loadOrderItems(transactions);
    }

    // Thêm sản phẩm vào đơn hàng
    private void addItem() {
        Transaction transaction = orderDetailView.getTransactionFromForm();

        if (transaction == null) {
            JOptionPane.showMessageDialog(orderDetailView, "Vui lòng nhập đầy đủ thông tin!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (transaction.getQuantity() <= 0) {
            JOptionPane.showMessageDialog(orderDetailView, "Số lượng phải lớn hơn 0!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra tồn kho
        if (!transactionDAO.checkStockAvailability(transaction.getProductId(), transaction.getQuantity())) {
            JOptionPane.showMessageDialog(orderDetailView, "Số lượng xuất vượt quá số lượng tồn kho!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Thêm chi tiết xuất kho
        boolean success = transactionDAO.addTransaction(transaction);

        if (success) {
            JOptionPane.showMessageDialog(orderDetailView, "Thêm sản phẩm thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            orderDetailView.clearItemForm();
            loadOrderItems();

            // Cập nhật tổng tiền của đơn hàng
            updateOrderTotal();
        } else {
            JOptionPane.showMessageDialog(orderDetailView, "Thêm sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xóa sản phẩm khỏi đơn hàng
    private void removeItem() {
        int selectedId = orderDetailView.getSelectedItemId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(orderDetailView, "Vui lòng chọn một sản phẩm để xóa!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(orderDetailView,
                "Bạn có chắc chắn muốn xóa sản phẩm này khỏi đơn hàng không?",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = transactionDAO.deleteTransaction(selectedId);

            if (success) {
                JOptionPane.showMessageDialog(orderDetailView, "Xóa sản phẩm thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                loadOrderItems();

                // Cập nhật tổng tiền của đơn hàng
                updateOrderTotal();
            } else {
                JOptionPane.showMessageDialog(orderDetailView, "Xóa sản phẩm thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Cập nhật tổng tiền đơn hàng
    private void updateOrderTotal() {
        transactionDAO.updateOrderTotal(currentOrder.getId());
        // Cập nhật lại đơn hàng hiện tại
        currentOrder = orderDAO.getOrderById(currentOrder.getId());
    }

    // Lưu đơn hàng và hoàn tất
    private void saveOrder() {
        // Kiểm tra xem đơn hàng có sản phẩm nào không
        List<Transaction> transactions = transactionDAO.getTransactionsByOrderId(currentOrder.getId());
        if (transactions.isEmpty()) {
            JOptionPane.showMessageDialog(orderDetailView, "Vui lòng thêm ít nhất một sản phẩm vào đơn hàng!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cập nhật trạng thái đơn hàng thành Completed
        currentOrder.setStatus("Completed");
        boolean success = orderDAO.updateOrderStatus(currentOrder.getId(), "Completed");

        if (success) {
            JOptionPane.showMessageDialog(orderDetailView, "Đơn hàng đã được lưu và hoàn tất!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            fireBackToOrdersEvent();
        } else {
            JOptionPane.showMessageDialog(orderDetailView, "Không thể hoàn tất đơn hàng!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Interface cho sự kiện quay lại danh sách đơn hàng
    public interface BackToOrdersListener {
        void onBackToOrders();
    }

    private BackToOrdersListener backToOrdersListener;

    // Thiết lập listener
    public void setBackToOrdersListener(BackToOrdersListener listener) {
        this.backToOrdersListener = listener;
    }

    // Kích hoạt sự kiện quay lại
    private void fireBackToOrdersEvent() {
        if (backToOrdersListener != null) {
            backToOrdersListener.onBackToOrders();
        }
    }
}