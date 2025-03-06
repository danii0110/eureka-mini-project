package ui.dialog;

import dao.PhoneDao;
import dto.Phone;
import ui.AdminMainView;

import javax.swing.*;
import java.awt.*;

public class AddPhoneDialog extends JDialog {
    private JTextField brandField, modelField, priceField, stockField;
    private JButton addButton, cancelButton;
    private PhoneDao phoneDao;
    private AdminMainView parentView;

    public AddPhoneDialog(AdminMainView parentView) {
        this.parentView = parentView;
        this.phoneDao = new PhoneDao();

        setTitle("휴대폰 추가");
        setSize(300, 250);
        setLayout(new GridLayout(5, 2));
        setLocationRelativeTo(parentView);

        // 입력 필드
        brandField = new JTextField();
        modelField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();

        add(new JLabel("브랜드:"));
        add(brandField);
        add(new JLabel("모델:"));
        add(modelField);
        add(new JLabel("가격:"));
        add(priceField);
        add(new JLabel("재고:"));
        add(stockField);

        // 버튼 추가
        addButton = new JButton("추가");
        cancelButton = new JButton("취소");

        add(addButton);
        add(cancelButton);

        // 버튼 이벤트
        addButton.addActionListener(e -> addPhone());
        cancelButton.addActionListener(e -> dispose());
    }

    private void addPhone() {
        String brand = brandField.getText();
        String model = modelField.getText();
        int price, stock;

        try {
            price = Integer.parseInt(priceField.getText());
            stock = Integer.parseInt(stockField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "가격과 재고는 숫자로 입력해야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (brand.isEmpty() || model.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Phone phone = new Phone(0, brand, model, price, stock);
        int result = phoneDao.createPhone(phone);

        if (result > 0) {
            JOptionPane.showMessageDialog(this, "휴대폰이 추가되었습니다.", "성공", JOptionPane.INFORMATION_MESSAGE);
            parentView.refreshTable(); // 테이블 새로고침
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "추가 실패. 다시 시도하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}