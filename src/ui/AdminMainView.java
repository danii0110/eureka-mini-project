package ui;

import dao.PhoneDao;
import dto.Phone;
import ui.dialog.AddPhoneDialog;
import ui.dialog.EditPhoneDialog;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminMainView extends JPanel {
    private MainFrame mainFrame;
    private JTable phoneTable;
    private JButton addButton, editButton, deleteButton, salesHistoryButton, userModeButton;
    private PhoneDao phoneDao;

    public AdminMainView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.phoneDao = new PhoneDao();

        setLayout(new BorderLayout());

        // 테이블 데이터 모델 생성
        String[] columnNames = {"ID", "브랜드", "모델", "가격", "재고"};
        Object[][] data = fetchPhoneData();
        phoneTable = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(phoneTable);

        // 버튼 패널
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("휴대폰 추가");
        editButton = new JButton("수정");
        deleteButton = new JButton("삭제");
        salesHistoryButton = new JButton("판매 내역 조회");
        userModeButton = new JButton("사용자 모드");

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(salesHistoryButton);
        buttonPanel.add(userModeButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // 버튼 이벤트 추가
        addButton.addActionListener(e -> openAddPhoneDialog());
        editButton.addActionListener(e -> openEditPhoneDialog());
        deleteButton.addActionListener(e -> deletePhone());
        salesHistoryButton.addActionListener(e -> mainFrame.showView("SalesHistoryView"));
        userModeButton.addActionListener(e -> mainFrame.showView("UserMainView"));
    }

    // 전체 휴대폰 목록 조회
    private Object[][] fetchPhoneData() {
        List<Phone> phones = phoneDao.getAllPhones();
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

    // 휴대폰 추가 다이얼로그 (추가 후 테이블 갱신)
    private void openAddPhoneDialog() {
        AddPhoneDialog dialog = new AddPhoneDialog(this);
        dialog.setVisible(true);

        // 다이얼로그가 닫힌 후 테이블 새로고침
        refreshTable();
    }

    // 휴대폰 수정 다이얼로그 (수정 후 테이블 갱신)
    private void openEditPhoneDialog() {
        int selectedRow = phoneTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "수정할 휴대폰을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int phoneId = (int) phoneTable.getValueAt(selectedRow, 0);
        Phone phone = phoneDao.getPhoneById(phoneId);

        EditPhoneDialog dialog = new EditPhoneDialog(this, phone);
        dialog.setVisible(true);

        // 다이얼로그가 정상적으로 닫힌 후에만 새로고침
        if (dialog.isConfirmed()) { // isConfirmed()는 다이얼로그에서 수정이 완료되었는지 체크하는 메서드
            refreshTable();
        }
    }

    // 휴대폰 삭제
    private void deletePhone() {
        int selectedRow = phoneTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "삭제할 휴대폰을 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int phoneId = (int) phoneTable.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "정말 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            int result = phoneDao.deletePhone(phoneId);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "휴대폰 삭제 완료!", "성공", JOptionPane.INFORMATION_MESSAGE);
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "삭제 실패. 다시 시도해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // 테이블 새로고침 (removeAll() 제거)
    public void refreshTable() {
        Object[][] newData = fetchPhoneData();
        phoneTable.setModel(new javax.swing.table.DefaultTableModel(newData, new String[]{"ID", "브랜드", "모델", "가격", "재고"}));

        revalidate();
        repaint();
    }
}