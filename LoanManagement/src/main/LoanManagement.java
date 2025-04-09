package main;

import dao.LoanRepositoryImpl;
import dao.ILoanRepository;
import entity.CarLoan;
import entity.HomeLoan;
import entity.Customer;
import entity.Loan;
import exception.InvalidLoanException;

import java.util.*;

public class LoanManagement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ILoanRepository loanRepo = new LoanRepositoryImpl();
        boolean running = true;

        while (running) {
            System.out.println("\n--- Loan Management Menu ---");
            System.out.println("1. Apply Loan");
            System.out.println("2. Get All Loans");
            System.out.println("3. Get Loan By ID");
            System.out.println("4. Loan Repayment");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) {
                case 1:
                    System.out.print("Enter loan ID: ");
                    int loanId = sc.nextInt();
                    System.out.print("Enter customer ID: ");
                    int customerId = sc.nextInt();
                    System.out.print("Enter principal amount: ");
                    double principal = sc.nextDouble();
                    System.out.print("Enter interest rate: ");
                    double rate = sc.nextDouble();
                    System.out.print("Enter loan term in months: ");
                    int term = sc.nextInt();
                    sc.nextLine(); // consume newline
                    System.out.print("Enter loan type (Home/Car): ");
                    String type = sc.nextLine().toLowerCase();

                    Customer cust = new Customer();
                    cust.setCustomerId(customerId);

                    Loan loan = null;

                    if (type.equals("home")) {
                        System.out.print("Enter property address: ");
                        String address = sc.nextLine();
                        System.out.print("Enter property value: ");
                        int value = sc.nextInt();
                        sc.nextLine();
                        loan = new HomeLoan(loanId, cust, principal, rate, term, "Home", "Pending", address, value);

                    } else if (type.equals("car")) {
                        System.out.print("Enter car model: ");
                        String model = sc.nextLine();
                        System.out.print("Enter car value: ");
                        int value = sc.nextInt();
                        sc.nextLine();
                        loan = new CarLoan(loanId, cust, principal, rate, term, "Car", "Pending", model, value);
                    } else {
                        System.out.println("Invalid loan type entered.");
                    }

                    if (loan != null) {
                        boolean success = loanRepo.applyLoan(loan);
                        System.out.println(success ? "Loan applied successfully!" : "Loan application failed.");
                    }
                    break;

                case 2:
                    List<Loan> loans = loanRepo.getAllLoan();
                    for (Loan l : loans) {
                        System.out.println("Loan ID: " + l.getLoanId() + ", Type: " + l.getLoanType() + ", Status: " + l.getLoanStatus());
                    }
                    break;

                case 3:
                    System.out.print("Enter loan ID: ");
                    int id = sc.nextInt();
                    try {
                        Loan l = loanRepo.getLoanById(id);
                        System.out.println("Loan Details:");
                        System.out.println("ID: " + l.getLoanId());
                        System.out.println("Type: " + l.getLoanType());
                        System.out.println("Status: " + l.getLoanStatus());
                    } catch (InvalidLoanException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Enter loan ID: ");
                    int repayId = sc.nextInt();
                    System.out.print("Enter repayment amount: ");
                    double amount = sc.nextDouble();
                    try {
                        loanRepo.loanRepayment(repayId, amount);
                    } catch (InvalidLoanException e) {
                        System.out.println("Repayment failed: " + e.getMessage());
                    }
                    break;

                case 5:
                    running = false;
                    System.out.println("Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }

        sc.close();
    }
}
