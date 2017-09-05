//////////////////////////////////
//	IBehavior
//
//  Type specifc citizen behavior
//  (human, vampire, werewolf) implemented
//  using strategy pattern.  This facilitates
//  the conversion of citizens from human
//  to vampire/werewolf.  It also facilitates
//  implementing additional behaviors in
//  the future.
//
//////////////////////////////////

public interface IBehavior {
	
	/// perform turn action
	/// return false if nothing to do
	public void DoTurn(Citizen citizen);
	
	/// Returns the type of citizen this person
	/// appear to be. (Only for werewolves at this point.)
	public Citizen.ECitizenType GetApparentType(Citizen citizen);
}
