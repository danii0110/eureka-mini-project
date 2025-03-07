package ui;

import dao.SaleDao;
import dto.Sale;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SalesHistoryView extends JPanel {
    private MainFrame mainFrame;
    private JTable salesTable;
    private JButton backButton;
    private SaleDao saleDao;

    public SalesHistoryView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.saleDao = new SaleDao();

        setLayout(new BorderLayout());

        // 테이블 데이터 모델 생성
        String[] columnNames = {"판매 ID", "사용자 ID", "휴대폰 ID", "판매 날짜"};
        Object[][] data = fetchSalesData();
        salesTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(salesTable);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        backButton = new JButton("뒤로 가기");

        buttonPanel.add(backButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 뒤로 가기 버튼 이벤트
        backButton.addActionListener(e -> mainFrame.showView("AdminMainView"));
    }

    // 판매 내역 조회
    private Object[][] fetchSalesData() {
        List<Sale> sales = saleDao.getAllSales();
        Object[][] data = new Object[sales.size()][4];

        for (int i = 0; i < sales.size(); i++) {
            data[i][0] = sales.get(i).getId();
            data[i][1] = sales.get(i).getUserId();
            data[i][2] = sales.get(i).getPhoneId();
            data[i][3] = sales.get(i).getSaleTime();
        }

        return data;
    }
}