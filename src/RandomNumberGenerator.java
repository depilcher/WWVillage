//////////////////////////////////
//	RandomNumberGenerator
//
//  Generates random numbers.
//  Implemented using singleton pattern.
//  Random numbers used in several places
//  in simulation.  The singleton pattern
//  facilitates access to an application-wide
//  common RNG implementation.
//  
//////////////////////////////////

import java.util.Random;

public class RandomNumberGenerator {
	
	private static RandomNumberGenerator mInstance = null;
	
	private Random mRng;
	
	
	private RandomNumberGenerator() {
		mRng = new Random();
	}
	
	
	public static RandomNumberGenerator Inst() {
		if (mInstance == null) {
			mInstance = new RandomNumberGenerator();
		}
		
		return mInstance;
	}
	
	
	// generate random integer between
	// min and max (inclusive)
	public int Generate(int min, int max) {
		int randomValue = mRng.nextInt(max - min + 1) + min;
		
		return randomValue;
	}
}
