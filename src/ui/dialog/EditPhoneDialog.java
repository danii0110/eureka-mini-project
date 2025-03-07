package ui.dialog;

import dao.PhoneDao;
import dto.Phone;
import ui.AdminMainView;

import javax.swing.*;
import java.awt.*;

public class EditPhoneDialog extends JDialog {
    private JTextField brandField, modelField, priceField, stockField;
    private JButton updateButton, cancelButton;
    private PhoneDao phoneDao;
    private AdminMainView parentView;
    private int phoneId;
    private boolean isConfirmed = false; // 수정 완료 여부 확인을 위한 필드 추가

    public EditPhoneDialog(AdminMainView parentView, Phone phone) {
        this.parentView = parentView;
        this.phoneDao = new PhoneDao();
        this.phoneId = phone.getId();

        setTitle("휴대폰 수정");
        setSize(300, 250);
        setLayout(new GridLayout(5, 2));
        setLocationRelativeTo(parentView);

        // 입력 필드 (기존 값 설정)
        brandField = new JTextField(phone.getBrand());
        modelField = new JTextField(phone.getModel());
        priceField = new JTextField(String.valueOf(phone.getPrice()));
        stockField = new JTextField(String.valueOf(phone.getStock()));

        add(new JLabel("브랜드:"));
        add(brandField);
        add(new JLabel("모델:"));
        add(modelField);
        add(new JLabel("가격:"));
        add(priceField);
        add(new JLabel("재고:"));
        add(stockField);

        // 버튼 추가
        updateButton = new JButton("수정");
        cancelButton = new JButton("취소");

        add(updateButton);
        add(cancelButton);

        // 버튼 이벤트
        updateButton.addActionListener(e -> updatePhone());
        cancelButton.addActionListener(e -> dispose());
    }

    private void updatePhone() {
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

        Phone phone = new Phone(phoneId, brand, model, price, stock);
        int result = phoneDao.updatePhone(phone);

        if (result > 0) {
            JOptionPane.showMessageDialog(this, "수정 완료!", "성공", JOptionPane.INFORMATION_MESSAGE);
            isConfirmed = true; // 수정이 정상적으로 완료된 경우 true 설정
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "수정 실패. 다시 시도하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 다이얼로그가 닫힌 후 수정 여부를 확인할 수 있도록 추가
    public boolean isConfirmed() {
        return isConfirmed;
    }
}