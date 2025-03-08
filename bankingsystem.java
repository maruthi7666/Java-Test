package Test;

abstract class BankAccount1 {
    String accountNumber;
    double balance;

    public BankAccount1(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public void deposit(double amount) {
        balance += amount;
        System.out.println("New balance: " + balance);
    }

    public abstract boolean withdraw(double amount);
}

interface Transaction {
    boolean transfer(BankAccount1 toAccount, double amount);
}

class SavingsAccount extends BankAccount1 implements Transaction {
    public SavingsAccount(String accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public boolean withdraw(double amount) {
        if (balance - amount >= 500) {
            balance -= amount;
            System.out.println("Withdrawal successful, New balance: " + balance);
            return true;
        }
        System.out.println("Withdrawal failed, minimum ₹500 balance required.");
        return false;
    }

    @Override
    public boolean transfer(BankAccount1 toAccount, double amount) {
        if (withdraw(amount)) {
            toAccount.deposit(amount);
            return true;
        }
        return false;
    }
}

class CurrentAccount extends BankAccount1 implements Transaction {
    private static final double OVERDRAFT_LIMIT = -5000;

    public CurrentAccount(String accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public boolean withdraw(double amount) {
        if (balance - amount >= OVERDRAFT_LIMIT) {
            balance -= amount;
            System.out.println("Withdrawal successful, New balance: " + balance);
            return true;
        }
        System.out.println("Withdrawal failed, limit exceeded.");
        return false;
    }

    @Override
    public boolean transfer(BankAccount1 toAccount, double amount) {
        if (withdraw(amount)) {
            toAccount.deposit(amount);
            return true;
        }
        return false;
    }
}

public class bankingsystem {
    public static void main(String[] args) {
        BankAccount1 savings = new SavingsAccount("SAV123", 5000);
        BankAccount1 current = new CurrentAccount("CUR456", 2000);

        savings.deposit(1000);
        current.withdraw(3000);
        ((Transaction) savings).transfer(current, 1500);
        ((Transaction) current).transfer(savings, 6000);
    }
}