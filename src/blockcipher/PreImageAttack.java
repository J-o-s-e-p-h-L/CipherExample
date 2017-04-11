package blockcipher;

import java.util.ArrayList;
import java.util.Random;

/**
 * An instance of this class accepts a randomly generated target digest value,
 * d, then attempts to find a chaining value h and message m such that Eh(m) =
 * d. The @see PreImageAttack#find100Collisions() will be called to find 100
 * collisions using this technique and print the average attempts per collision,
 * along with some other statistics.
 * 
 * @author Joseph Lewis
 *
 */
public class PreImageAttack {

	private Comp343Cipher bc;
	private byte[] message = new byte[2];
	private byte[] chaining_value = new byte[2];
	private byte[] digest = new byte[2];
	private byte[] targetDigest;

	private int passAvg;

	/**
	 * Constructor to create an instance of the compression function created
	 * from the block cipher in Stage 1 and start the collision searching
	 * process.
	 * 
	 * @param H
	 *            is the given the target digest that collisions are going to be
	 *            found with
	 */
	public PreImageAttack(byte[] H) {
		bc = new Comp343Cipher();
		targetDigest = H;
		passAvg = 0;
		find100Collisions();
	}

	/**
	 * Used to generate digests to search for 100 collisions with the digest
	 * generated from the block given as a parameter in the constructor.
	 */
	public void find100Collisions() {
		ArrayList<Integer> attempts = new ArrayList<Integer>();
		int passes = 0;
		int most = -1;
		int least = -1;
		System.out.println("Printing the next 100 collisions with digest " + BirthdayAttack.toHex(targetDigest) + ".");
		while (attempts.size() < 100) {
			new Random().nextBytes(message);
			new Random().nextBytes(chaining_value);
			// Generate a new digest
			digest = bc.compression(message, chaining_value);
			// If a collision has been found...
			if (BirthdayAttack.toHex(digest).equals(BirthdayAttack.toHex(targetDigest))) {
				System.out.println(
						BirthdayAttack.toHex(message) + " " + BirthdayAttack.toHex(chaining_value) + "\ncollision");
				// Track the most and least attempts for a collision
				if (most == -1 || passes > most) {
					most = passes;
				}
				if (least == -1 || passes < least) {
					least = passes;
				}
				attempts.add(passes);
				// Store the attempt where the collision was made
				attempts.add(passes);
				passes = 0;
			}
			passes++;
		}
		// Calculate the average attempts
		for (int i = 0; i < attempts.size(); i++) {
			passAvg += attempts.get(i);
		}
		passAvg /= attempts.size();
		// Print the statistics of the attack to the console
		Double PIA_TC = Math.pow(2, (16));
		String PIA_Stats = "--- Pre-Image Attack Statistics ---\n";
		PIA_Stats += "Theoretical complexity of the attack is: O(2^(n)) = " + PIA_TC.intValue() + "\n";
		PIA_Stats += "Most attempts until collision: " + most + " | Least: " + least;
		System.out.println(PIA_Stats);
	}

	/**
	 * Getter method that returns the average number of attempts for each
	 * collision in the attack.
	 * 
	 * @return the average number of attempts
	 */
	public int getAvgAttempts() {
		return passAvg;
	}

}
