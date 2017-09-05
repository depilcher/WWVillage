//////////////////////////////////
//	TurnManager
//
//  Responsible for keeping track of
//  and incrementing game turn.
//  Implemented using singleton pattern
//  to ensure current turn is synchronized
//  across entire application.
//
//////////////////////////////////

public class TurnManager {

	private static TurnManager mInstance = null;
	
	private int mTurn;
	
	
	private TurnManager() {
		mTurn = 0;
	}
	
	
	public static TurnManager Inst() {
		
		if (mInstance == null) {
			mInstance = new TurnManager();
		}
		
		return mInstance;
	}
	
	
	public int CurrentTurn() {
		return mTurn;
	}
	
	
	public void Increment() {
		mTurn += 1;
	}
	
	
	public void ResetCount() {
		mTurn = 0;
	}
}
