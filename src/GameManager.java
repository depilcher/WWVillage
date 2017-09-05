//////////////////////////////////
//	GameManager
//
//  Responsible for setting up initial
//  conditions of village and running
//  correct number of turns.  Also checks
//  for early termination conditions.
//
//  Pertinent rules:
//  - Each "day" consists of a turn.
//  - The simulation ends early if there are no valid actions left for any citizen.
//  - Dead citizens take no actions.
//
//////////////////////////////////

public class GameManager {

	private Village mVillage;
	
	
	public GameManager() {
		mVillage = null;
	}
	
	
	public void InitializeNewGame(int numHumans, int numVampires, int numWerewolves) {
		
		// setup new village with initial population
		mVillage = new Village(numHumans, numVampires, numWerewolves);
		
		// reset turn counter
		TurnManager.Inst().ResetCount();
	}
	
	
	/// Minimal input validation
	public boolean InputValid(int value) {
		return (value >= 0 && value < Integer.MAX_VALUE);
	}
	
	
	/// Run game for specified number of turns
	public void Run(int numTurns) {
		
		while (TurnManager.Inst().CurrentTurn() < numTurns) {
			
			// check early termination conditions
			if (true == EarlyTermination()) {
				break;
			}
			
			// run next turn
			mVillage.DoTurn();
			
			// increment turn counter
			TurnManager.Inst().Increment();
		}
		
		System.out.println("Total turns run: " + String.valueOf(TurnManager.Inst().CurrentTurn()));
		System.out.print(mVillage.GetStatus());
	}
	
	
	/// Check early termination conditions
	/// NOTE:  Pertinent rules are: "Dead citizens take no actions." and
	/// "no valid actions left for any citizens".  "Valid actions" is
	/// somewhat vague.  I interpret humans starvation as a valid action
	/// since this will change the living population of the village. So 
	/// as long as a human is alive the game will continue.  Vampires 
	/// and werewolves don't go hungry (according to the rules) so their
	/// only valid actions are victimizing other citizens.  Given that 
	/// neither victimize their own kind, the game will terminate early if
	/// all citizens are vampires or all citizens are werewolves.
	private boolean EarlyTermination() {
		
		return mVillage.AllDead() || mVillage.AllVampires() || mVillage.AllWerewolves();
	}
}
