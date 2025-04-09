package dao;

import entity.*;
import exception.InvalidLoanException;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LoanRepositoryImpl implements ILoanRepository {

    private Connection connection;

    public LoanRepositoryImpl() {
        this.connection = DBUtil.getDBConn();
    }

    @Override
    public boolean applyLoan(Loan loan) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Do you want to proceed with applying for the loan? (Yes/No): ");
        String confirmation = scanner.nextLine();

        if (!confirmation.equalsIgnoreCase("Yes")) {
            System.out.println("Loan application cancelled.");
            return false;
        }

        try {
            String sql = "INSERT INTO Loan (loanId, customerId, principalAmount, interestRate, loanTerm, loanType, loanStatus) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, loan.getLoanId());
            statement.setInt(2, loan.getCustomer().getCustomerId());
            statement.setDouble(3, loan.getPrincipalAmount());
            statement.setDouble(4, loan.getInterestRate());
            statement.setInt(5, loan.getLoanTerm());
            statement.setString(6, loan.getLoanType());
            statement.setString(7, "Pending"); // Default status

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Loan application submitted successfully.");
                return true;
            } else {
                System.out.println("Loan application failed.");
                return false;
            }

        } catch (SQLException e) {
            System.out.println("Error while applying for loan: " + e.getMessage());
            return false;
        }
    }

    @Override
    public double calculateInterest(int loanId) throws InvalidLoanException {
        String sql = "SELECT principalAmount, interestRate, loanTerm FROM Loan WHERE loanId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double principal = resultSet.getDouble("principalAmount");
                double rate = resultSet.getDouble("interestRate");
                int term = resultSet.getInt("loanTerm");
                return (principal * rate * term) / 12;
            } else {
                throw new InvalidLoanException("Loan with ID " + loanId + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public double calculateInterest(double principal, double rate, int term) {
        return (principal * rate * term) / 12;
    }

    @Override
    public void loanStatus(int loanId) {
        String fetchQuery = "select creditScore from Customer inner join Loan ON Customer.customerId = Loan.customerId where loanId = ?";
        try (PreparedStatement statement = connection.prepareStatement(fetchQuery)) {
            statement.setInt(1, loanId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int creditScore = resultSet.getInt("creditScore");
                String status = creditScore > 650 ? "Approved" : "Rejected";

                String updateQuery = "UPDATE Loan SET loanStatus = ? where loanId = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
                    updateStmt.setString(1, status);
                    updateStmt.setInt(2, loanId);
                    updateStmt.executeUpdate();
                    System.out.println("Loan " + status);
                }

            } else {
                System.out.println("Loan or customer not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public double calculateEMI(int loanId) throws InvalidLoanException {
        String sql = "SELECT principalAmount, interestRate, loanTerm FROM Loan WHERE loanId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                double principal = resultSet.getDouble("principalAmount");
                double rate = resultSet.getDouble("interestRate") / 12 / 100; 
                int term = resultSet.getInt("loanTerm");

                return (principal * rate * Math.pow(1 + rate, term)) / (Math.pow(1 + rate, term) - 1);
            } else {
                throw new InvalidLoanException("Loan not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public double calculateEMI(double principal, double rate, int term) {
        double monthlyRate = rate / 12 / 100;
        return (principal * monthlyRate * Math.pow(1 + monthlyRate, term)) /
               (Math.pow(1 + monthlyRate, term) - 1);
    }

    @Override
    public void loanRepayment(int loanId, double amount) {
        try {
            double emi = calculateEMI(loanId);
            if (amount < emi) {
                System.out.println("Amount less than one EMI. Payment rejected.");
                return;
            }

            int numberOfEmisPaid = (int) (amount / emi);
            System.out.println("Number of EMIs paid: " + numberOfEmisPaid);

        } catch (InvalidLoanException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Loan> getAllLoan() {
        List<Loan> loanList = new ArrayList<>();
        String sql = "SELECT * FROM Loan";

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {

            while (resultSet.next()) {
                Loan loan = new Loan();
                loan.setLoanId(resultSet.getInt("loanId"));
                loan.setPrincipalAmount(resultSet.getDouble("principalAmount"));
                loan.setInterestRate(resultSet.getDouble("interestRate"));
                loan.setLoanTerm(resultSet.getInt("loanTerm"));
                loan.setLoanType(resultSet.getString("loanType"));
                loan.setLoanStatus(resultSet.getString("loanStatus"));
                loanList.add(loan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loanList;
    }

    @Override
    public Loan getLoanById(int loanId) throws InvalidLoanException {
        String sql = "SELECT * FROM Loan WHERE loanId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Loan loan = new Loan();
                loan.setLoanId(resultSet.getInt("loanId"));
                loan.setPrincipalAmount(resultSet.getDouble("principalAmount"));
                loan.setInterestRate(resultSet.getDouble("interestRate"));
                loan.setLoanTerm(resultSet.getInt("loanTerm"));
                loan.setLoanType(resultSet.getString("loanType"));
                loan.setLoanStatus(resultSet.getString("loanStatus"));
                return loan;
            } else {
                throw new InvalidLoanException("Loan with ID " + loanId + " not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}