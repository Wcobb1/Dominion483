package dominionAgents;

import java.util.ArrayList;

public class GameSimulator {

	private Kingdom kingdom;
	private PlayerCommunication pc;
	private Player p1;
	private Player p2;
	private PlayerStats p1Stats;
	private PlayerStats p2Stats;
	private int[] scores = {0,0};
	private int turn = 1;
	private int cardsPlayed = 0;
	private boolean p1MoreTurns = false;
	private int[] winnerCards;
	private int winner = -1;
	
	public GameSimulator(Player p1, Player p2) {
		kingdom = p1.getKingdom();
		pc = p1.getPlayerCommunication();
		
		this.p1 = p1;
		this.p2 = p2;
		
		p1Stats = p1.getPlayerStats();
		p2Stats = p2.getPlayerStats();
		
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
		//tied
		if(p1Score == p2Score) {
			//tie-breaker: does p2 have less turns played than p1?
			if(p1MoreTurns) {
				winnerCards = p2Stats.getCardsOwned();
				//p2 wins
				winner = 1;
				return 1;
			}
			winnerCards = p1Stats.getCardsOwned();
			//If not: draw
			winner = 2;
			return 2;
		}
		//p1 wins
		if(p1Score > p2Score) {
			winnerCards = p1Stats.getCardsOwned();
			winner = 0;
			return 0;
		}
		//p2 wins
		winnerCards = p2Stats.getCardsOwned();
		winner = 1;
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
			}else {
				p1MoreTurns = true;
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
	
	public StringInt[][] getDecks(){
		StringInt[][] playerDecks = new StringInt[2][17];
		playerDecks[0] = p1Stats.getDeckOwned();
		playerDecks[1] = p2Stats.getDeckOwned();
		return playerDecks;
	}
	
	public String[] getCardsInKingdom() {
		String[] cards = new String[10];
		ArrayList<SupplyPile> supply = kingdom.getSupplyPiles();
		for(int i = 0;i < supply.size()-7;i ++) {
			cards[i] = supply.get(i+7).getName();
		}
		return cards;
	}
	
	public ArrayList<StringInt> getWinnerCardsOwned() {
		ArrayList<StringInt> cardCounts = new ArrayList<StringInt>();
		for(int i = 0;i < winnerCards.length;i ++) {
			StringInt si = new StringInt(kingdom.cardNameFromIndex(i), winnerCards[i]);
			cardCounts.add(si);
		}
		return cardCounts;
	}
	
	public ArrayList<String> getPlayerCardsGainedFirstTwoTurns(boolean winningPlayer){
		if(winner == 0) {
			if(winningPlayer) {
				return p1Stats.getCardsFirstTwoTurns();
			}
			return p2Stats.getCardsFirstTwoTurns();
		}else{
			if(winningPlayer) {
				return p2Stats.getCardsFirstTwoTurns();
			}
			return p1Stats.getCardsFirstTwoTurns();
		}
	}
	
	public ArrayList<String> getPlayerCardsGainedMidGame(boolean winningPlayer){
		if(winner == 0) {
			if(winningPlayer) {
				return p1Stats.getCardsMidGame();
			}
			return p2Stats.getCardsMidGame();
		}else{
			if(winningPlayer) {
				return p2Stats.getCardsMidGame();
			}
			return p1Stats.getCardsMidGame();
		}
	}
	
	public ArrayList<String> getPlayerCardsGainedLateGame(boolean winningPlayer){
		if(winner == 0) {
			if(winningPlayer) {
				return p1Stats.getCardsLateGame();
			}
			return p2Stats.getCardsLateGame();
		}else{
			if(winningPlayer) {
				return p2Stats.getCardsLateGame();
			}
			return p1Stats.getCardsLateGame();
		}
	}
}
