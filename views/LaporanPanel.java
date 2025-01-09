package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import models.AbsensiModel;
import controllers.FileHelper;

public class LaporanPanel extends JPanel {
    private static final Color BACKGROUND_DARK = new Color(26, 27, 38);
    private static final Color SURFACE_DARK = new Color(32, 33, 44);
    private static final Color INPUT_DARK = new Color(38, 39, 50); 
    private static final Color ACCENT_COLOR = new Color(187, 176, 230);
    private static final Color TEXT_PRIMARY = new Color(236, 236, 241);
    private static final Color TEXT_SECONDARY = new Color(199, 199, 204);

    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> filterBox;
    private JComboBox<String> statusBox;
    private JTextField searchField;
    private List<AbsensiModel> absensiList;

    public LaporanPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(BACKGROUND_DARK);

        // Title Panel
        JLabel titleLabel = new JLabel("Laporan Absensi", SwingConstants.CENTER);
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterPanel.setBackground(SURFACE_DARK);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        String[] filterOptions = {"Semua", "Hari Ini", "Minggu Ini", "Bulan Ini"};
        filterBox = new JComboBox<>(filterOptions);
        styleComboBox(filterBox);
        filterBox.addActionListener(e -> filterData());

        String[] statusOptions = {"Semua", "Hadir", "Izin", "Alpha", "Sakit"};
        statusBox = new JComboBox<>(statusOptions);
        styleComboBox(statusBox);
        statusBox.addActionListener(e -> filterData());

        searchField = new JTextField(15);
        searchField.setBackground(INPUT_DARK);
        searchField.setForeground(TEXT_PRIMARY);
        searchField.setCaretColor(TEXT_PRIMARY);
        searchField.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
        searchField.addActionListener(e -> filterData());

        JLabel[] labels = {
            new JLabel("Filter Tanggal: "),
            new JLabel("Status: "),
            new JLabel("Cari NIM: ")
        };
        for (JLabel label : labels) {
            label.setForeground(TEXT_PRIMARY);
            label.setFont(new Font("Arial", Font.BOLD, 12));
        }

        filterPanel.add(labels[0]);
        filterPanel.add(filterBox);
        filterPanel.add(labels[1]);
        filterPanel.add(statusBox);
        filterPanel.add(labels[2]);
        filterPanel.add(searchField);

        // Table Panel
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
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        refreshTable();
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(INPUT_DARK);
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setRenderer(createDarkRenderer());
        
        comboBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = super.createArrowButton();
                button.setBackground(INPUT_DARK);
                button.setForeground(TEXT_PRIMARY);
                return button;
            }
        });
    }

    private DefaultListCellRenderer createDarkRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? ACCENT_COLOR : INPUT_DARK);
                setForeground(TEXT_PRIMARY);
                setOpaque(true);
                return this;
            }
        };
    }

    private void filterData() {
        String filter = (String) filterBox.getSelectedItem();
        String statusFilter = (String) statusBox.getSelectedItem();
        String searchText = searchField.getText().trim().toLowerCase();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        tableModel.setRowCount(0);
        for (AbsensiModel a : absensiList) {
            LocalDateTime absensiDate = LocalDateTime.parse(a.getTanggal(), formatter);
            boolean includeDate = switch (filter) {
                case "Hari Ini" -> absensiDate.toLocalDate().equals(now.toLocalDate());
                case "Minggu Ini" -> {
                    int weekAbs = absensiDate.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
                    int weekNow = now.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
                    yield weekAbs == weekNow && absensiDate.getYear() == now.getYear();
                }
                case "Bulan Ini" -> absensiDate.getMonth() == now.getMonth() && 
                                   absensiDate.getYear() == now.getYear();
                default -> true;
            };

            boolean includeStatus = statusFilter.equals("Semua") || a.getStatus().equalsIgnoreCase(statusFilter);
            boolean includeSearch = searchText.isEmpty() || a.getNim().toLowerCase().contains(searchText);

            if (includeDate && includeStatus && includeSearch) {
                tableModel.addRow(new Object[]{a.getNim(), a.getTanggal(), a.getStatus()});
            }
        }
    }

    private void refreshTable() {
        absensiList = FileHelper.getAbsensi();
        filterData();
    }
}