package ui;

import common.PasswordUtil;
import dao.UserDao;
import dto.User;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JPanel {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton, registerButton;
    private MainFrame mainFrame;
    private UserDao userDao;

    public LoginView(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.userDao = new UserDao();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel emailLabel = new JLabel("이메일:");
        JLabel passwordLabel = new JLabel("비밀번호:");

        emailField = new JTextField(15);
        passwordField = new JPasswordField(15);

        loginButton = new JButton("로그인");
        registerButton = new JButton("회원가입");

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(emailLabel, gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passwordLabel, gbc);
        gbc.gridx = 1;
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(loginButton, gbc);

        gbc.gridy = 3;
        add(registerButton, gbc);

        // 로그인 버튼 클릭 이벤트
        loginButton.addActionListener(e -> login());

        // 회원가입 버튼 클릭 이벤트 (회원가입 뷰로 이동)
        registerButton.addActionListener(e -> mainFrame.showView("RegisterView"));
    }

    private void login() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이메일과 비밀번호를 입력하세요.", "로그인 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = userDao.getUserByEmail(email);

        if (user == null) {
            JOptionPane.showMessageDialog(this, "이메일 또는 비밀번호가 잘못되었습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 디버깅용 출력
        System.out.println("입력한 비밀번호: " + password);
        System.out.println("DB에서 가져온 해시 비밀번호: " + user.getPassword());

        if (!PasswordUtil.checkPassword(password, user.getPassword())) {
            JOptionPane.showMessageDialog(this, "이메일 또는 비밀번호가 잘못되었습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "로그인 성공!", "로그인", JOptionPane.INFORMATION_MESSAGE);
        mainFrame.loginSuccess(user);
    }

}