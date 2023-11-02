//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package entity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/*
 * This class creates a detailed transaction with associated filed like accountId, timeStamp, amount and location
 */
public final class DetailedTransaction {
    private long accountId;
    private long timestamp;
    private double amount;

    //zipcode of transaction
    private String zipcode;

    //For testing we limit our zipcodes only 3 values
    private static final List<String> zips = Arrays.asList("01003", "02115", "78712");


    //private static final List<String> zips = Arrays.asList("01003");
    // Tested with 2 zipcodes for higher prob of same zipcodes on fraud txns. Got expected output.
    // In some trial, if the fraudulent txn got assigned same zip codes, an alert was shown.
    // In a trial, if the the fraudulent txn got assigned different zip codes, and an alert was not shown.

    public DetailedTransaction() {
    }

    //Constructor
    public DetailedTransaction(long accountId, long timestamp, double amount) {
        this.accountId = accountId;
        this.timestamp = timestamp;
        this.amount = amount;
        Random r = new Random();
        int randomItem = r.nextInt(zips.size());
        this.zipcode = zips.get(randomItem);
    }

    //Getter and Setter for member fields
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public long getAccountId() {
        return this.accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    /*
     * Check two transactions are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DetailedTransaction that = (DetailedTransaction) o;
        return accountId == that.accountId && timestamp == that.timestamp && Double.compare(that.amount, amount) == 0 && zipcode.equals(that.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, timestamp, amount, zipcode);
    }

    @Override
    public String toString() {
        return "DetailedTransaction{" +
                "accountId=" + accountId +
                ", timestamp=" + timestamp +
                ", amount=" + amount +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}
