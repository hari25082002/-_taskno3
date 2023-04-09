import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;

public class ATM {
    public static void main(String[] args) {
        User[] users = new User[2];
        ATMOperations[] atmOperations = new ATMOperations[2];
        BankAccount[] bankAccounts = new BankAccount[2];
        Scanner input = new Scanner(System.in);      
     // Create  users and their corresponding bank accounts.
     HashSet<Integer> accountNumbers = new HashSet<Integer>();  
        for (int i = 0; i < users.length; i++) {
        	System.out.println("Enter 4 digit user id for user " + (i+1) + ": ");
        	int userId = 0;
        	while (true) {
        	    try {
        	        userId = input.nextInt();
        	        if (String.valueOf(userId).length() != 4) {
        	            throw new Exception("User ID should be exactly 4 digits");
        	        }
        	        // Check if the entered user ID already exists
        	        boolean exists = false;
        	        for (int j = 0; j < i; j++) {
        	            if (userId == users[j].getUserId()) {
        	                exists = true;
        	                break;
        	            }
        	        }
        	        if (exists) {
        	            throw new Exception("User ID should be unique");
        	        }
        	        String userIdString = String.valueOf(userId);
        	        for (int j = 0; j < userIdString.length() - 1; j++) {
        	            if (userIdString.charAt(j) + 1 == userIdString.charAt(j+1)) {
        	                throw new Exception("User ID should not contain consecutive digits.");
        	            }
        	            if (userIdString.charAt(j) == userIdString.charAt(j+1)) {
        	                throw new Exception("User ID should not contain repeating digits.");
        	            }
        	        }
        	        break;
        	    } catch (Exception e) {
        	        System.out.println(e.getMessage());
        	        System.out.println("Enter  user ID again for user " + (i+1) + ": ");
        	        input.nextLine();
        	    }
        	}
        	
            System.out.println("Enter 4 digit user pin for user " + (i+1) + ": ");
            int userPin = 0;
            while (true) {
                try {
                    userPin = input.nextInt();
                    if (String.valueOf(userPin).length() != 4) {
                        throw new Exception("User PIN should be exactly 4 digits");
                    }
                    if (userPin == userId) {
                        throw new Exception("User PIN cannot be the same as user ID");
                    }
                    String userPinString = String.valueOf(userPin);
                    for (int j = 0; j < userPinString.length() - 1; j++) {
                        if (userPinString.charAt(j) + 1 == userPinString.charAt(j+1)) {
                            throw new Exception("User PIN should not contain consecutive digits.");
                        }
                    }
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Enter user PIN again for user " + (i+1) + ": ");
                    input.nextLine();
                }
            }
            
            System.out.println("Enter 6 digit account number for user " + (i+1) + ": ");
            int accountNumber = 0;
            while (true) {
                try {
                    accountNumber = input.nextInt();
                    // Check if the entered account number already exists
                    if (accountNumbers.contains(accountNumber)) {
                        throw new Exception("Account number should be unique");
                    }                 
                    if (String.valueOf(accountNumber).length() != 6) {
                        throw new Exception("Account number should have exactly 6 digits");
                    }                   
                    accountNumbers.add(accountNumber);
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("Enter account number again for user " + (i+1) + ": ");
                    input.nextLine(); // Consume the invalid input
                }
            }
            System.out.println("Enter account balance for user " + (i+1) + ": ");
            double accountBalance = input.nextDouble();
            bankAccounts[i] = new BankAccount(accountNumber, accountBalance);
            users[i] = new User(userId, userPin, new BankAccount[]{bankAccounts[i]});
            atmOperations[i] = new ATMOperations(bankAccounts[i]); // create an instance of ATMOperations for the current BankAccount
        }
        // Start the ATM interface
        ATMInterface atmInterface = new ATMInterface(users, atmOperations, bankAccounts);
        atmInterface.start();
        
    }
}

class User {
    private int userId;
    private int userPin;
    private BankAccount[] accounts;

    public User(int userId, int userPin, BankAccount[] accounts) {
        this.userId = userId;
        this.userPin = userPin;
        this.accounts = accounts;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserPin() {
        return userPin;
    }

    public void setUserPin(int userPin) {
        this.userPin = userPin;
    }

    public BankAccount[] getAccounts() {
        return accounts;
    }

    public void setAccounts(BankAccount[] accounts) {
        this.accounts = accounts;
    }
}

class BankAccount {
    private int accountNumber;
    private double accountBalance;

    public BankAccount(int accountNumber, double accountBalance) {
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
    
}

class ATMOperations {
    private BankAccount bankAccount;
    private List<Double> transactionHistory;

    public ATMOperations(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
        this.transactionHistory = new ArrayList<>();
    }

    public double checkBalance() {
        return bankAccount.getAccountBalance();
    }

    public void withdraw(double amount) {
        if (amount > bankAccount.getAccountBalance()) {
            System.out.println("Insufficient funds");
        } else {
            bankAccount.setAccountBalance(bankAccount.getAccountBalance() - amount);
            transactionHistory.add(-amount);
            System.out.println("Withdrawal successful");
        }
    }

    public void deposit(double amount) {
        bankAccount.setAccountBalance(bankAccount.getAccountBalance() + amount);
        transactionHistory.add(amount);
        System.out.println("Deposit successful");
    }

    public List<Double> getTransactionHistory() {
        return transactionHistory;
    }
}

class ATMInterface {
    private User[] users;
    private ATMOperations[] atmOperations;
    private BankAccount[] bankAccounts;

    public ATMInterface(User[] users, ATMOperations[] atmOperations, BankAccount[] bankAccounts) {
        this.users = users;
        this.atmOperations = atmOperations;
        this.bankAccounts = bankAccounts;
    }
    public void transfer(int senderIndex, int receiverIndex, double amount) {
        if (senderIndex >= 0 && senderIndex < bankAccounts.length && receiverIndex >= 0 && receiverIndex < bankAccounts.length) {
            BankAccount senderAccount = bankAccounts[senderIndex];
            BankAccount receiverAccount = bankAccounts[receiverIndex];
            if (senderAccount.getAccountBalance() >= amount) {
                senderAccount.setAccountBalance(senderAccount.getAccountBalance() - amount);
                receiverAccount.setAccountBalance(receiverAccount.getAccountBalance() + amount);
                System.out.println("Transfer successful");
            } else {
                System.out.println("Insufficient funds for transfer");
            }
        } else {
            System.out.println("Invalid user index for transfer");
        }
    }

    public void start() {
        Scanner input = new Scanner(System.in);
        System.out.println("Welcome to the ATM");
        System.out.println("Choose an option:");
        System.out.println("1. Transaction history");
        System.out.println("2. Withdraw");
        System.out.println("3. Deposit");
        System.out.println("4. Transfer");
        System.out.println("5. Quit");
        int option = input.nextInt();
    
        switch (option) {
        case 1:
            System.out.println("Enter your user id:");
            int userId = input.nextInt();
            System.out.println("Enter your user pin:");
            int userPin = input.nextInt();
            // Find the user with the matching userId and userPin
            User currentUser = null;
            ATMOperations currentOperations = null;
            for (int i = 0; i < users.length; i++) {
                if (users[i].getUserId() == userId && users[i].getUserPin() == userPin) {
                    currentUser = users[i];
                    currentOperations = atmOperations[i];
                    break;
                }
            }
            if (currentUser != null && currentOperations != null) {
                List<Double> transactionHistory = currentOperations.getTransactionHistory();
                System.out.println("Transaction history:");
                for (double amount : transactionHistory) {
                    if (amount > 0) {
                        System.out.println("Deposit: " + amount);
                    } else {
                        System.out.println("Withdrawal: " + Math.abs(amount));
                    }
                }
            } else {
                System.out.println("Invalid user id or pin");
            }
            break;
            case 2:
                System.out.println("Enter the user index:");
                int userIndex = input.nextInt();
                if (userIndex >= 0 && userIndex < atmOperations.length) {
                    System.out.println("Enter the amount to withdraw:");
                    double withdrawAmount = input.nextDouble();
                    atmOperations[userIndex].withdraw(withdrawAmount);
                } else {
                    System.out.println("Invalid user index");
                }
                break;
            case 3:
                System.out.println("Enter your user id:");
                userId = input.nextInt();
                System.out.println("Enter your user pin:");
                userPin = input.nextInt();
                // Find the user with the matching userId and userPin
                currentUser = null;
                currentOperations = null;
                for (int i = 0; i < users.length; i++) {
                    if (users[i].getUserId() == userId && users[i].getUserPin() == userPin) {
                        currentUser = users[i];
                        currentOperations = atmOperations[i];
                        break;
                    }
                }
                if (currentUser != null && currentOperations != null) {
                    System.out.println("Login successful");
                    System.out.println("Enter the amount to deposit:");
                    double depositAmount = input.nextDouble();
                    currentOperations.deposit(depositAmount);
                } else {
                    System.out.println("Invalid user id or user pin");
                }
                break;
            case 4:
                System.out.println("Enter the user index of the sender:");
                int senderIndex = input.nextInt();
                System.out.println("Enter the user index of the receiver:");
                int receiverIndex = input.nextInt();
                System.out.println("Enter the amount to transfer:");
                double transferAmount = input.nextDouble();
                transfer(senderIndex, receiverIndex, transferAmount);
                break;
            case 5:
                System.out.println("Thank you for using the ATM! Have a nyc day ");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option");
                break;
        }
            // ask user if they want to perform another transaction
            System.out.println("Do you want to perform another transaction? (Y/N)");
            String answer = input.next();
            if (answer.equalsIgnoreCase("Y")) {
            // perform another transaction
            start();
            } else {
            // end program
            System.out.println("Thank you for using the ATM");
            }
            }
}