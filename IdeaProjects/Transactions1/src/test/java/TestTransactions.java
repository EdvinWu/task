import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class TestTransactions extends TestCase {

    Account firstAcc;
    Account secondAcc;
    Bank bank;
    Map<String, Account> accounts;

    long firstAccMoney = 40000;
    long secondAccMoney = 70000;
    String firstAccNumber = "ABC";
    String secondAccNumber = "AAA";

    @Override
    public void setUp() {
        bank = new Bank();
        accounts = new HashMap<>();
        firstAcc = new Account(firstAccMoney, firstAccNumber);
        secondAcc = new Account(secondAccMoney, secondAccNumber);

        bank.registerNewAccount(firstAccNumber, firstAcc);
        bank.registerNewAccount(secondAccNumber, secondAcc);

        accounts.put(firstAccNumber, firstAcc);
        accounts.put(secondAccNumber, secondAcc);
    }

    public void testTransfer() throws InterruptedException {

        long money = 40000;

        bank.transfer(firstAcc.getAccNumber(), secondAcc.getAccNumber(), money);

        long expectedResult = firstAccMoney - money;
        long actualResult = firstAcc.getMoney();

        long expectedResult1 = secondAccMoney + money;
        long actualResult1 = secondAcc.getMoney();

        assertEquals(expectedResult, actualResult);
        assertEquals(expectedResult1, actualResult1);
    }

    public void testIsMoneyEnough() throws InterruptedException {

        long money = 41000;

        bank.transfer(firstAcc.getAccNumber(), secondAcc.getAccNumber(), money);

        long expected = firstAccMoney;
        long actual = firstAcc.getMoney();

        assertEquals(expected, actual);

    }

    public void testCorrectMoneyAmount() throws InterruptedException {

        long money = -1;

        bank.transfer(firstAcc.getAccNumber(), secondAcc.getAccNumber(), money);

        long expected = firstAccMoney;
        long actual = firstAcc.getMoney();

        assertEquals(expected, actual);

    }

    public void testSumOffAllAccounts() {

        long expected = firstAccMoney + secondAccMoney;
        long actual = bank.getSumAllAccounts();

        System.out.println(expected);
        System.out.println(actual);

        assertEquals(expected, actual);

    }

    public void testGetBalance() {

        long expected = firstAccMoney;
        long actual = bank.getBalance(firstAccNumber);

        assertEquals(expected, actual);
    }

    public void testPutMoney() {

        long amount = 10000;

        long expected = firstAcc.getMoney() + amount;
        long actual = bank.putMoneyOnAccount(firstAcc, amount);

        assertEquals(expected, actual);
    }

    public void testCorrectMoneyAmountPutOnAccount() {

        long amount = -10;

        long expected = firstAcc.getMoney();
        long actual = bank.putMoneyOnAccount(firstAcc, amount);

        assertEquals(expected, actual);
    }

    public void testWithdrawMoney() {

        long amount = 20000;

        long expected = firstAcc.getMoney() - amount;
        long actual = bank.withdrawMoney(firstAcc, amount);

        assertEquals(expected, actual);
    }

    public void testNotEnoughMoneyOnBalance() {

        long amount = 41000;

        long expected = firstAcc.getMoney();
        long actual = bank.withdrawMoney(firstAcc, amount);

        assertEquals(expected, actual);
    }

    public void testCorrectMoneyAmountWithdrawToAccount() {

        long amount = -1;

        long expected = firstAcc.getMoney();
        long actual = bank.withdrawMoney(firstAcc, amount);

        assertEquals(expected, actual);
    }
}