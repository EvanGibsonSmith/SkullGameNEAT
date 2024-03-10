package src.game_objects;

import java.util.Stack;
import java.util.Scanner;

public class PlacingRoundPlayer {
    Hand hand;
    Stack<Card> playedCards;

    // TODO potentially protected so that it can be used in player to tie these together but not elsewhere?
    public PlacingRoundPlayer(Hand hand, Stack<Card> playedCards) {
        this.hand = hand;
        this.playedCards = playedCards;
    }

    // TODO is this extra constructor useful?
    public PlacingRoundPlayer(Hand hand) {
        this.hand = hand;
        playedCards = new Stack<>();
    }

    public Hand getHand() {
        return this.hand;
    }

    public int getStackSize() {return playedCards.size();}
    
    private Card popCard(boolean isSkull) {
        if (isSkull) {
            return hand.popSkull();
        }
        else {
            return hand.popFlower();
        }
    }
    
    /**
     * Returns a boolean array of the options that the play has for this round
     * at this moment in time. This could be playing a skull or flower
     * @return and array of true and false of length 2, representing the possible choices of the player,
     *         in the order of [skullBoolean, flowerBoolean]
     */
    public boolean[] options() {
        boolean[] optionsOutput = {false, false};
        if (hand.getMultiSet().contains(new Card("skull"))) {optionsOutput[0]=true;}
        if (hand.getMultiSet().contains(new Card("flower"))) {optionsOutput[1]=true;}

        return optionsOutput;
    }

    // TODO document. TODO catch exception potentially throws by popCard if there is no flower/skull and it is played
    public void placeCard(boolean isSkull) {
        Card toPlay = popCard(isSkull);
        playedCards.add(toPlay);
    }

    public void placeCard(int isSkull) {
        boolean isSkullBoolean = false;
        if (isSkull==0) {isSkullBoolean=true;}
        else if (isSkull==1) {isSkullBoolean=false;}
        else {
            // TODO handle this, invalid case
        }
        placeCard(isSkullBoolean);
    }

    // TODO potentially make this an abstract class so that this chooseCard can be extended? Right now it can just be overridden which should also be fine
    // TODO document. Returns flag for if PlacingRound was ended by this player.
    public boolean decide(boolean canBeginBetting) { // TODO make this catch exception if the choice they made isn't valid thrown by placeCard
        // for human player this is just querying to the terminal, could be implemneted by other players in other ways
        Scanner scnr = new Scanner(System.in);
        System.out.println("Would you like to play Skull, Flower, or begin betting round? Type 0 for skull, 1 for flower and 2 for beginning betting round");
        int decision = scnr.nextInt();
        //scnr.close(); TODO not closing right now (which isn't great) so user can continue to give inputs
        // process decision
        if (decision==2) {
            return true; // placing round over flag is raised
        }
        else {
            placeCard(decision);
        }
        return false;
    }
}
