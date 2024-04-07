import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionAnalyzer {
    private List<Transaction> transactions;

    public TransactionAnalyzer() {
        this.transactions = new ArrayList<>();
    }

    public void readTransactionsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Пропускаємо перший рядок, оскільки він містить заголовки стовпців
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String date = data[0].trim();
                    String description = data[1].trim();
                    double amount = Double.parseDouble(data[2].trim());
                    transactions.add(new Transaction(date, description, amount));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double calculateTotalBalance() {
        double totalBalance = 0;
        for (Transaction transaction : transactions) {
            totalBalance += transaction.getAmount();
        }
        return totalBalance;
    }

    public Map<String, Integer> countTransactionsByMonth() {
        Map<String, Integer> transactionsByMonth = new HashMap<>();
        for (Transaction transaction : transactions) {
            String[] dateParts = transaction.getDate().split("/");
            if (dateParts.length == 3) {
                String month = dateParts[1] + "/" + dateParts[2];
                transactionsByMonth.put(month, transactionsByMonth.getOrDefault(month, 0) + 1);
            }
        }
        return transactionsByMonth;
    }

    public List<Transaction> getTopExpenses(int n) {
        transactions.sort((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()));
        return transactions.subList(0, Math.min(n, transactions.size()));
    }

    public static void main(String[] args) {
        TransactionAnalyzer analyzer = new TransactionAnalyzer();
        analyzer.readTransactionsFromCSV("transactions.csv");

        // Calculate total balance
        double totalBalance = analyzer.calculateTotalBalance();
        System.out.println("Total balance: $" + totalBalance);

        // Count transactions by month
        Map<String, Integer> transactionsByMonth = analyzer.countTransactionsByMonth();
        System.out.println("Transactions by month:");
        for (Map.Entry<String, Integer> entry : transactionsByMonth.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Get top expenses
        List<Transaction> topExpenses = analyzer.getTopExpenses(10);
        System.out.println("Top expenses:");
        for (Transaction transaction : topExpenses) {
            System.out.println(transaction.getDate() + " | " +
                    transaction.getDescription() + " | $" + transaction.getAmount());
        }
    }

    private static class Transaction {
        private String date;
        private String description;
        private double amount;

        public Transaction(String date, String description, double amount) {
            this.date = date;
            this.description = description;
            this.amount = amount;
        }

        public String getDate() {
            return date;
        }

        public String getDescription() {
            return description;
        }

        public double getAmount() {
            return amount;
        }
    }
}
