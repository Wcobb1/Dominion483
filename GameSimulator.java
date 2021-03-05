package dominionAgents;

public class GameSimulator {

	private Kingdom kingdom;
	private PlayerCommunication pc;
	private Player p1;
	private Player p2;
	private int[] scores = {0,0};
	private int turn = 1;
	private int cardsPlayed = 0;
	
	public GameSimulator() {
		kingdom = new Kingdom();
		
		pc = new PlayerCommunication();
		
		p1 = new BasicBotV1_0(kingdom, pc);
		p2 = new BasicBotV1_0(kingdom, pc);
		
		pc.addPlayer(p1);
		pc.addPlayer(p2);
	}
	
	private boolean gameOver() {
		//If there are more than 2 empty supply piles, or an empty Province pile, the game is over
		if(kingdom.emptySupplyPiles() > 2 || kingdom.getSupplyPiles().get(0).getCardsRemaining() < 1) {
			return true;
		}
		return false;
	}
	
	//p1 wins: return 0; p2 wins: return 1; draw: return 2;
	private int getWinner() {
		scores[0] = p1.getVictoryPoints();
		scores[1] = p2.getVictoryPoints();
		int p1Score = scores[0];
		int p2Score = scores[1];
		//draw
		if(p1Score == p2Score) {
			return 2;
		}
		//p1 wins
		if(p1Score > p2Score) {
			return 0;
		}
		//p2 wins
		return 1;
	}
	
	public int runGame() {
		while(!gameOver()) {
			turn ++;
			//System.out.println("Turn " + turn + "; Player 1 ---------");
			p1.takeTurn();
			cardsPlayed += p1.getNumCardsPlayed();
			//System.out.println(p1.getTurnLog());
			if(!gameOver()) {
				//System.out.println("Turn " + turn + "; Player 2 ---------");
				p2.takeTurn();
				cardsPlayed += p2.getNumCardsPlayed();
				//System.out.println(p2.getTurnLog());
			}
		}
		return getWinner();
	}
	
	public int[] getScores() {
		return scores;
	}
	
	public int getTurns() {
		return turn;
	}
	
	public int getCardsPlayed() {
		return cardsPlayed;
	}
	
}
