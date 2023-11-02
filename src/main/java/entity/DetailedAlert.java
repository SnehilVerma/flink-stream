//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package entity;

/*
 * This class creates Detailed Alert
 */
public final class DetailedAlert {

    private long accountId;

    /*
     * Return a string containing associated details with the alert
     */
    @Override
    public String toString() {
        return "DetailedAlert{" +
                "accountId=" + accountId +
                ", timestamp=" + timestamp +
                ", amount=" + amount +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }

    private long timestamp;
    private double amount;

    private String zipcode;

    public DetailedAlert() {
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
