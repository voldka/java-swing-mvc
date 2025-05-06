package Controller;

import DAO.HistoryDAO;
import Model.History;
import View.HistoryView;

import java.util.List;

public class HistoryController {
    private HistoryDAO historyDAO;
    private HistoryView historyView;

    public HistoryController(HistoryView historyView) {
        this.historyDAO = new HistoryDAO();
        this.historyView = historyView;

        // Thiết lập action listener cho các nút
        this.historyView.setSearchButtonListener(e -> searchHistory());
        this.historyView.setRefreshButtonListener(e -> loadAllHistory());
        this.historyView.setBackButtonListener(e -> fireBackEvent());
    }

    // Tải toàn bộ lịch sử nhập kho
    public void loadAllHistory() {
        List<History> historyList = historyDAO.getAllHistory();
        historyView.loadHistory(historyList);
    }

    /**
     * Method called from MainFrame to load history data
     * Add debug output to verify this is being called
     */
    public void loadHistory() {
        loadAllHistory();
    }

    // Tìm kiếm lịch sử nhập kho theo tên sản phẩm
    private void searchHistory() {
        String keyword = historyView.getSearchKeyword();
        if (keyword.isEmpty()) {
            loadAllHistory(); // Nếu từ khóa trống, tải lại toàn bộ lịch sử
        } else {
            List<History> historyList = historyDAO.searchHistoryByProductName(keyword);
            loadAllHistory();
        }
    }

    // Interface cho sự kiện quay lại
    public interface BackListener {
        void onBack();
    }

    private BackListener backListener;

    // Thiết lập listener
    public void setBackListener(BackListener listener) {
        this.backListener = listener;
    }

    // Kích hoạt sự kiện quay lại
    private void fireBackEvent() {
        if (backListener != null) {
            backListener.onBack();
        }
    }
}