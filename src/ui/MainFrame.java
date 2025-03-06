package ui;

import dto.User;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JButton backButton, loginButton, adminModeButton;

    private User currentUser; // 현재 로그인한 사용자 (null이면 비로그인 상태)
    private Stack<String> viewHistory = new Stack<>(); // 이전 화면 스택

    // 화면 식별자
    private static final String USER_MAIN_VIEW = "UserMainView";
    private static final String ADMIN_MAIN_VIEW = "AdminMainView";
    private static final String LOGIN_VIEW = "LoginView";

    public MainFrame() {
        setTitle("휴대폰 판매 관리 시스템");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 레이아웃 설정
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 각 View 추가 (초기 화면은 UserMainView)
        mainPanel.add(new UserMainView(this), USER_MAIN_VIEW);
        mainPanel.add(new AdminMainView(this), ADMIN_MAIN_VIEW);
        mainPanel.add(new LoginView(this), LOGIN_VIEW);

        // 상단 패널 (뒤로 가기, 로그인/로그아웃, 관리자 모드 버튼)
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        backButton = new JButton("<-");
        backButton.addActionListener(e -> navigateBack());

        loginButton = new JButton("로그인");
        loginButton.addActionListener(e -> {
            if (currentUser == null) {
                navigateTo(LOGIN_VIEW);
            } else {
                logout();
            }
        });

        adminModeButton = new JButton("관리자 모드");
        adminModeButton.setVisible(false); // 초기에는 숨김
        adminModeButton.addActionListener(e -> navigateTo(ADMIN_MAIN_VIEW));

        buttonPanel.add(backButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(adminModeButton);
        topPanel.add(buttonPanel, BorderLayout.WEST);

        // 레이아웃 추가
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // 초기 화면 설정
        navigateTo(USER_MAIN_VIEW);
    }

    // 특정 View로 전환
    public void navigateTo(String viewName) {
        if (!viewHistory.isEmpty() && !viewHistory.peek().equals(viewName)) {
            viewHistory.push(viewName);
        }
        cardLayout.show(mainPanel, viewName);
    }

    // 뒤로 가기 기능
    public void navigateBack() {
        if (viewHistory.size() > 1) {
            viewHistory.pop();
            cardLayout.show(mainPanel, viewHistory.peek());
        }
    }

    // 로그인 기능
    public void login(User user) {
        this.currentUser = user;
        loginButton.setText("로그아웃");

        if ("admin".equals(user.getRole())) {
            adminModeButton.setVisible(true); // 관리자 모드 활성화
        }

        navigateTo(USER_MAIN_VIEW); // 로그인 후 사용자 메인 화면으로 이동
    }

    // 로그아웃 기능
    public void logout() {
        this.currentUser = null;
        loginButton.setText("로그인");
        adminModeButton.setVisible(false); // 관리자 모드 비활성화
        navigateTo(USER_MAIN_VIEW); // 로그아웃 후 초기 화면으로 이동
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}