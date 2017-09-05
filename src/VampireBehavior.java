//////////////////////////////////
//	VampireBehavior
//
//  Implements vampire specific behavior.
// 
//  Pertinent rules:
//  Vampires will attempt a conversion of a human once a day. There are 4 possible outcomes: 
//  1) They convert a human, they succeed, the human becomes a vampire,
//     their hunger is reduced by half and their health increases by 10.
//  2) They attempt to convert a human, they kill the human instead. Their health is increased by 50.
//  3) They attempt to convert a human, they fail, but the human survives.
//     The vampire gains 1 health per 5 health he can take from the human, up to 25 max
//     (it will attempt to get as much as possible. If the human is killed, see #2).
//  4) They attempt to convert a human who is actually a werewolf (but not in werewolf form),
//     reducing between 5-40 health from the target. They gain no health from this encounter.
//     This human cannot be converted to a vampire, so they just lose health.
//
//////////////////////////////////

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class VampireBehavior implements IBehavior {

	private IVictimProvider mVictimProvider;
	
	public VampireBehavior(IVictimProvider victimProvider) {
		mVictimProvider = victimProvider;
	}
	
	/// Vampire specific turn behavior
	/// NOTE:  Rules don't specify anything about vampires getting hungry
	/// so I'm not implementing any such behavior.  The only time a vampire
	/// will have non-zero hunger is if they've been converted from a human.
	@Override
	public void DoTurn(Citizen citizen) {
			
		// find suitable victim
		Citizen victim = SelectVictim();
		
		if (victim != null) {
			// first check if victim is a werewolf in human form.
			if (victim.GetType() == Citizen.ECitizenType.WEREWOLF)
			{
				WerewolfConverion(citizen, victim);
			}
			else
			{	// generate random human conversion outcome
				int MIN_OUTCOME = 0;
				int MAX_OUTCOME = 2;
				int outcome = RandomNumberGenerator.Inst().Generate(MIN_OUTCOME, MAX_OUTCOME);
				
				switch (outcome) {
				case 0:
					SuccessfulConversion(citizen, victim);
					break;
					
				case 1:
					VictimKilled(citizen, victim);
					break;
					
				case 2:
					FailedConversion(citizen, victim);
					break;
				}
			}
		}
	}
	
	
	/// Vampires always look like vampires
	@Override
	public Citizen.ECitizenType GetApparentType(Citizen citizen) {
		return Citizen.ECitizenType.VAMPIRE;
	}

	
	/// Successful conversion action
	private void SuccessfulConversion(Citizen citizen, Citizen victim) {
		
		// human is converted to vampire
		victim.SetBehavior(new VampireBehavior(mVictimProvider));
		
		// hunger reduced by half
		citizen.Hunger(citizen.Hunger() / 2);
		
		// health increased by 10
		int HEALTH_INCREASE = 10;
		citizen.Health(citizen.Health() + HEALTH_INCREASE);
	}
	
	
	/// Victim killed action
	private void VictimKilled(Citizen citizen, Citizen victim) {
		
		// victim is killed
		victim.Killed(Citizen.EKillType.VAMPIRE);
		
		// health increased by 50
		int HEALTH_INCREASE = 50;
		citizen.Health(citizen.Health() + HEALTH_INCREASE);
	}
	
	
	/// Failed conversion action
	private void FailedConversion(Citizen citizen, Citizen victim) {
		// victim loses up to 25 health points
		int MIN_HIT = 0;
		int MAX_HIT = 25;
		int healthHit = RandomNumberGenerator.Inst().Generate(MIN_HIT, MAX_HIT);
		
		victim.Health(victim.Health() - healthHit);
		
		// vampire gains 1 point for each 5 taken from victim
		int healthIncrease = healthHit / 5;
		citizen.Health(citizen.Health() + healthIncrease);
		
		// check for death
		if (victim.Health() == Citizen.HEALTH_MIN) {
			VictimKilled(citizen, victim);
		}
	}
	
	
	/// Attempted werewolf conversion action
	private void WerewolfConverion(Citizen citizen, Citizen victim) {
		// victim loses between 5 - 40 (inclusive) health points
		int MIN_HIT = 5;
		int MAX_HIT = 40;
		
		int healthHit = RandomNumberGenerator.Inst().Generate(MIN_HIT, MAX_HIT);
		victim.Health(victim.Health() - healthHit);
		
		// check for death
		if (victim.Health() == Citizen.HEALTH_MIN) {
			victim.Killed(Citizen.EKillType.VAMPIRE);
		}
	}

	
	/// Select victim.  Must be living
	/// and appear to be human.
	private Citizen SelectVictim() {
		
		List<Citizen> lstVictimPool = mVictimProvider.GetVictimPool();
		
		// refine selection with "appears human"
		Predicate<Citizen> vicCriteria = (c -> c.GetApparentType() == Citizen.ECitizenType.HUMAN);
		
		List<Citizen> lstVictims = lstVictimPool.stream().filter(vicCriteria).collect(Collectors.toList());
		
		if (lstVictims.isEmpty()) {
			return null;
		}
		else {	// select by random index
			int MIN_INDEX = 0;
			int MAX_INDEX = lstVictims.size() - 1;
			int victimIndex = RandomNumberGenerator.Inst().Generate(MIN_INDEX, MAX_INDEX);
			return lstVictims.get(victimIndex);
		}
	}
}
