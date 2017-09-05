//////////////////////////////////
//	IVictimProvider
//
// Interface implemented by Village class
// to provide access to village population.
// Not absolutely necessary, but no need
// to expose entire Village interface to
// behaviors.
//
//////////////////////////////////

import java.util.List;


public interface IVictimProvider {
	public List<Citizen> GetVictimPool();
}
