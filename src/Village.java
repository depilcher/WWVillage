//////////////////////////////////
//	Village
//
//  Manages village population.
//  Implements IVictimProvider interface
//  to give population access to 
//  behavior objects.
//
//  Pertinent rules:
//  The village has many different citizens:
//  Humans, Vampires, Werewolves
//
//////////////////////////////////

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Village implements IVictimProvider {

	// List of all citizens in village
	private ArrayList<Citizen> mCitizens;
	
	
	/// Initialize village population
	public Village(int numHumans, int numVampires, int numWerewolves)
	{
		mCitizens = new ArrayList<Citizen>(numHumans + numVampires + numWerewolves);
		
		int count = 0;
		while (count < numHumans)
		{
			mCitizens.add(CreateHuman());
			++count;
		}
		
		count = 0;
		while (count < numVampires)
		{
			mCitizens.add(CreateVampire());
			++count;
		}
		
		count = 0;
		while (count < numWerewolves)
		{
			mCitizens.add(CreateWerewolf());
			++count;
		}
	}
	
	
	/// Run one turn on citizens
	public void DoTurn()
	{
		/// shuffle citizens before each turn
		RandomizeCitizens();
		
		mCitizens.forEach( citizen -> citizen.DoTurn() );
	}
	
	
	/// Helper method for checking
	/// early termination conditions.
	public boolean AllDead() {
		
		List<Citizen> lstAlive = GetLivingCitizens();
		
		return lstAlive.isEmpty();
	}
	
	
	/// Helper method for checking
	/// early termination conditions.
	/// Check if living citizens are
	/// all vampires.
	public boolean AllVampires() {
		
		List<Citizen> lstLiving = GetLivingCitizens();
		
		Predicate<Citizen> isVampire = ( c -> c.GetType() == Citizen.ECitizenType.VAMPIRE );
		
		return lstLiving.stream().allMatch(isVampire);
	}
	
	
	/// Helper method for checking
	/// early termination conditions.
	/// Check if living citizens are
	/// all werewolves.
	public boolean AllWerewolves() {
		
		List<Citizen> lstLiving = GetLivingCitizens();
		
		Predicate<Citizen> isWerewolf = ( c -> c.GetType() == Citizen.ECitizenType.WEREWOLF );
		
		return lstLiving.stream().allMatch(isWerewolf);
	}
	
	
	/// From IVictimProvider
	public List<Citizen> GetVictimPool()
	{
		// In all cases victims must be living.
		// Behaviors may further refine victim
		// selection
		return GetLivingCitizens();
	}
	
	
	/// Helper method returning all living citizens
	private List<Citizen> GetLivingCitizens() {
		
		Predicate<Citizen> criteria = (c -> c.KilledBy() == Citizen.EKillType.INVALID);
		
		List<Citizen> lstLiving = mCitizens.stream().filter(criteria).collect(Collectors.toList());
		
		return lstLiving;
	}
	
	
	/// randomize citizen list
	private void RandomizeCitizens() {
		Collections.shuffle(mCitizens);
	}
	
	
	/// Human factory method
	private Citizen CreateHuman() {
		return new Citizen( new HumanBehavior() );
	}
	
	
	/// Vampire factory method
	private Citizen CreateVampire() {
		return new Citizen( new VampireBehavior(this) );
	}
	
	
	/// Werewolf factory method
	private Citizen CreateWerewolf() {
		return new Citizen( new WerewolfBehavior(this) );
	}
	
	
	/// Return string of status of all citizens
	public String GetStatus() {
		
		// Easier to view results
		// when grouped by type.
		Predicate<Citizen> isHuman = ( c -> c.GetType() == Citizen.ECitizenType.HUMAN );
		List<Citizen> lstHumans = mCitizens.stream().filter(isHuman).collect(Collectors.toList());
		
		Predicate<Citizen> isVampire = ( c -> c.GetType() == Citizen.ECitizenType.VAMPIRE );
		List<Citizen> lstVampires = mCitizens.stream().filter(isVampire).collect(Collectors.toList());
		
		Predicate<Citizen> isWerewolf = ( c -> c.GetType() == Citizen.ECitizenType.WEREWOLF );
		List<Citizen> lstWerewolves = mCitizens.stream().filter(isWerewolf).collect(Collectors.toList());
		
		StringBuilder sb = new StringBuilder();
		
		for (Citizen citizen : lstHumans) {
			sb.append(citizen.GetStatus());
		}
		
		for (Citizen citizen : lstVampires) {
			sb.append(citizen.GetStatus());
		}
		
		for (Citizen citizen : lstWerewolves) {
			sb.append(citizen.GetStatus());
		}
		
		return sb.toString();
	}
}
