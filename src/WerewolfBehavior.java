//////////////////////////////////
//	WerewolfBehavior
//
//  Implements werewolf specific behavior.
// 
//  Pertinent rules:
//  - They behave as normal humans, except every 7 days, they become a werewolf.
//  - When they are a werewolf, they act differently.
//    When in this state, they will try and feed.
//    In a feeding, they will reduce between 30-80 health from a human.
//    If they try and fail to reduce a human's health to 0, that human then becomes a werewolf.
//    Werewolves cannot be converted to vampires, only killed by vampires.
//    If a werewolf encounters a vampire, they will reduce between 10-30 health of the vampire.
//  - Werewolves have healing power. They heal 5 health per day as humans, 10 health when they're a werewolf.
//
//////////////////////////////////

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class WerewolfBehavior implements IBehavior {

	private static int TurnThreshold = 7;
	
	private IVictimProvider mVictimProvider;
	
	
	public WerewolfBehavior(IVictimProvider victimProvider)
	{
		mVictimProvider = victimProvider;
	}
	
	
	/// Werewolf specific turn behavior
	/// NOTE:  Given that werewolves only increase their health
	/// value when feeding (and don't decrease their hunger value), and
	/// given that they heal 5 points per day which is the same amount
	/// humans lose when starving, I'm interpreting the rule "They behave as
	/// normal humans..." to mean "they APPEAR as normal humans...".  
	@Override
	public void DoTurn(Citizen citizen) {
		
		// behaves as human unless full moon
		if (true == IsFullMoon()) {
			
			// heal 10 points per day as werewolf
			int HEALTH_INCREASE = 10;
			citizen.Health(citizen.Health() + HEALTH_INCREASE);
						
			// find suitable victim
			Citizen victim = SelectVictim();
			
			if (victim != null) {
				if (victim.GetType() == Citizen.ECitizenType.HUMAN) {
					HumanFeeding(citizen, victim);
				}
				else {
					VampireFeeding(citizen, victim);
				}
			}
		}
		else {	// "human" behavior
			
			// heal 5 points per day as human
			int HEALTH_INCREASE = 5;
			citizen.Health(citizen.Health() + HEALTH_INCREASE);
		}
	}

	
	/// Werewolves sometimes look human 
	/// sometimes look like werewolves
	@Override
	public Citizen.ECitizenType GetApparentType(Citizen citizen) {
		if (IsFullMoon()) {
			return Citizen.ECitizenType.WEREWOLF;
		}
		else {
			return Citizen.ECitizenType.HUMAN;
		}
	}
	
	
	/// Helper for determining if appearance
	/// is werewolf or human
	private boolean IsFullMoon() {
		
		// Have first werewolf appearance on
		// turn 6 rather than turn 0.
		return TurnManager.Inst().CurrentTurn() % TurnThreshold == TurnThreshold - 1;
	}
	
	
	private void HumanFeeding(Citizen citizen, Citizen victim) {
		
		// human loses between 30-80 (inclusive) health points
		int MIN_HIT = 30;
		int MAX_HIT = 80;
		int healthHit = RandomNumberGenerator.Inst().Generate(MIN_HIT, MAX_HIT);
		victim.Health(victim.Health() - healthHit);
		
		// if human lives they become a werewolf
		if (victim.Health() > Citizen.HEALTH_MIN) {
			victim.SetBehavior(new WerewolfBehavior(mVictimProvider));
		}
		else {
			victim.Killed(Citizen.EKillType.WEREWOLF);
		}
				
	}
	
	
	private void VampireFeeding(Citizen citizen, Citizen victim) {
		// vampire loses between 10-30 (inclusive) health points
		int MIN_HIT = 10;
		int MAX_HIT = 30;
		int healthHit = RandomNumberGenerator.Inst().Generate(MIN_HIT, MAX_HIT);
		victim.Health(victim.Health() - healthHit);
		
		// check for death
		if (victim.Health() == Citizen.HEALTH_MIN) {
			victim.Killed(Citizen.EKillType.WEREWOLF);
		}
	}
	
	
	/// Select victim.  
	/// The rules don't state anything about werewolf on
	/// werewolf attacks, so I'm assuming they don't attach
	/// eachother.
	private Citizen SelectVictim() {
		List<Citizen> lstVictimPool = mVictimProvider.GetVictimPool();
		
		// refine selection with "not a werewolf"
		Predicate<Citizen> vicCriteria = (c -> c.GetType() != Citizen.ECitizenType.WEREWOLF);
		
		List<Citizen> lstVictims = lstVictimPool.stream().filter(vicCriteria).collect(Collectors.toList());
		
		if (lstVictims.isEmpty()) {
			return null;
		}
		else {	// select with random index
			int MIN_INDEX = 0;
			int MAX_INDEX = lstVictims.size() - 1;
			int victimIndex = RandomNumberGenerator.Inst().Generate(MIN_INDEX, MAX_INDEX);
			return lstVictims.get(victimIndex);
		}
	}
}
