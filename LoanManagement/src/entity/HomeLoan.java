package entity;

public class HomeLoan extends Loan {
    private String propertyAddress;
    private int propertyValue;

    public HomeLoan() {
        super();
    }

    public HomeLoan(int loanId, Customer customer, double principalAmount, double interestRate,
                    int loanTerm, String loanType, String loanStatus,
                    String propertyAddress, int propertyValue) {
        super(loanId, customer, principalAmount, interestRate, loanTerm, loanType, loanStatus);
        this.propertyAddress = propertyAddress;
        this.propertyValue = propertyValue;
    }

    public String getPropertyAddress() {
        return propertyAddress;
    }

    public void setPropertyAddress(String propertyAddress) {
        this.propertyAddress = propertyAddress;
    }

    public int getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(int propertyValue) {
        this.propertyValue = propertyValue;
    }
    
    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("----- Home Loan Extra Details -----");
        System.out.println("Property Address: " + propertyAddress);
        System.out.println("Property Value: " + propertyValue);
    }
}
