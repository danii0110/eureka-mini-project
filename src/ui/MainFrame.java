package ui;

import dao.UserDao;
import dto.User;

import javax.swing.*;
import java.awt.*;
import java.util.Stack;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JButton backButton;
    private JButton loginLogoutButton;
    private boolean isLoggedIn = false;
    private boolean isAdmin = false;
    private User loggedInUser; // 로그인된 사용자 정보
    private Stack<String> viewHistory = new Stack<>();

    public MainFrame() {
        setTitle("휴대폰 판매 관리 시스템");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 화면 추가
        mainPanel.add(new UserMainView(this), "UserMainView");
        mainPanel.add(new LoginView(this), "LoginView");
        mainPanel.add(new RegisterView(this), "RegisterView");
        mainPanel.add(new AdminMainView(this), "AdminMainView");
        mainPanel.add(new UserPurchaseHistoryView(this), "UserPurchaseHistoryView");
        mainPanel.add(new SalesHistoryView(this), "SalesHistoryView");

        add(mainPanel, BorderLayout.CENTER);

        // 상단 네비게이션 패널
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        backButton = new JButton("←");
        loginLogoutButton = new JButton("로그인");

        topPanel.add(backButton);
        topPanel.add(loginLogoutButton);
        add(topPanel, BorderLayout.NORTH);

        // 버튼 이벤트 추가
        backButton.addActionListener(e -> goBack());
        loginLogoutButton.addActionListener(e -> toggleLoginLogout());

        // 초기 화면 설정
        showView("UserMainView");
    }

    // 화면 전환 메서드 (중복 추가 방지)
    public void showView(String viewName) {
        if (viewHistory.isEmpty() || !viewHistory.peek().equals(viewName)) {
            viewHistory.push(viewName);
        }
        cardLayout.show(mainPanel, viewName);
    }

    // 뒤로 가기 메서드 (예외 방지)
    private void goBack() {
        if (viewHistory.size() > 1) {
            viewHistory.pop(); // 현재 화면 제거
            String previousView = viewHistory.peek(); // 이전 화면 가져오기
            cardLayout.show(mainPanel, previousView);
        }
    }

    // 로그인/로그아웃 버튼 동작
    private void toggleLoginLogout() {
        if (isLoggedIn) {
            isLoggedIn = false;
            isAdmin = false;
            loggedInUser = null; // 사용자 정보 초기화
            loginLogoutButton.setText("로그인");
            showView("UserMainView");
        } else {
            showView("LoginView");
        }
    }

    // 로그인 성공 시 호출될 메서드
    public void loginSuccess(User user) {
        isLoggedIn = true;
        isAdmin = user.getRole().equals("admin");
        loggedInUser = user; // 로그인한 사용자 정보 저장

        loginLogoutButton.setText("로그아웃");

        if (isAdmin) {
            showView("AdminMainView");
        } else {
            showView("UserMainView");
        }
    }

    // 현재 로그인된 사용자 반환
    public User getLoggedInUser() {
        return loggedInUser;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}