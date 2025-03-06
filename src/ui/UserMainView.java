package ui;

import dao.PhoneDao;
import dto.Phone;
import dao.SaleDao;
import dto.User;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserMainView extends JPanel {
    private MainFrame mainFrame;
    private JTable phoneTable;
    private JButton purchaseButton, historyButton;
    private PhoneDao phoneDao;
    private SaleDao saleDao;

    public UserMainView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.phoneDao = new PhoneDao();
        this.saleDao = new SaleDao();

        setLayout(new BorderLayout());

        // 테이블 데이터 모델 생성
        String[] columnNames = {"ID", "브랜드", "모델", "가격", "재고"};
        Object[][] data = fetchPhoneData();
        phoneTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(phoneTable);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        purchaseButton = new JButton("구매하기");
        historyButton = new JButton("구매 내역 조회");

        buttonPanel.add(purchaseButton);
        buttonPanel.add(historyButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 구매 버튼 이벤트
        purchaseButton.addActionListener(e -> purchasePhone());

        // 구매 내역 조회 버튼 이벤트
        historyButton.addActionListener(e -> mainFrame.showView("UserPurchaseHistoryView"));
    }

    // 판매 가능한 휴대폰 목록 조회
    private Object[][] fetchPhoneData() {
        List<Phone> phones = phoneDao.getAvailablePhones();
        Object[][] data = new Object[phones.size()][5];

        for (int i = 0; i < phones.size(); i++) {
            data[i][0] = phones.get(i).getId();
            data[i][1] = phones.get(i).getBrand();
            data[i][2] = phones.get(i).getModel();
            data[i][3] = phones.get(i).getPrice();
            data[i][4] = phones.get(i).getStock();
        }

        return data;
    }

    // 구매 기능
    private void purchasePhone() {
        int selectedRow = phoneTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "구매할 휴대폰을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int phoneId = (int) phoneTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "이 제품을 구매하시겠습니까?", "구매 확인", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            User loggedInUser = mainFrame.getLoggedInUser();
            if (loggedInUser == null) {
                JOptionPane.showMessageDialog(this, "로그인이 필요합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int result = saleDao.createSale(loggedInUser.getUserId(), phoneId);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "구매 완료!", "성공", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "구매 실패. 다시 시도해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 테이블 새로고침
    private void refreshTable() {
        removeAll();
        Object[][] newData = fetchPhoneData();
        phoneTable.setModel(new javax.swing.table.DefaultTableModel(newData, new String[]{"ID", "브랜드", "모델", "가격", "재고"}));
        revalidate();
        repaint();
    }
}