import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		System.out.println("Welcome to the village simulator!");
		System.out.println("(Where there's nothing to eat and your neighbors are monsters.)\n");
		
		GameManager gameMgr = new GameManager();
		
		Scanner input = new Scanner(System.in);
		
		boolean bContinue = true;
		while (bContinue) {
			
			int numHumans = -1;
			int numVampires = -1;
			int numWerewolves = -1;
			int numTurns = -1;
			
			// perform input validation
			boolean bValidating = true;
			while (bValidating) {
				System.out.println("Please enter starting number of humans");
				numHumans = input.nextInt();
				
				bValidating = !gameMgr.InputValid(numHumans);
			}
			
			
			bValidating = true;
			while (bValidating) {
				System.out.println("Please enter starting number of vampires");
				numVampires = input.nextInt();
				
				bValidating = !gameMgr.InputValid(numVampires);
			}
			
			
			bValidating = true;
			while (bValidating) {
				System.out.println("Please enter starting number of werewolves");
				numWerewolves = input.nextInt();
				
				bValidating = !gameMgr.InputValid(numWerewolves);
			}
			
			
			bValidating = true;
			while (bValidating) {
				System.out.println("Please enter number of game turns to simulate");
				numTurns = input.nextInt();
				
				bValidating = !gameMgr.InputValid(numTurns);
			}
			
			
			gameMgr.InitializeNewGame(numHumans, numVampires, numWerewolves);
			
			gameMgr.Run(numTurns);
			
			System.out.println("\n\nWould you like to run another simulation? (Y/N)");
			String strYesNo = input.next().toLowerCase();
			if (false == strYesNo.startsWith("y")) {
				bContinue = false;
			}
		}
		
		input.close();
	}
}
