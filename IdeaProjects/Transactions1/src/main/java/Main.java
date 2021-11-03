import org.apache.log4j.Logger;

import java.util.*;

public class Main {

    private static Bank bank;
    private static List<Account> accounts = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final int cores = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {

        List<Thread> threads = new ArrayList<>(cores);

        bank = new Bank();

        accounts = createMultipleAccounts(3);
        registerAccountsToBank(bank, accounts);

        int transactions = 1000;

        System.out.println(bank.getSumAllAccounts() + " Сумма денег в банке перед транзакциями");

        for (int i = 0; i < transactions; i++) {

            threads.add(new Thread(() -> {
                String fromAcc = accounts.get((int) (Math.random() * accounts.size())).getAccNumber();
                String toAcc = accounts.get((int) (Math.random() * accounts.size())).getAccNumber();

                long money = (long) (1 + Math.random() * 100000);

                try {
                    bank.transfer(fromAcc, toAcc, money);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Some exception", e);
                }
            }));
        }
        threads.forEach(Thread::start);

        for(Thread thread : threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(bank.getSumAllAccounts() + " Сумма денег в банке после транзакций");
    }


    private static List<Account> createMultipleAccounts(int amount) {
        for (int i = 0; i < amount; i++) {
            accounts.add(new Account(generateMoneyAmount(), generateAccountNumber()));
        }
        return accounts;
    }

    private static void registerAccountsToBank(Bank bank, List<Account> accounts) {
        String accNumber;

        for (Account account : accounts) {
            accNumber = account.getAccNumber();
            bank.registerNewAccount(accNumber, account);
        }
    }

    private static long generateMoneyAmount() {
        return (long) (1 + Math.random() * 250000);
    }

    private static String generateAccountNumber() {

        StringBuilder countryCode = new StringBuilder("AA");
        Random random = new Random();

        for (int i = 0; i <= 3; i++) {
            int numbers = random.nextInt(9);
            countryCode.append((numbers));
        }
        return countryCode.toString();
    }
}