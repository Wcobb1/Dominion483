package dominionAgents;

import java.util.ArrayList;

import dominionAgents.CardData.CardType;

public class PlayerCommunication {

	private ArrayList<Player> players;
	
	public PlayerCommunication() {
		players = new ArrayList<Player>();
	}
	
	public PlayerCommunication(PlayerCommunication pc){
		this.players = new ArrayList<Player>(pc.players);
	}

	public PlayerCommunication(Player p1, Player p2) {
		players = new ArrayList<Player>();
		players.add(p1);
		players.add(p2);
	}
	
	public void addPlayer(Player p) {
		players.add(p);
	}
	
	public static enum MessageCode {
		ENEMY_WITCH_PLAYED,
		DRAW_CARD,
		ENEMY_MILITIA_PLAYED,
		ENEMY_BUREAUCRAT_PLAYED
		//Spy and Thief implemented in Player.java
	}
	
	public static enum PlayerCode {
		SELF,
		ENEMY
	}
	
	public void sendMessageToAllOtherPlayers(Player sender, MessageCode mc) {
		for(Player p: players) {
			if(p != sender) {
				p.acceptMessage(mc);
			}
		}
	}
	
	public void playThief(Player sender) {
		ArrayList<Card> trashedCards = new ArrayList<Card>();
		//Trash cards(if any)
		for(Player p:players) {
			if(p != sender) {
				//Only perform attack if player does not have moat in hand
				if(!p.cardInHand("MOAT")) {
					Card c1 = p.getDeck().popTopCard();
					Card c2 = p.getDeck().popTopCard();
					if(c1 != null) {
						if(c2 != null) {
							if(c1.isCardType(CardType.TREASURE)) {
								if(c2.isCardType(CardType.TREASURE)) {
									//Trash more valuable treasure, discard other
									if(c1.getCoinsAdded() >= c2.getCoinsAdded()) {
										trashedCards.add(c1);
										p.getDeck().addCardToDiscard(c2);
									}else {
										trashedCards.add(c2);
										p.getDeck().addCardToDiscard(c1);
									}
								}else {
									//trash c1, discard c2
									trashedCards.add(c1);
									p.getDeck().addCardToDiscard(c2);
								}
							}else {
								if(c2.isCardType(CardType.TREASURE)) {
									//trash c2, discard c1
									trashedCards.add(c2);
									p.getDeck().addCardToDiscard(c1);
								}else {
									//Neither card was a treasure card; discard both
									p.getDeck().addCardToDiscard(c1);
									p.getDeck().addCardToDiscard(c2);
								}
							}
						}else {
							//c1 != null, but c2 == null
							if(c1.isCardType(CardType.TREASURE)) {
								trashedCards.add(c1);
							}
						}
					}
				}
			}
		}
		
		//choose whether or not to gain trashed cards
		for(int i = 0;i < trashedCards.size();i ++) {
			boolean gain = sender.chooseToGainCard(trashedCards.get(i));
			if(gain) {
				//player gains card
				sender.getDeck().addCardToDiscard(trashedCards.get(i));
				trashedCards.remove(i);
				i --;
			}else {
				//trash card
				sender.getKingdom().addCardToTrashPile(trashedCards.get(i));
				trashedCards.remove(i);
				i --;
			}
		}
		
	}
	
	public void playSpy(Player sender) {
		for(Player p: players) {
			if(p == sender || !p.cardInHand("MOAT")) {
				Card c = p.getDeck().popTopCard();
				if(c != null) {
					boolean discard;
					if(p != sender) {
						discard = sender.discardOrPutBack(c, PlayerCode.ENEMY);
					}else {
						discard = sender.discardOrPutBack(c, PlayerCode.SELF);
					}
					if(discard) {
						p.getDeck().addCardToDiscard(c);
					}else {
						p.getDeck().placeCardOnDeck(c);
					}
				}
			}
		}
	}
	
}