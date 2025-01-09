package views;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import models.MahasiswaModel;
import controllers.FileHelper;

public class MahasiswaPanel extends JPanel {
    // Constants
    private static final String TITLE = "Data Mahasiswa";
    private static final String[] TABLE_COLUMNS = {"NIM", "Nama", "Kelas"};
    private static final Dimension TEXT_FIELD_SIZE = new Dimension(200, 25);
    
    // Theme colors
    private static final class Theme {
        static final Color BACKGROUND_DARK = new Color(26, 27, 38);
        static final Color SURFACE_DARK = new Color(32, 33, 44);
        static final Color INPUT_DARK = new Color(38, 39, 50);
        static final Color ACCENT = new Color(187, 176, 230);
        static final Color SUCCESS = new Color(152, 195, 121);
        static final Color WARNING = new Color(229, 192, 123);
        static final Color DANGER = new Color(224, 108, 117);
        static final Color TEXT_PRIMARY = new Color(236, 236, 241);
        static final Color TEXT_SECONDARY = new Color(199, 199, 204);
        static final Color GRID = new Color(58, 59, 70);
    }

    // UI Components
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField nimField;
    private final JTextField namaField;
    private final JTextField kelasField;
    private final JButton addButton;
    private final JButton deleteButton;
    private final JButton updateButton;
    private final JButton clearButton;
    private final JButton searchButton;
    private final JTextField searchField;

    private List<MahasiswaModel> mahasiswaList;
    private int selectedRow = -1;

    public MahasiswaPanel() {
        setupPanel();
        
        // Initialize components
        searchField = createStyledTextField();
        nimField = createStyledTextField();
        namaField = createStyledTextField();
        kelasField = createStyledTextField();
        
        tableModel = createTableModel();
        table = createTable();
        
        // Create buttons
        addButton = createStyledButton("Tambah", Theme.SUCCESS);
        updateButton = createStyledButton("Perbarui", Theme.WARNING);
        deleteButton = createStyledButton("Hapus", Theme.DANGER);
        clearButton = createStyledButton("Clear", Theme.ACCENT);
        searchButton = createStyledButton("Cari", Theme.ACCENT);
        
        // Build layout
        JPanel headerPanel = createHeaderPanel();
        JPanel formPanel = createFormPanel();
        JScrollPane scrollPane = createTableScrollPane();
        
        // Main layout
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(Theme.BACKGROUND_DARK);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        setupEventListeners();
        initializeState();
    }

    private void setupPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(Theme.BACKGROUND_DARK);
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(TEXT_FIELD_SIZE);
        field.setBackground(Theme.INPUT_DARK);
        field.setForeground(Theme.TEXT_PRIMARY);
        field.setCaretColor(Theme.TEXT_PRIMARY);
        field.setBorder(BorderFactory.createLineBorder(Theme.ACCENT, 1));
        return field;
    }

    private DefaultTableModel createTableModel() {
        return new DefaultTableModel(TABLE_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTable createTable() {
        JTable table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setBackground(Theme.SURFACE_DARK);
        table.setForeground(Theme.TEXT_PRIMARY);
        table.setGridColor(Theme.GRID);
        table.setSelectionBackground(Theme.ACCENT);
        table.setSelectionForeground(Theme.TEXT_PRIMARY);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.INPUT_DARK);
        header.setForeground(new Color(30, 30, 30));
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBorder(BorderFactory.createLineBorder(Theme.ACCENT, 1));
        
        return table;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(Theme.BACKGROUND_DARK);
        
        // Title on left
        JLabel titleLabel = new JLabel(TITLE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Theme.TEXT_PRIMARY);
        
        // Search on right
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setBackground(Theme.BACKGROUND_DARK);
        
        searchField.setPreferredSize(new Dimension(150, 25));
        searchButton.setPreferredSize(new Dimension(60, 25));
        
        JLabel searchLabel = new JLabel("Cari: ");
        searchLabel.setForeground(Theme.TEXT_PRIMARY);
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Theme.SURFACE_DARK);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.ACCENT, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Add form fields
        addFormRow(formPanel, "NIM:", nimField, gbc, 0);
        addFormRow(formPanel, "Nama:", namaField, gbc, 1);
        addFormRow(formPanel, "Kelas:", kelasField, gbc, 2);

        // Button panel aligned to right
        JPanel buttonPanel = createCompactButtonPanel();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(buttonPanel, gbc);

        return formPanel;
    }

    private JPanel createCompactButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setBackground(Theme.SURFACE_DARK);
        
        // Set smaller size for all buttons
        Dimension buttonSize = new Dimension(80, 25);
        addButton.setPreferredSize(buttonSize);
        updateButton.setPreferredSize(buttonSize);
        deleteButton.setPreferredSize(buttonSize);
        clearButton.setPreferredSize(buttonSize);
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        return buttonPanel;
    }

    private JScrollPane createTableScrollPane() {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Theme.SURFACE_DARK);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.ACCENT, 1));
        return scrollPane;
    }

    private void setupEventListeners() {
        addButton.addActionListener(e -> addMahasiswa());
        updateButton.addActionListener(e -> updateMahasiswa());
        deleteButton.addActionListener(e -> deleteMahasiswa());
        clearButton.addActionListener(e -> clearForm());
        searchButton.addActionListener(e -> searchMahasiswa(searchField.getText()));

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTableSelection();
            }
        });
    }

    private void addFormRow(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Theme.TEXT_PRIMARY);
        
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(label, gbc);
        
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Theme.BACKGROUND_DARK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 11));
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }

    private void handleTableSelection() {
        selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            MahasiswaModel m = mahasiswaList.get(selectedRow);
            nimField.setText(m.getNim());
            namaField.setText(m.getNama());
            kelasField.setText(m.getKelas());
            nimField.setEditable(false);
            updateButton.setEnabled(true);
            deleteButton.setEnabled(true);
            addButton.setEnabled(false);
        }
    }

    private void initializeState() {
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        refreshTable();
    }

    private void addMahasiswa() {
        if (!validateForm()) {
            showError("Semua kolom harus diisi!");
            return;
        }

        MahasiswaModel mahasiswa = new MahasiswaModel(
            nimField.getText().trim(),
            namaField.getText().trim(),
            kelasField.getText().trim()
        );
        
        mahasiswaList.add(mahasiswa);
        FileHelper.saveMahasiswa(mahasiswaList);
        refreshTable();
        clearForm();
        showSuccess("Mahasiswa berhasil ditambahkan!");
    }

    private void updateMahasiswa() {
        if (selectedRow == -1) return;

        mahasiswaList = FileHelper.getMahasiswa();
        Optional.ofNullable(mahasiswaList.get(selectedRow)).ifPresent(mahasiswa -> {
            mahasiswa.setNama(namaField.getText().trim());
            mahasiswa.setKelas(kelasField.getText().trim());

            FileHelper.saveMahasiswa(mahasiswaList);
            refreshTable();
            clearForm();
            showSuccess("Data mahasiswa berhasil diupdate!");
        });
    }

    private void deleteMahasiswa() {
        if (selectedRow == -1) return;

        if (showConfirmDialog("Apakah Anda yakin ingin menghapus data ini?")) {
            mahasiswaList = FileHelper.getMahasiswa();
            mahasiswaList.remove(selectedRow);
            FileHelper.saveMahasiswa(mahasiswaList);
            refreshTable();
            clearForm();
            showSuccess("Data mahasiswa berhasil dihapus!");
        }
    }

    private void searchMahasiswa(String keyword) {
        List<MahasiswaModel> filteredList = FileHelper.getMahasiswaByKeyword(keyword);
        // Sort filtered results
        filteredList.sort((a, b) -> a.getNim().compareTo(b.getNim()));
        updateTableData(filteredList);
    }

    private void clearForm() {
        nimField.setText("");
        namaField.setText("");
        kelasField.setText("");
        nimField.setEditable(true);
        table.clearSelection();
        selectedRow = -1;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        addButton.setEnabled(true);
    }

    private void refreshTable() {
        mahasiswaList = FileHelper.getMahasiswa();
        updateTableData(mahasiswaList);
    }

    private void updateTableData(List<MahasiswaModel> data) {
        // Sort the data by NIM
        data.sort((a, b) -> a.getNim().compareTo(b.getNim()));
        
        tableModel.setRowCount(0);
        for (MahasiswaModel m : data) {
            tableModel.addRow(new Object[]{m.getNim(), m.getNama(), m.getKelas()});
        }
    }

    private boolean validateForm() {
        return !nimField.getText().trim().isEmpty() &&
               !namaField.getText().trim().isEmpty() &&
               !kelasField.getText().trim().isEmpty();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Sukses", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Konfirmasi", 
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
}
