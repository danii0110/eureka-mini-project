package ui;

import common.PasswordUtil;
import dao.UserDao;
import dto.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class RegisterView extends JPanel {
    private JTextField nameField, contactField, emailField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private MainFrame mainFrame;
    private UserDao userDao;

    public RegisterView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.userDao = new UserDao();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel nameLabel = new JLabel("이름:");
        JLabel contactLabel = new JLabel("연락처:");
        JLabel emailLabel = new JLabel("이메일:");
        JLabel passwordLabel = new JLabel("비밀번호:");

        nameField = new JTextField(15);
        contactField = new JTextField(15);
        emailField = new JTextField(15);
        passwordField = new JPasswordField(15);

        registerButton = new JButton("가입하기");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);
        gbc.gridx = 1;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(contactLabel, gbc);
        gbc.gridx = 1;
        add(contactField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(emailLabel, gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(registerButton, gbc);

        // 회원가입 버튼 클릭 이벤트
        registerButton.addActionListener(e -> register());
    }

    // 회원가입 처리 메서드
    private void register() {
        String name = nameField.getText();
        String contact = contactField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "회원가입 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User existingUser = userDao.getUserByEmail(email);
        if (existingUser != null) {
            JOptionPane.showMessageDialog(this, "이미 가입된 이메일입니다.", "회원가입 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 비밀번호 해싱 추가
        String hashedPassword = PasswordUtil.hashPassword(password);

        // 해싱된 비밀번호 저장
        User newUser = new User(0, name, contact, email, hashedPassword, new Timestamp(System.currentTimeMillis()), "user");
        int result = userDao.createUser(newUser);

        if (result > 0) {
            JOptionPane.showMessageDialog(this, "회원가입 성공! 로그인 해주세요.", "회원가입 완료", JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            mainFrame.showView("LoginView");
        } else {
            JOptionPane.showMessageDialog(this, "회원가입 실패. 다시 시도하세요.", "회원가입 오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 입력 필드 초기화 메서드 추가
    private void clearFields() {
        nameField.setText("");
        contactField.setText("");
        emailField.setText("");
        passwordField.setText("");
    }
}