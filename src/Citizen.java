//////////////////////////////////
//	Citizen
//
//  Represents an individual member
//  of village.  Manages properties
//  common to all village members,
//  including health and hunger.
//
//  Pertinent rules:
//  Each citizen has two attributes:
//  - Hunger (0-9)
//  - Health (0-99)
//
//  - Turn order is considered to be random.
//    No citizen is known to act before any other citizen in a day.
//
//////////////////////////////////

public class Citizen {
	
	public enum ECitizenType {
		INVALID,
		HUMAN,
		VAMPIRE,
		WEREWOLF
	}
	
	public enum EKillType {
		INVALID,
		HUNGER,
		VAMPIRE,
		WEREWOLF
	}

	public static final int HUNGER_MAX = 9;
	public static final int HUNGER_MIN = 0;
	public static final int HEALTH_MAX = 99;
	public static final int HEALTH_MIN = 0;
	
	private int mHunger;
	
	private int mHealth;
	
	private EKillType mKilledBy;
	
	private IBehavior mBehavior;
	
	private boolean mConvertedHuman;
	
	/// Default constructor
	public Citizen(IBehavior behavior) {
		mHunger = HUNGER_MIN;
		mHealth = HEALTH_MAX;
		mKilledBy = EKillType.INVALID;
		mBehavior = behavior;
		mConvertedHuman = false;
	}
	
	
	/// Perform turn action
	public void DoTurn()
	{
		// if still alive perform turn action
		// (this is common for all citizens)
		if (mKilledBy == EKillType.INVALID) {
			mBehavior.DoTurn(this);
		}
	}
	
	
	/// Update turn strategy
	public void SetBehavior(IBehavior newBehavior)
	{
		// flag if human changing type
		if (GetType() == ECitizenType.HUMAN && 
			GetType(newBehavior) != ECitizenType.HUMAN) {
			mConvertedHuman = true;
		}
		
		mBehavior = newBehavior;
	}
	
	
	/// Hunger getter
	public int Hunger() {
		return mHunger;
	}
	
	
	/// Hunger setter
	public void Hunger(int hunger) {
		
		// respect bounds
		if (hunger < HUNGER_MIN) {
			mHunger = HUNGER_MIN;
		}
		else if (hunger > HUNGER_MAX) {
			mHunger = HUNGER_MAX;
		}
		else {
			mHunger = hunger;
		}
	}
	
	
	/// Health getter
	public int Health() {
		return mHealth;
	}
	
	
	/// Health setter
	public void Health(int health) {
		
		// respect bounds
		if (health < HEALTH_MIN) {
			mHealth = HEALTH_MIN;
		}
		else if (health > HEALTH_MAX) {
			mHealth = HEALTH_MAX;
		}
		else
		{
			mHealth = health;
		}
	}
	
	
	/// Killed-by getter
	public EKillType KilledBy() {
		return mKilledBy;
	}
	
	
	/// Killed-by setter
	public void Killed(EKillType killer) {
		
		mKilledBy = killer;
		mHealth = 0;	// for consistency?
	}
	
	
	/// Get type from behavior
	public ECitizenType GetType() {
		
		return GetType(mBehavior);
	}
	
	
	/// Helper for type conversion
	private ECitizenType GetType(IBehavior behavior) {

		ECitizenType type = ECitizenType.INVALID;
		
		if (behavior instanceof WerewolfBehavior) {
			type = ECitizenType.WEREWOLF;
		}
		else if (behavior instanceof VampireBehavior) {
			type = ECitizenType.VAMPIRE;
		}
		else if (behavior instanceof HumanBehavior) {
			type = ECitizenType.HUMAN;
		}
		
		return type;
	}
	
	
	///  Returns type this citizen 
	///  appears to be.
	public ECitizenType GetApparentType() {
		return mBehavior.GetApparentType(this);
	}
	
	
	/// Return string representation 
	/// of citizen status
	public String GetStatus() {
		StringBuilder sb = new StringBuilder();
		
		sb.append(GetTypeString(GetType()));
		
		sb.append("   ");
		
		sb.append("Hunger: " + String.valueOf(mHunger));
		
		sb.append("   ");
		
		sb.append("Health: " + String.valueOf(mHealth));
		
		if (mKilledBy != EKillType.INVALID) {
			sb.append("   ");
			
			sb.append("Killed by: " + GetKilledByString(mKilledBy));
		}
		
		if (true == mConvertedHuman) {
			sb.append("   (converted from human)");
		}
		
		
		sb.append('\n');
		
		return sb.toString();
	}
	
	
	/// Helper for printing citizen state
	private String GetTypeString(ECitizenType type) {
		
		String strType = "";
		
		switch (type) {
		case HUMAN:
			strType = "Human   ";
			break;
			
		case VAMPIRE:
			strType = "Vampire ";
			break;
			
		case WEREWOLF:
			strType = "Werewolf";
			break;
			
		case INVALID:
			strType = "Unknown ";
			break;
		}
		
		return strType;
	}
	
	
	/// Helper for printing citizen state
	private String GetKilledByString(EKillType killType) {
		
		String strType = "";
		
		switch (killType) {
		case HUNGER:
			strType = "Hunger  ";
			break;
			
		case VAMPIRE:
			strType = "Vampire ";
			break;
			
		case WEREWOLF:
			strType = "Werewolf";
			break;
			
		case INVALID:
			strType = "Unknown ";
			break;
		}
		
		return strType;
	}
	
}
