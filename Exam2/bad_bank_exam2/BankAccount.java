//package bad_bank_exam2;

public class BankAccount {
	private int every_last_penny;
	private Object lock = new Object();

	// Constructor
	public BankAccount(int amount) {
		this.every_last_penny = amount;
	}

	public int getAmount() {
		return every_last_penny;
	}

	public void deposit(int amount) {
		synchronized (lock) {
			this.every_last_penny += amount;
		}
	}

	public void withdraw(int amount) {
		synchronized (lock) {
			this.every_last_penny -= amount;
		}
	}
}