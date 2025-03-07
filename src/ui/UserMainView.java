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
    private List<Phone> availablePhones;

    public UserMainView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.phoneDao = new PhoneDao();
        this.saleDao = new SaleDao();

        setLayout(new BorderLayout());

        // 테이블 데이터 모델 생성
        String[] columnNames = {"브랜드", "모델", "가격"};
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

        // 🔹 구매 내역 조회 버튼 이벤트 (로그인 여부 체크 추가)
        historyButton.addActionListener(e -> {
            User loggedInUser = mainFrame.getLoggedInUser();
            if (loggedInUser == null) {
                JOptionPane.showMessageDialog(this, "로그인이 필요합니다.", "오류", JOptionPane.ERROR_MESSAGE);
                return;
            }
            mainFrame.showView("UserPurchaseHistoryView");
        });
    }

    // 판매 가능한 휴대폰 목록 조회
    private Object[][] fetchPhoneData() {
        availablePhones = phoneDao.getAvailablePhones();
        Object[][] data = new Object[availablePhones.size()][3];

        for (int i = 0; i < availablePhones.size(); i++) {
            data[i][0] = availablePhones.get(i).getBrand();
            data[i][1] = availablePhones.get(i).getModel();
            data[i][2] = availablePhones.get(i).getPrice();
        }

        return data;
    }

    // 구매 기능 (ID 컬럼 삭제 반영)
    private void purchasePhone() {
        int selectedRow = phoneTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "구매할 휴대폰을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ID 없이 List에서 직접 가져옴
        int phoneId = availablePhones.get(selectedRow).getId();
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
        phoneTable.setModel(new javax.swing.table.DefaultTableModel(newData, new String[]{"브랜드", "모델", "가격"}));
        revalidate();
        repaint();
    }
}