package ui;

import dao.SaleDao;
import dto.Sale;
import dto.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserPurchaseHistoryView extends JPanel {
    private MainFrame mainFrame;
    private JTable purchaseTable;
    private JButton backButton;
    private SaleDao saleDao;
    private String[] columnNames = {"브랜드", "모델", "가격", "구매 날짜"};
    private DefaultTableModel tableModel;

    public UserPurchaseHistoryView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.saleDao = new SaleDao();

        setLayout(new BorderLayout());

        // 초기 테이블 생성 (빈 데이터)
        tableModel = new DefaultTableModel(new Object[0][4], columnNames);
        purchaseTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(purchaseTable);

        // 뒤로 가기 버튼
        backButton = new JButton("← 뒤로 가기");
        backButton.addActionListener(e -> mainFrame.showView("UserMainView"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // 최신 구매 내역을 불러와 테이블 갱신
    public void refreshTable() {
        User loggedInUser = mainFrame.getLoggedInUser();
        if (loggedInUser == null) {
            JOptionPane.showMessageDialog(this, "로그인이 필요합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Sale> purchases = saleDao.getSalesByUserId(loggedInUser.getUserId());

        Object[][] newData = new Object[purchases.size()][4];
        for (int i = 0; i < purchases.size(); i++) {
            newData[i][0] = purchases.get(i).getBrand();
            newData[i][1] = purchases.get(i).getModel();
            newData[i][2] = purchases.get(i).getPrice();
            newData[i][3] = purchases.get(i).getSaleTime();
        }

        tableModel.setDataVector(newData, columnNames);
    }
}