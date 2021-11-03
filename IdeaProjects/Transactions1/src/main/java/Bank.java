import org.apache.log4j.Logger;

import java.util.*;

public class Bank {

    private final int maxLimitTransaction = 50000;
    private final static Logger logger = Logger.getLogger(Bank.class.getName());


    private final Map<String, Account> accounts;
    private final Random random = new Random();

    public Bank() {
        accounts = new HashMap<>();
    }


    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {

        Thread.sleep(1000);
        return random.nextBoolean();
    }

    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {

        Object lowSyncAcc;
        Object topSyncAcc;


        Account sender = accounts.get(fromAccountNum);
        Account receiver = accounts.get(toAccountNum);


        synchronized (sender) {
            synchronized (receiver) {
                if (sender.isBlocked() || receiver.isBlocked()) {
                    System.out.println("Невозможно провести операцию счет заблокирован");
                    return;
                }
            }
        }

        synchronized (lowSyncAcc) {

            synchronized (topSyncAcc) {

                sendMoney(sender, receiver, amount);
            }
        }

        synchronized (lowSyncAcc) {

            synchronized (topSyncAcc) {

                if (amount > maxLimitTransaction && isFraud(fromAccountNum, toAccountNum, amount)) {
                    sender.setBlocked(true);
                    receiver.setBlocked(true);
                }
            }
        }
    }

    public void printAccounts() {
        accounts.forEach((key, value) -> System.out.println(key + " " + value));
    }

    public long getBalance(String accountNum) {

        long money = 0;

        for (Map.Entry<String, Account> entry : accounts.entrySet()) {
            if (entry.getValue().getAccNumber().equals(accountNum)) {
                money = entry.getValue().getMoney();
            }
        }
        return money;
    }

    public long getSumAllAccounts() {

        return accounts.values().stream().mapToLong(Account::getMoney).sum();
    }

    public void registerNewAccount(String accNumber, Account account) {
        accNumber = account.getAccNumber();
        accounts.put(accNumber, account);
    }

    public void sendMoney(Account sender, Account receiver, long amount) {

        if (amount > sender.getMoney() || amount <= 0) {
            sender.setMoney(sender.getMoney());
            System.out.println("Недостаточно средств или неверная сумма перевода");
        } else {
            sender.setMoney(sender.getMoney() - amount);
            receiver.setMoney(receiver.getMoney() + amount);
        }
    }

    public long putMoneyOnAccount(Account account, long amount) {

        if (amount <= 0) {
            account.setMoney(account.getMoney() - (amount));
            System.out.println("Неверная сумма");
        }

        return account.setMoney(account.getMoney() + amount);
    }

    public long withdrawMoney(Account account, long amount) {

        if (amount > account.getMoney() || amount <= 0) {
            account.setMoney(account.getMoney() + amount);
            System.out.println("Недостаточный баланс или неверная сумма");
        }

        return account.setMoney(account.getMoney() - amount);
    }
}