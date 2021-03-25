//package bad_bank_exam2;

// A Java program to demonstrate working of synchronized. 
// Philip Garcia CS251
// The averge run time of bad bank over 3 runs is 5ms.
// The averge run time of good bank over 3 runs is 55ms.  There was ~10x increase in runtime to make a bad bank, good

import java.io.*;
import java.util.*;

// Driver class 
public class SyncDemo {
	public static void main(String args[]) {
		BankAccount account = new BankAccount(10); // 10 cents
		ThreadedDeposit depositer = new ThreadedDeposit(account);
		ThreadedWithdraw withdrawer = new ThreadedWithdraw(account);

		// Start two threads
		long startTime = System.currentTimeMillis();
		depositer.start();
		withdrawer.start();

		// wait for threads to end
		try {
			// When we invoke the join() method on a thread, the calling thread goes into a
			// waiting state. It remains in a waiting state until the referenced thread
			// terminates.
			// Source: www.baeldung.com/java-thread-join
			depositer.join();
			withdrawer.join();
		} catch (Exception e) {
			System.out.println("Interrupted");
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.printf("\nTotal Runtime Multi-Thread %d miliseconds\n\n", totalTime);
		System.out.printf("Amount in account: %,d \u00A2 \n", account.getAmount());
		int expected = 10 + 5000000 - 1000000;
		System.out.printf("I expect to have %,d \u00A2 \n", expected);
	}
}