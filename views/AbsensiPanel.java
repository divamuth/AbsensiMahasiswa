package views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import models.AbsensiModel;
import controllers.FileHelper;

public class AbsensiPanel extends JPanel {
    // Pastel color scheme for dark mode
    private static final Color BACKGROUND_DARK = new Color(26, 27, 38);
    private static final Color SURFACE_DARK = new Color(32, 33, 44);
    private static final Color INPUT_DARK = new Color(38, 39, 50);
    private static final Color ACCENT_COLOR = new Color(187, 176, 230); 
    private static final Color TEXT_PRIMARY = new Color(236, 236, 241);
    private static final Color TEXT_SECONDARY = new Color(199, 199, 204);
    
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField nimField;
    private JComboBox<String> statusBox;
    private JLabel clockLabel;
    private List<AbsensiModel> absensiList;

    public AbsensiPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(BACKGROUND_DARK);
    
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(BACKGROUND_DARK);
        
        JLabel titleLabel = new JLabel("Pencatatan Absensi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        clockLabel = new JLabel("", SwingConstants.RIGHT);
        clockLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        clockLabel.setForeground(TEXT_SECONDARY);
        updateClock();
        
        Timer timer = new Timer(1000, e -> updateClock());
        timer.start();
    
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(clockLabel, BorderLayout.EAST);
    
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(SURFACE_DARK);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
    
        nimField = new JTextField(15);
        nimField.setPreferredSize(new Dimension(200, 30));
        nimField.setBackground(INPUT_DARK);
        nimField.setForeground(TEXT_PRIMARY);
        nimField.setCaretColor(TEXT_PRIMARY);
        nimField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
        
        String[] statusOptions = {"Hadir", "Izin", "Sakit", "Alpha"};
        statusBox = new JComboBox<>(statusOptions);
        statusBox.setPreferredSize(new Dimension(200, 30));
        statusBox.setBackground(INPUT_DARK);
        statusBox.setForeground(TEXT_PRIMARY);
        ((JComponent) statusBox.getEditor().getEditorComponent()).setBackground(INPUT_DARK); 
        ((JComponent) statusBox.getEditor().getEditorComponent()).setForeground(TEXT_PRIMARY);
        statusBox.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
    
        statusBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (c instanceof JLabel label) {
                    label.setBackground(isSelected ? ACCENT_COLOR : INPUT_DARK); // Latar belakang
                    label.setForeground(TEXT_PRIMARY); // Warna teks terlihat
                    label.setOpaque(true); // Pastikan latar belakang diterapkan
                }
                return c;
            }
        });

        statusBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBackground(INPUT_DARK);
                return button;
            }
        });
        
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel nimLabel = new JLabel("NIM:");
        nimLabel.setFont(new Font("Arial", Font.BOLD, 12));
        nimLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(nimLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(nimField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(statusLabel, gbc);
        
        gbc.gridx = 1;
        formPanel.add(statusBox, gbc);
        
        gbc.gridx = 1; gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        JButton submitButton = createStyledButton("Submit");
        submitButton.addActionListener(e -> submitAbsensi());
        formPanel.add(submitButton, gbc);
    
        // Table
        String[] columns = {"NIM", "Tanggal", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setBackground(SURFACE_DARK);
        table.setForeground(TEXT_PRIMARY);
        table.setGridColor(new Color(58, 59, 70));
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(TEXT_PRIMARY);
        
        // Style the table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(INPUT_DARK);
        header.setForeground(new Color(30, 30, 30));
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(SURFACE_DARK);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BACKGROUND_DARK);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshTable();
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(BACKGROUND_DARK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });
        
        return button;
    }
    
    private void updateClock() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        clockLabel.setText(LocalDateTime.now().format(formatter));
    }
    
    private void submitAbsensi() {
        String nim = nimField.getText().trim();
        if (nim.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "NIM harus diisi!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        AbsensiModel absensi = new AbsensiModel();
        absensi.setNim(nim);
        absensi.setTanggal(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        absensi.setStatus((String) statusBox.getSelectedItem());
    
        absensiList = FileHelper.getAbsensi();
        absensiList.add(absensi);
        FileHelper.saveAbsensi(absensiList);
        
        refreshTable();
        nimField.setText("");
        JOptionPane.showMessageDialog(this, 
            "Absensi berhasil dicatat!", 
            "Sukses", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void refreshTable() {
        absensiList = FileHelper.getAbsensi();
        tableModel.setRowCount(0);
        for (AbsensiModel a : absensiList) {
            tableModel.addRow(new Object[]{a.getNim(), a.getTanggal(), a.getStatus()});
        }
    }
}