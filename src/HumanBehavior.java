//////////////////////////////////
//	HumanBehavior
//
//  Implements human specific behavior
// 
//  Pertinent rules:
//  Humans' hunger increases every turn.
//  If their hunger is already 9, they lose 5 health.
//
//////////////////////////////////

public class HumanBehavior implements IBehavior {

	private static final int HUNGER_PER_TURN = 1;
	
	private static final int STARVATION_HEALTH_COST = 5;
	
	@Override
	public void DoTurn(Citizen citizen) {
		
		if (citizen.Hunger() < Citizen.HUNGER_MAX)
		{
			citizen.Hunger(citizen.Hunger() + HUNGER_PER_TURN);
		}
		else
		{
			citizen.Health(citizen.Health() - STARVATION_HEALTH_COST);
		}
		
		// check for starvation
		if (citizen.Health() == Citizen.HEALTH_MIN) {
			citizen.Killed(Citizen.EKillType.HUNGER);
		}
	}
	
	
	/// Humans always look like humans
	public Citizen.ECitizenType GetApparentType(Citizen citizen) {
		
		return Citizen.ECitizenType.HUMAN;
	}

}
