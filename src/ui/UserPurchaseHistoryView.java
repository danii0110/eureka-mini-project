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
    private String[] columnNames = {"판매 ID", "휴대폰 ID", "구매 날짜"};
    private Object[][] data;
    private DefaultTableModel tableModel;

    public UserPurchaseHistoryView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.saleDao = new SaleDao();

        setLayout(new BorderLayout());

        // 초기 테이블 생성 (빈 데이터)
        data = new Object[0][3];
        tableModel = new DefaultTableModel(data, columnNames);
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

    // 화면이 열릴 때마다 최신 데이터를 가져오도록 설정
    public void refreshTable() {
        User loggedInUser = mainFrame.getLoggedInUser();
        if (loggedInUser == null) {
            JOptionPane.showMessageDialog(this, "로그인이 필요합니다.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Sale> purchases = saleDao.getSalesByUserId(loggedInUser.getUserId());

        // 디버깅: 실제 가져온 데이터 확인
        System.out.println("구매 내역 개수: " + purchases.size());
        for (Sale sale : purchases) {
            System.out.println(sale.getId() + " - " + sale.getPhoneId() + " - " + sale.getSaleTime());
        }

        // 테이블 업데이트
        Object[][] newData = new Object[purchases.size()][3];
        for (int i = 0; i < purchases.size(); i++) {
            newData[i][0] = purchases.get(i).getId();
            newData[i][1] = purchases.get(i).getPhoneId();
            newData[i][2] = purchases.get(i).getSaleTime();
        }

        tableModel.setDataVector(newData, columnNames);
    }
}