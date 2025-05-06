package Controller;

import DAO.OrderDAO;
import DAO.TransactionDAO;
import Model.Order;
import View.OrderDetailView;
import View.OrderView;

import javax.swing.*;
import java.util.List;

public class OrderController {
    private OrderDAO orderDAO;
    private TransactionDAO transactionDAO;
    private OrderView orderView;
    private OrderDetailView orderDetailView;

    public OrderController(OrderView orderView, OrderDetailView orderDetailView) {
        this.orderDAO = new OrderDAO();
        this.transactionDAO = new TransactionDAO();
        this.orderView = orderView;
        this.orderDetailView = orderDetailView;

        // Thiết lập event listeners cho OrderView
        this.orderView.setAddButtonListener(e -> addOrder());
        this.orderView.setUpdateButtonListener(e -> updateOrder());
        this.orderView.setDeleteButtonListener(e -> deleteOrder());
        this.orderView.setClearButtonListener(e -> this.orderView.clearForm());
        this.orderView.setSearchButtonListener(e -> searchOrders());
        this.orderView.setViewDetailButtonListener(e -> viewOrderDetail());
        this.orderView.setCreateOrderButtonListener(e -> createNewOrder());
        this.orderView.setBackButtonListener(e -> fireBackEvent());
    }

    // Tải tất cả các đơn hàng
    public void loadAllOrders() {
        List<Order> orders = orderDAO.getAllOrders();
        orderView.loadOrders(orders);
    }

    // Thêm đơn hàng mới
    private void addOrder() {
        Order order = orderView.getOrderFromForm();

        if (order.getCustomerName().isEmpty()) {
            JOptionPane.showMessageDialog(orderView, "Vui lòng nhập tên khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Đặt tổng tiền ban đầu là 0, sẽ được tính sau khi thêm chi tiết
        order.setTotalAmount(0);

        int orderId = orderDAO.addOrder(order);

        if (orderId > 0) {
            JOptionPane.showMessageDialog(orderView, "Thêm đơn hàng thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            orderView.clearForm();
            loadAllOrders();

            // Chuyển sang màn hình chi tiết đơn hàng
            order.setId(orderId);
            fireViewOrderDetailEvent(order);
        } else {
            JOptionPane.showMessageDialog(orderView, "Thêm đơn hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tạo đơn hàng mới và chuyển sang màn hình chi tiết
    private void createNewOrder() {
        orderView.clearForm();
        Order newOrder = new Order();
        newOrder.setCustomerName("Khách hàng mới");
        newOrder.setOrderDate(new java.util.Date());
        newOrder.setStatus("Pending");
        newOrder.setTotalAmount(0);
        newOrder.setNote("");

        int orderId = orderDAO.addOrder(newOrder);

        if (orderId > 0) {
            newOrder.setId(orderId);
            fireViewOrderDetailEvent(newOrder);
        } else {
            JOptionPane.showMessageDialog(orderView, "Không thể tạo đơn hàng mới!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cập nhật đơn hàng
    private void updateOrder() {
        int selectedId = orderView.getSelectedOrderId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(orderView, "Vui lòng chọn một đơn hàng để cập nhật!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Order order = orderView.getOrderFromForm();

        if (order.getCustomerName().isEmpty()) {
            JOptionPane.showMessageDialog(orderView, "Vui lòng nhập tên khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Giữ nguyên tổng tiền khi cập nhật thông tin đơn hàng
        Order currentOrder = orderDAO.getOrderById(selectedId);
        order.setTotalAmount(currentOrder.getTotalAmount());

        boolean success = orderDAO.updateOrder(order);

        if (success) {
            JOptionPane.showMessageDialog(orderView, "Cập nhật đơn hàng thành công!", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            orderView.clearForm();
            loadAllOrders();
        } else {
            JOptionPane.showMessageDialog(orderView, "Cập nhật đơn hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xóa đơn hàng
    private void deleteOrder() {
        int selectedId = orderView.getSelectedOrderId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(orderView, "Vui lòng chọn một đơn hàng để xóa!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(orderView,
                "Bạn có chắc chắn muốn xóa đơn hàng này không? \nTất cả chi tiết đơn hàng cũng sẽ bị xóa.",
                "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = orderDAO.deleteOrder(selectedId);

            if (success) {
                JOptionPane.showMessageDialog(orderView, "Xóa đơn hàng thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                orderView.clearForm();
                loadAllOrders();
            } else {
                JOptionPane.showMessageDialog(orderView, "Xóa đơn hàng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Tìm kiếm đơn hàng
    private void searchOrders() {
        String keyword = orderView.getSearchKeyword();
        if (keyword.isEmpty()) {
            loadAllOrders();
        } else {
            List<Order> orders = orderDAO.searchOrdersByCustomerName(keyword);
            orderView.loadOrders(orders);
        }
    }

    // Xem chi tiết đơn hàng
    private void viewOrderDetail() {
        int selectedId = orderView.getSelectedOrderId();
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(orderView, "Vui lòng chọn một đơn hàng để xem chi tiết!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Order order = orderDAO.getOrderById(selectedId);
        if (order != null) {
            fireViewOrderDetailEvent(order);
        }
    }

    // Interface cho sự kiện quay lại màn hình chính
    public interface BackListener {
        void onBack();
    }

    // Interface cho sự kiện chuyển sang màn hình chi tiết
    public interface ViewOrderDetailListener {
        void onViewOrderDetail(Order order);
    }

    private BackListener backListener;
    private ViewOrderDetailListener viewOrderDetailListener;

    // Thiết lập listeners
    public void setBackListener(BackListener listener) {
        this.backListener = listener;
    }

    public void setViewOrderDetailListener(ViewOrderDetailListener listener) {
        this.viewOrderDetailListener = listener;
    }

    // Kích hoạt sự kiện
    private void fireBackEvent() {
        if (backListener != null) {
            backListener.onBack();
        }
    }

    private void fireViewOrderDetailEvent(Order order) {
        if (viewOrderDetailListener != null) {
            viewOrderDetailListener.onViewOrderDetail(order);
        }
    }
}