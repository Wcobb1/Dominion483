package dominionAgents;

import java.util.ArrayList;
import java.util.Random;

// The Bot is similar to the rush bot but more laid back, the goal of the ai is to keep buying treasure cards until they are able to buy point cards
// priortizing higher point cards over lower value ones: in pratice the AI would buy try to get cards that allow it to buy more then once if able it buys those cards 
// then when it can and has the buys it first buys more action cards then a tresure card. The idea is to maximize the amount of gold it can play a turn then buy points
// unlike the rush bot it should play it slower focusing more on deck build along side just buying money making cards

// V0: very similar to attackBot when we get a chance we can flush this out more

public class MoneyMakingBotV1_0 {
    
    boolean treasureInSupply = false;
    int actionNum = 0;   
    public MoneyMakingBotV1_0(Kingdom k, PlayerCommunication pc) {
		super(k, pc);

        
    }


    protected void resolveBuyPhase() {
        

        String cardToBuy = cheapestBuy())
        if(coins < 8 && !cardToBuy.isNull()){
            buyCard(cardToBuy);
        }
        while(kingdom.canBuy("Provice") && (coins >= 8){// buy as many provinces as you want
           buyCard("Provice");
        }
            
        

        
	}
    protected String cheapestBuy(){

        System.out.print("hi");



        return s;
    }
    




}
