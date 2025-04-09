use loan_management;

CREATE TABLE Customer (
    customerId INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    emailAddress VARCHAR(100) UNIQUE NOT NULL,
    phoneNumber VARCHAR(15) UNIQUE NOT NULL,
    address VARCHAR(255),
    creditScore INT
);

CREATE TABLE Loan (
    loanId INT PRIMARY KEY,
    customerId INT,
    principalAmount DECIMAL(15, 2),
    interestRate DECIMAL(5, 2),
    loanTerm INT,
    loanType VARCHAR(50),
    loanStatus VARCHAR(20),
    FOREIGN KEY (customerId) REFERENCES Customer(customerId)
);

show tables;