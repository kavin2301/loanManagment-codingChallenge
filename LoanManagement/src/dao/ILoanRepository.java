package dao;

import exception.InvalidLoanException;
import entity.Loan;
import java.util.List;

public interface ILoanRepository {

    
    boolean applyLoan(Loan loan); 

    double calculateInterest(int loanId) throws InvalidLoanException;

    double calculateInterest(double principal, double rate, int tenureMonths);

    void loanStatus(int loanId) throws InvalidLoanException;

    double calculateEMI(int loanId) throws InvalidLoanException;

    double calculateEMI(double principal, double rate, int tenureMonths);

    void loanRepayment(int loanId, double amount) throws InvalidLoanException;

    List<Loan> getAllLoan();

    Loan getLoanById(int loanId) throws InvalidLoanException;
}
