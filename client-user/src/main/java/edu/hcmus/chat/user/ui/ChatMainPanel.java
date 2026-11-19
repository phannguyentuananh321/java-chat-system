package edu.hcmus.chat.user.ui;

import edu.hcmus.chat.common.ui.Theme;
import edu.hcmus.chat.user.service.ChatService;
import edu.hcmus.chat.user.service.UserServiceFactory;
import edu.hcmus.chat.user.ui.components.ChatBubblePanel;
import edu.hcmus.chat.user.ui.components.ConversationListCellRenderer;
import edu.hcmus.chat.user.ui.DummyUserData.DummyContact;
import edu.hcmus.chat.user.ui.DummyUserData.DummyMessage;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.List;

/**
 * Màn hình: Khung chat chính (Phân hệ Người dùng).
 * Cấu trúc 3 phân vùng chuẩn:
 * - Bên trái (WEST): Danh sách cuộc trò chuyện, tìm kiếm, nút tạo nhóm, nút quản lý bạn bè.
 * - Ở giữa (CENTER): Vùng hiển thị bong bóng chat (100% Java Swing thuần, Zero HTML).
 * - Phía dưới (SOUTH): Thanh soạn thảo và gửi tin nhắn.
 *
 * Người phụ trách: Thành viên B (Người dùng).
 */
public class ChatMainPanel extends JPanel {

    private final ChatService chatService = UserServiceFactory.getChatService();

    private DefaultListModel<DummyContact> contactListModel;
    private JList<DummyContact> contactList;

    private JLabel activeChatTitleLabel;
    private JLabel activeChatStatusLabel;
    private JPanel messageListContainer;
    private JScrollPane messageScrollPane;
    private JTextField messageInputField;

    private DummyContact currentActiveContact;

    public ChatMainPanel() {
        initComponents();
        loadConversations();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_MAIN);

        // 1. Phân vùng bên trái (WEST): Chiều rộng 340px
        add(createWestSidebar(), BorderLayout.WEST);

        // 2. Phân vùng ở giữa & dưới: Khung chat chính
        add(createChatArea(), BorderLayout.CENTER);
    }

    private JPanel createWestSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout(0, 10));
        sidebar.setPreferredSize(new Dimension(340, 0));
        sidebar.setBackground(Theme.BG_SURFACE);
        sidebar.setBorder(new CompoundBorder(
                new LineBorder(Theme.BORDER_COLOR, 1, false),
                new EmptyBorder(12, 10, 10, 10)
        ));

        // Header trên cùng của Sidebar: Tiêu đề + Các nút tiện ích
        JPanel topPanel = new JPanel(new BorderLayout(0, 8));
        topPanel.setOpaque(false);

        JLabel appTitle = new JLabel("Đoạn chat");
        appTitle.setFont(Theme.FONT_TITLE);
        appTitle.setForeground(Theme.TEXT_DARK);

        JPanel headerButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        headerButtons.setOpaque(false);

        JButton btnNewGroup = new JButton("+ Nhóm");
        btnNewGroup.setFont(Theme.FONT_REGULAR);
        btnNewGroup.setFocusPainted(false);
        btnNewGroup.addActionListener(e -> {
            Frame topFrame = (Frame) SwingUtilities.getWindowAncestor(this);
            CreateGroupDialog dialog = new CreateGroupDialog(topFrame);
            dialog.setVisible(true);
            if (dialog.isGroupCreated()) {
                loadConversations();
            }
        });

        JButton btnFriends = new JButton("Bạn bè");
        btnFriends.setFont(Theme.FONT_REGULAR);
        btnFriends.setFocusPainted(false);
        btnFriends.addActionListener(e -> {
            JFrame friendFrame = new JFrame("Quản lý bạn bè & Lời mời kết bạn");
            friendFrame.setSize(850, 550);
            friendFrame.setLocationRelativeTo(this);
            friendFrame.setContentPane(new FriendManagementPanel());
            friendFrame.setVisible(true);
        });

        headerButtons.add(btnNewGroup);
        headerButtons.add(btnFriends);

        topPanel.add(appTitle, BorderLayout.WEST);
        topPanel.add(headerButtons, BorderLayout.EAST);

        // Ô tìm kiếm cuộc trò chuyện
        JTextField searchField = new JTextField();
        searchField.setFont(Theme.FONT_REGULAR);
        searchField.putClientProperty("JTextField.placeholderText", "Tìm kiếm bạn bè, nhóm...");
        searchField.putClientProperty("JTextField.showClearButton", true);
        topPanel.add(searchField, BorderLayout.SOUTH);

        sidebar.add(topPanel, BorderLayout.NORTH);

        // Danh sách các cuộc trò chuyện (JList + Custom Renderer)
        contactListModel = new DefaultListModel<>();
        contactList = new JList<>(contactListModel);
        contactList.setCellRenderer(new ConversationListCellRenderer());
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                DummyContact selected = contactList.getSelectedValue();
                if (selected != null) {
                    selectConversation(selected);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(contactList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        sidebar.add(scrollPane, BorderLayout.CENTER);

        return sidebar;
    }

    private JPanel createChatArea() {
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.setBackground(Theme.BG_MAIN);

        // 1. Header cuộc trò chuyện đang mở (NORTH)
        JPanel chatHeader = new JPanel(new BorderLayout(10, 0));
        chatHeader.setPreferredSize(new Dimension(0, 60));
        chatHeader.setBackground(Theme.BG_SURFACE);
        chatHeader.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR),
                new EmptyBorder(8, 20, 8, 20)
        ));

        JPanel titleContainer = new JPanel(new GridLayout(2, 1, 0, 2));
        titleContainer.setOpaque(false);

        activeChatTitleLabel = new JLabel("Chọn cuộc trò chuyện");
        activeChatTitleLabel.setFont(Theme.FONT_SUBTITLE);
        activeChatTitleLabel.setForeground(Theme.TEXT_DARK);

        activeChatStatusLabel = new JLabel("Đang ngoại tuyến");
        activeChatStatusLabel.setFont(Theme.FONT_SMALL);
        activeChatStatusLabel.setForeground(Theme.TEXT_MUTED);

        titleContainer.add(activeChatTitleLabel);
        titleContainer.add(activeChatStatusLabel);
        chatHeader.add(titleContainer, BorderLayout.WEST);

        chatPanel.add(chatHeader, BorderLayout.NORTH);

        // 2. Vùng hiển thị tin nhắn (CENTER)
        messageListContainer = new JPanel();
        messageListContainer.setLayout(new BoxLayout(messageListContainer, BoxLayout.Y_AXIS));
        messageListContainer.setBackground(Theme.BG_MAIN);
        messageListContainer.setBorder(new EmptyBorder(10, 10, 10, 10));

        messageScrollPane = new JScrollPane(messageListContainer);
        messageScrollPane.setBorder(BorderFactory.createEmptyBorder());
        messageScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        chatPanel.add(messageScrollPane, BorderLayout.CENTER);

        // 3. Thanh nhập liệu tin nhắn (SOUTH)
        JPanel inputBar = new JPanel(new BorderLayout(10, 0));
        inputBar.setPreferredSize(new Dimension(0, 65));
        inputBar.setBackground(Theme.BG_SURFACE);
        inputBar.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Theme.BORDER_COLOR),
                new EmptyBorder(10, 20, 10, 20)
        ));

        messageInputField = new JTextField();
        messageInputField.setFont(Theme.FONT_REGULAR);
        messageInputField.putClientProperty("JTextField.placeholderText", "Nhập tin nhắn của bạn (Enter để gửi)...");
        messageInputField.addActionListener(e -> handleSendMessage());

        JButton btnSend = Theme.createPrimaryButton("Gửi");
        btnSend.addActionListener(e -> handleSendMessage());

        inputBar.add(messageInputField, BorderLayout.CENTER);
        inputBar.add(btnSend, BorderLayout.EAST);

        chatPanel.add(inputBar, BorderLayout.SOUTH);
        return chatPanel;
    }

    private void loadConversations() {
        contactListModel.clear();
        List<DummyContact> conversations = chatService.getConversations();
        for (DummyContact c : conversations) {
            contactListModel.addElement(c);
        }

        if (!contactListModel.isEmpty()) {
            contactList.setSelectedIndex(0);
        }
    }

    private void selectConversation(DummyContact contact) {
        this.currentActiveContact = contact;
        activeChatTitleLabel.setText(contact.name());

        if (contact.isGroup()) {
            activeChatStatusLabel.setText("Nhóm trò chuyện • 4 thành viên");
            activeChatStatusLabel.setForeground(Theme.PRIMARY_COLOR);
        } else if (contact.isOnline()) {
            activeChatStatusLabel.setText("● Đang hoạt động");
            activeChatStatusLabel.setForeground(Theme.SUCCESS_COLOR);
        } else {
            activeChatStatusLabel.setText("○ Ngoại tuyến");
            activeChatStatusLabel.setForeground(Theme.TEXT_MUTED);
        }

        // Tải danh sách bong bóng tin nhắn riêng cho cuộc trò chuyện này
        messageListContainer.removeAll();
        List<DummyMessage> messages = chatService.getMessages(contact.conversationId());
        for (DummyMessage msg : messages) {
            ChatBubblePanel bubble = new ChatBubblePanel(
                    msg.senderName(),
                    msg.content(),
                    msg.timestamp(),
                    msg.isMe()
            );
            messageListContainer.add(bubble);
        }

        messageListContainer.revalidate();
        messageListContainer.repaint();

        // Tự động cuộn xuống tin nhắn mới nhất
        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = messageScrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }

    private void handleSendMessage() {
        String content = messageInputField.getText().trim();
        if (content.isEmpty() || currentActiveContact == null) {
            return;
        }

        // Gửi qua Service để lưu vào bộ nhớ phiên của hội thoại này
        chatService.sendMessage(currentActiveContact.conversationId(), content);
        messageInputField.setText("");

        // Tải lại tin nhắn của cuộc trò chuyện đang chọn
        selectConversation(currentActiveContact);
    }
}
