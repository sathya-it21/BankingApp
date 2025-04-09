package BankingApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BankAppCardLayout {
    private static double annualInterestRate;
    private static ArrayList<Customer> customers = new ArrayList<>();
    private static CardLayout cardLayout = new CardLayout();
    private static JPanel mainPanel = new JPanel(cardLayout);
    private static JLabel resultLabel = new JLabel("");

    private static DefaultListModel<String> listModel = new DefaultListModel<>();
    private static JList<String> customerList = new JList<>(listModel);

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}

        customers.add(new Customer("Alice", 1000));
        customers.add(new Customer("Bob", 2000));

        SwingUtilities.invokeLater(() -> createUI());
    }

    private static void createUI() {
        JFrame frame = new JFrame("Bank App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 450);

        mainPanel.add(createMainMenuPanel(), "menu");
        mainPanel.add(createSetInterestPanel(), "interest");
        mainPanel.add(createCustomerListPanel(), "customers");
        mainPanel.add(createAddCustomerPanel(), "addCustomer");

        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createMainMenuPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JButton btnSetInterest = styledButton("Set Annual Interest Rate");
        JButton btnCustomers = styledButton("Customers List");

        btnSetInterest.addActionListener(e -> cardLayout.show(mainPanel, "interest"));
        btnCustomers.addActionListener(e -> {
            refreshCustomerListPanel();
            resultLabel.setText("");
            cardLayout.show(mainPanel, "customers");
        });

        panel.add(btnSetInterest);
        panel.add(btnCustomers);

        return wrapWithBack(panel, false);
    }

    private static JPanel createSetInterestPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel label = styledLabel("Enter Annual Interest Rate (%):");
        JTextField interestField = styledTextField();
        JButton setButton = styledButton("Set");

        setButton.addActionListener(e -> {
            try {
                annualInterestRate = Double.parseDouble(interestField.getText());
                JOptionPane.showMessageDialog(panel, "Interest rate set to " + annualInterestRate + "%");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Please enter a valid number.");
            }
        });

        panel.add(label);
        panel.add(interestField);
        panel.add(setButton);

        return wrapWithBack(panel, true);
    }

    private static JPanel createCustomerListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = styledLabel("Customer List");
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        customerList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        customerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(customerList);
        JButton addCustomerButton = styledButton("Add New Customer");

        addCustomerButton.addActionListener(e -> cardLayout.show(mainPanel, "addCustomer"));

        customerList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = customerList.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        Customer selected = customers.get(index);
                        String[] options = {"Show Balance", "Calculate Interest"};
                        int choice = JOptionPane.showOptionDialog(
                                panel,
                                "Choose an action for " + selected.getName(),
                                "Customer Action",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                options,
                                options[0]
                        );
                        if (choice == 0) {
                            JOptionPane.showMessageDialog(panel,
                                selected.getName() + "'s Balance: $" + String.format("%.2f", selected.getBalance()),
                                "Balance Info",
                                JOptionPane.INFORMATION_MESSAGE);
                        } else if (choice == 1) {
                            if (annualInterestRate == 0.0) {
                                JOptionPane.showMessageDialog(panel,
                                    "Annual interest rate is not set. Please set it first.",
                                    "Interest Rate Missing",
                                    JOptionPane.WARNING_MESSAGE);
                            } else {
                                double interest = selected.getBalance() * annualInterestRate / 100;
                                JOptionPane.showMessageDialog(panel,
                                    "Interest for " + selected.getName() + ": $" + String.format("%.2f", interest),
                                    "Interest Info",
                                    JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    }
                }
            }
        });

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(addCustomerButton, BorderLayout.SOUTH);

        JPanel wrapped = wrapWithBack(panel, true);
        wrapped.add(resultLabel, BorderLayout.SOUTH);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        return wrapped;
    }

    private static JPanel createAddCustomerPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JTextField nameField = styledTextField();
        JTextField balanceField = styledTextField();
        JButton addButton = styledButton("Add Customer");

        panel.add(styledLabel("Customer Name:"));
        panel.add(nameField);
        panel.add(styledLabel("Balance:"));
        panel.add(balanceField);
        panel.add(new JLabel(""));
        panel.add(addButton);

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                double balance = Double.parseDouble(balanceField.getText().trim());

                if (name.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Customer name cannot be empty.");
                    return;
                }
                
               


                customers.add(new Customer(name, balance));
                refreshCustomerListPanel();
                JOptionPane.showMessageDialog(panel, "Customer added: " + name);
                nameField.setText("");
                balanceField.setText("");
                cardLayout.show(mainPanel, "customers");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "Invalid balance.");
            }
        });

        return wrapWithBack(panel, true);
    }

    private static void refreshCustomerListPanel() {
        listModel.clear();
        for (Customer c : customers) {
            listModel.addElement(c.getName());
        }
    }

    private static JPanel wrapWithBack(JPanel panel, boolean addBack) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(panel, BorderLayout.CENTER);

        if (addBack) {
            JButton backButton = styledButton("Back");
            backButton.setPreferredSize(new Dimension(80, 30));
            backButton.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

            JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            topBar.add(backButton);
            wrapper.add(topBar, BorderLayout.NORTH);
        }

        return wrapper;
    }

    // --- Styling Helpers ---

    private static JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(200, 40));
        return btn;
    }

    private static JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private static JTextField styledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    // --- Customer Model ---
    static class Customer {
        private String name;
        private double balance;

        public Customer(String name, double balance) {
            this.name = name;
            this.balance = balance;
        }

        public String getName() {
            return name;
        }

        public double getBalance() {
            return balance;
        }
    }
}
