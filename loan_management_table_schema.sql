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

INSERT INTO Customer (customerId, name, emailAddress, phoneNumber, address, creditScore) VALUES
(1, 'Aarav Mehta', 'aarav.mehta@example.com', '9876543210', '123 Green Street, Chennai', 720),
(2, 'Divya Sharma', 'divya.sharma@example.com', '9123456780', '456 Blue Avenue, Bangalore', 680),
(3, 'Karthik Reddy', 'karthik.reddy@example.com', '9988776655', '789 Yellow Road, Hyderabad', 620),
(4, 'Sneha Iyer', 'sneha.iyer@example.com', '9001122334', '321 Red Lane, Mumbai', 750),
(5, 'Vikram Das', 'vikram.das@example.com', '8899776655', '654 Violet Plaza, Kolkata', 600);

INSERT INTO Loan (loanId, customerId, principalAmount, interestRate, loanTerm, loanType, loanStatus) VALUES
(101, 1, 500000.00, 7.5, 60, 'Home Loan', 'Pending'),
(102, 2, 300000.00, 8.2, 36, 'Car Loan', 'Pending'),
(103, 3, 400000.00, 9.0, 48, 'Car Loan', 'Pending'),
(104, 4, 750000.00, 6.5, 84, 'Home Loan', 'Pending'),
(105, 5, 600000.00, 7.8, 72, 'Home Loan', 'Pending');


show tables;