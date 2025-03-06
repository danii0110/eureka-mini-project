package ui;

import dao.SaleDao;
import dto.Sale;
import dto.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserPurchaseHistoryView extends JPanel {
    private MainFrame mainFrame;
    private JTable purchaseTable;
    private JButton backButton;
    private SaleDao saleDao;

    public UserPurchaseHistoryView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.saleDao = new SaleDao();

        setLayout(new BorderLayout());

        // 테이블 데이터 모델 생성
        String[] columnNames = {"판매 ID", "휴대폰 ID", "구매 날짜"};
        Object[][] data = fetchPurchaseData();
        purchaseTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(purchaseTable);

        // 뒤로 가기 버튼
        backButton = new JButton("← 뒤로 가기");
        backButton.addActionListener(e -> mainFrame.showView("UserMainView"));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(backButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // 현재 로그인된 사용자의 구매 내역 조회
    private Object[][] fetchPurchaseData() {
        User loggedInUser = mainFrame.getLoggedInUser();
        if (loggedInUser == null) {
            JOptionPane.showMessageDialog(this, "로그인이 필요합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return new Object[0][0];
        }

        List<Sale> purchases = saleDao.getSalesByUserId(loggedInUser.getUserId());
        Object[][] data = new Object[purchases.size()][3];

        for (int i = 0; i < purchases.size(); i++) {
            data[i][0] = purchases.get(i).getId();
            data[i][1] = purchases.get(i).getPhoneId();
            data[i][2] = purchases.get(i).getSaleTime();
        }

        return data;
    }
}