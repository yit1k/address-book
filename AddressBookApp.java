package lib;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import java.util.*;

public class AddressBookApp extends JFrame {
    private DefaultTableModel tableModel;
    private JTable contactTable;
    private JTextField nameField, phoneNumberField, emailField, jobField; // Added jobField
    private JButton addButton, updateButton, deleteButton;

    private static final String FILE_PATH = "address_book.csv";

    public AddressBookApp() {
        setTitle("Address Book");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        

        tableModel = new DefaultTableModel();
        tableModel.addColumn("Name");
        tableModel.addColumn("Phone Number");
        tableModel.addColumn("Email");
        tableModel.addColumn("Job"); // Added job column

        contactTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(contactTable);

        nameField = new JTextField();
        nameField.setBackground(Color.GREEN);
        phoneNumberField = new JTextField();
        phoneNumberField.setBackground(Color.GREEN);
        
        emailField = new JTextField();
        emailField.setBackground(Color.GREEN);
        jobField = new JTextField(); // Added jobField
        jobField.setBackground(Color.GREEN);
        
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        JPanel formPanel = new JPanel(new GridLayout(4, 2)); // Changed GridLayout to accommodate the jobField
        formPanel.setBackground(Color.cyan);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneNumberField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Job:"));
        formPanel.add(jobField); // Added jobField

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.orange);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    addContact();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AddressBookApp.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    updateContact();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AddressBookApp.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteContact();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(AddressBookApp.this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loadContactsFromFile();
    }

    private void addContact() {
        String name = nameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String job = jobField.getText(); // Retrieve job from jobField

        if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || job.isEmpty()) {
            throw new IllegalArgumentException("Please enter all fields.");
        }

        if (!isNameValid(name)) {
            throw new IllegalArgumentException("Invalid name. Name should not contain numbers.");
        }

        if (!isPhoneNumberValid(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number. Phone number should only contain numbers.");
        }

        String[] rowData = { name, phoneNumber, email, job }; // Added job to rowData
        tableModel.addRow(rowData);
        saveContactsToFile();

        nameField.setText("");
        phoneNumberField.setText("");
        emailField.setText("");
        jobField.setText(""); // Clear jobField
    }

    private void updateContact() {
        int selectedRow = contactTable.getSelectedRow();

        if (selectedRow == -1) {
            throw new IllegalArgumentException("Please select a contact to update.");
        }

        String name = nameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String job = jobField.getText(); // Retrieve job from jobField

        if (name.isEmpty() || phoneNumber.isEmpty() || email.isEmpty() || job.isEmpty()) {
            throw new IllegalArgumentException("Please enter all fields.");
        }

        if (!isNameValid(name)) {
            throw new IllegalArgumentException("Invalid name. Name should not contain numbers.");
        }

        if (!isPhoneNumberValid(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number. Phone number should only contain numbers.");
        }

        tableModel.setValueAt(name, selectedRow, 0);
        tableModel.setValueAt(phoneNumber, selectedRow, 1);
        tableModel.setValueAt(email, selectedRow, 2);
        tableModel.setValueAt(job, selectedRow, 3); // Update job in tableModel

        saveContactsToFile();

        nameField.setText("");
        phoneNumberField.setText("");
        emailField.setText("");
        jobField.setText(""); // Clear jobField
    }

    private void deleteContact() {
        int selectedRow = contactTable.getSelectedRow();

        if (selectedRow == -1) {
            throw new IllegalArgumentException("Please select a contact to delete.");
        }

        tableModel.removeRow(selectedRow);
        saveContactsToFile();

        nameField.setText("");
        phoneNumberField.setText("");
        emailField.setText("");
        jobField.setText(""); // Clear jobField
    }

    private void loadContactsFromFile() {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] rowData = line.split(",");

                if (rowData.length == 4) { // Check if the line has all the required fields
                    tableModel.addRow(rowData);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveContactsToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();

                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    sb.append(tableModel.getValueAt(i, j));

                    if (j != tableModel.getColumnCount() - 1) {
                        sb.append(",");
                    }
                }

                writer.println(sb.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isNameValid(String name) {
        return !name.matches(".*\\d+.*"); // Check if the name contains any numbers
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("\\d+"); // Check if the phoneNumber contains only numbers
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AddressBookApp().setVisible(true);
            }
        });
    }
}