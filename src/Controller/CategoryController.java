package Controller;

import DAO.CategoryDAO;
import Model.Category;
import View.CategoryView;

import javax.swing.*;
import java.util.List;

public class CategoryController {
    private CategoryDAO categoryDAO;
    private CategoryView categoryView;
    private List<Category> categories;

    public CategoryController(CategoryView categoryView) {
        this.categoryDAO = new CategoryDAO();
        this.categoryView = categoryView;

        // Khởi tạo danh sách loại sản phẩm
        loadCategories();

        // Thiết lập các event listener cho các nút
        setupEventListeners();
    }

    // Tải danh sách loại sản phẩm từ database
    public void loadCategories() {
        categories = categoryDAO.getAllCategories();
        categoryView.loadCategories(categories);
    }

    // Thiết lập các event listener
    private void setupEventListeners() {
        // Thêm loại sản phẩm mới
        categoryView.setAddButtonListener(e -> {
            addCategory();
        });

        // Cập nhật loại sản phẩm
        categoryView.setUpdateButtonListener(e -> {
            updateCategory();
        });

        // Xóa loại sản phẩm
        categoryView.setDeleteButtonListener(e -> {
            deleteCategory();
        });

        // Xóa form
        categoryView.setClearButtonListener(e -> {
            categoryView.clearForm();
        });

        // Quay lại dashboard
        categoryView.setBackButtonListener(e -> {
            if (backListener != null) {
                backListener.onBackRequested();
            }
        });
    }

    // Thêm loại sản phẩm mới
    private void addCategory() {
        Category category = categoryView.getCategoryFromForm();

        // Kiểm tra thông tin nhập vào
        if (category.getCategoryItem() == null || category.getCategoryItem().trim().isEmpty()) {
            JOptionPane.showMessageDialog(categoryView, "Vui lòng nhập tên loại sản phẩm!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra trùng lặp
        if (categoryDAO.isCategoryExists(category.getCategoryItem())) {
            JOptionPane.showMessageDialog(categoryView, "Loại sản phẩm này đã tồn tại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Thêm vào database
        if (categoryDAO.addCategory(category)) {
            JOptionPane.showMessageDialog(categoryView, "Thêm loại sản phẩm thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadCategories();
            categoryView.clearForm();
        } else {
            JOptionPane.showMessageDialog(categoryView, "Thêm loại sản phẩm thất bại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cập nhật loại sản phẩm
    private void updateCategory() {
        Category category = categoryView.getCategoryFromForm();

        // Kiểm tra đã chọn loại sản phẩm chưa
        if (category.getId() <= 0) {
            JOptionPane.showMessageDialog(categoryView, "Vui lòng chọn loại sản phẩm cần cập nhật!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Kiểm tra thông tin nhập vào
        if (category.getCategoryItem() == null || category.getCategoryItem().trim().isEmpty()) {
            JOptionPane.showMessageDialog(categoryView, "Vui lòng nhập tên loại sản phẩm!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Cập nhật database
        if (categoryDAO.updateCategory(category)) {
            JOptionPane.showMessageDialog(categoryView, "Cập nhật loại sản phẩm thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadCategories();
            categoryView.clearForm();
        } else {
            JOptionPane.showMessageDialog(categoryView, "Cập nhật loại sản phẩm thất bại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xóa loại sản phẩm
    private void deleteCategory() {
        int categoryId = categoryView.getSelectedCategoryId();

        // Kiểm tra đã chọn loại sản phẩm chưa
        if (categoryId <= 0) {
            JOptionPane.showMessageDialog(categoryView, "Vui lòng chọn loại sản phẩm cần xóa!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Xác nhận xóa
        int option = JOptionPane.showConfirmDialog(categoryView, "Bạn có chắc chắn muốn xóa loại sản phẩm này?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.NO_OPTION) {
            return;
        }

        // Xóa khỏi database
        if (categoryDAO.deleteCategory(categoryId)) {
            JOptionPane.showMessageDialog(categoryView, "Xóa loại sản phẩm thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadCategories();
            categoryView.clearForm();
        } else {
            JOptionPane.showMessageDialog(categoryView,
                    "Xóa loại sản phẩm thất bại! Có thể loại sản phẩm này đang được sử dụng.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Interface và listener cho quay lại dashboard
    public interface BackListener {
        void onBackRequested();
    }

    private BackListener backListener;

    public void setBackListener(BackListener listener) {
        this.backListener = listener;
    }

    // Lấy danh sách các loại sản phẩm
    public List<Category> getCategories() {
        return this.categories;
    }
}