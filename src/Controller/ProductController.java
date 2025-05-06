package Controller;

import DAO.CategoryDAO;
import DAO.ProductDAO;
import Model.Category;
import Model.Product;
import View.ProductView;

import javax.swing.*;
import java.util.List;

public class ProductController {
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;
    private ProductView productView;
    private List<Product> products;

    public ProductController(ProductView productView) {
        this.productDAO = new ProductDAO();
        this.categoryDAO = new CategoryDAO();
        this.productView = productView;

        // Load categories for combobox
        loadCategories();

        // Load products
        loadProducts();

        // Setup event listeners
        setupEventListeners();
    }

    // Load categories for combobox
    private void loadCategories() {
        List<Category> categories = categoryDAO.getAllCategories();
        productView.loadCategories(categories);
    }

    // Load products to table
    public void loadProducts() {
        products = productDAO.getAllProducts();
        productView.loadProducts(products);
    }

    // Setup event listeners for UI components
    private void setupEventListeners() {
        // Add product
        productView.setAddButtonListener(e -> {
            addProduct();
        });

        // Update product
        productView.setUpdateButtonListener(e -> {
            updateProduct();
        });

        // Delete product
        productView.setDeleteButtonListener(e -> {
            deleteProduct();
        });

        // Clear form
        productView.setClearButtonListener(e -> {
            productView.clearForm();
        });

        // Back to dashboard
        productView.setBackButtonListener(e -> {
            if (backListener != null) {
                backListener.onBackRequested();
            }
        });

        // Search products
        productView.setSearchButtonListener(e -> {
            searchProducts();
        });

        // Import stock
        productView.setImportButtonListener(e -> {
            importStock();
        });
    }

    // Add new product
    private void addProduct() {
        try {
            Product product = productView.getProductFromForm();

            // Validate input
            if (product.getName() == null || product.getName().trim().isEmpty()) {
                JOptionPane.showMessageDialog(productView, "Vui lòng nhập tên sản phẩm!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (product.getPrice() <= 0) {
                JOptionPane.showMessageDialog(productView, "Giá sản phẩm phải lớn hơn 0!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (product.getStock() < 0) {
                JOptionPane.showMessageDialog(productView, "Số lượng tồn kho không thể âm!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (product.getUnit() <= 0) {
                JOptionPane.showMessageDialog(productView, "Đơn vị phải lớn hơn 0!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check duplicates
            if (productDAO.isProductExists(product.getName())) {
                JOptionPane.showMessageDialog(productView, "Sản phẩm này đã tồn tại!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add to database
            if (productDAO.addProduct(product)) {
                JOptionPane.showMessageDialog(productView, "Thêm sản phẩm thành công!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadProducts();
                productView.clearForm();
            } else {
                JOptionPane.showMessageDialog(productView, "Thêm sản phẩm thất bại!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(productView, "Vui lòng nhập đúng định dạng số!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(productView, "Lỗi: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update existing product
    private void updateProduct() {
        try {
            Product product = productView.getProductFromForm();

            // Check if a product is selected
            if (product.getId() <= 0) {
                JOptionPane.showMessageDialog(productView, "Vui lòng chọn sản phẩm cần cập nhật!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate input
            if (product.getName() == null || product.getName().trim().isEmpty()) {
                JOptionPane.showMessageDialog(productView, "Vui lòng nhập tên sản phẩm!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (product.getPrice() <= 0) {
                JOptionPane.showMessageDialog(productView, "Giá sản phẩm phải lớn hơn 0!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (product.getStock() < 0) {
                JOptionPane.showMessageDialog(productView, "Số lượng tồn kho không thể âm!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (product.getUnit() <= 0) {
                JOptionPane.showMessageDialog(productView, "Đơn vị phải lớn hơn 0!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update in database
            if (productDAO.updateProduct(product)) {
                JOptionPane.showMessageDialog(productView, "Cập nhật sản phẩm thành công!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadProducts();
                productView.clearForm();
            } else {
                JOptionPane.showMessageDialog(productView, "Cập nhật sản phẩm thất bại!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(productView, "Vui lòng nhập đúng định dạng số!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(productView, "Lỗi: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete product
    private void deleteProduct() {
        int productId = productView.getSelectedProductId();

        // Check if a product is selected
        if (productId <= 0) {
            JOptionPane.showMessageDialog(productView, "Vui lòng chọn sản phẩm cần xóa!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm deletion
        int option = JOptionPane.showConfirmDialog(productView, "Bạn có chắc chắn muốn xóa sản phẩm này?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.NO_OPTION) {
            return;
        }

        // Delete from database
        if (productDAO.deleteProduct(productId)) {
            JOptionPane.showMessageDialog(productView, "Xóa sản phẩm thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadProducts();
            productView.clearForm();
        } else {
            JOptionPane.showMessageDialog(productView, "Xóa sản phẩm thất bại! Có thể sản phẩm này đang được sử dụng.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Search products by name
    private void searchProducts() {
        String keyword = productView.getSearchKeyword();

        if (keyword == null || keyword.trim().isEmpty()) {
            loadProducts();
            return;
        }

        List<Product> searchResults = productDAO.searchProductsByName(keyword);
        productView.loadProducts(searchResults);
    }

    // Import stock
    private void importStock() {
        int productId = productView.getSelectedProductId();

        // Check if a product is selected
        if (productId <= 0) {
            JOptionPane.showMessageDialog(productView, "Vui lòng chọn sản phẩm cần nhập kho!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get import quantity
        int quantity = productView.getImportQuantity();

        // Validate quantity
        if (quantity <= 0) {
            JOptionPane.showMessageDialog(productView, "Số lượng nhập kho phải lớn hơn 0!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Update stock
        if (productDAO.updateStock(productId, quantity)) {
            JOptionPane.showMessageDialog(productView, "Nhập kho thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            loadProducts();
            productView.clearForm();
        } else {
            JOptionPane.showMessageDialog(productView, "Nhập kho thất bại!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Interface and listener for back to dashboard
    public interface BackListener {
        void onBackRequested();
    }

    private BackListener backListener;

    public void setBackListener(BackListener listener) {
        this.backListener = listener;
    }

    // Get list of products
    public List<Product> getProducts() {
        return this.products;
    }
}