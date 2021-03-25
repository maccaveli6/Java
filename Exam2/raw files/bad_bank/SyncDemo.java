// A Java program to demonstrate working of synchronized. 
import java.io.*; 
import java.util.*; 

// Driver class 
public class SyncDemo
{ 
    public static void main(String args[]) 
    {
        BankAccount account = new BankAccount(10); //10 cents 
        ThreadedDeposit depositer = new ThreadedDeposit(account); 
        ThreadedWithdraw withdrawer = new ThreadedWithdraw(account);
  
        // Start two threads
        depositer.start(); 
        withdrawer.start(); 
  
        // wait for threads to end 
        try
        {
			//When we invoke the join() method on a thread, the calling thread goes into a waiting state. It remains in a waiting state until the referenced thread terminates.
			//Source: www.baeldung.com/java-thread-join
            depositer.join(); 
            withdrawer.join(); 
        } 
        catch(Exception e) 
        { 
            System.out.println("Interrupted"); 
        }
		
		System.out.printf("Amount in account: %,d \u00A2 \n",account.getAmount());
		int expected = 10+5000000-1000000;
		System.out.printf("I expect to have %,d \u00A2 \n",expected);
    } 
}