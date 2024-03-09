import java.util.Stack;
import java.util.Scanner;

// TODO potentially this would work best split into a bbetting player and a round player, useful for AI potentially as well
public class PlacingRoundPlayer {
    Hand hand;
    Stack<Card> playedCards;

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
        if (hand.getMultiSet().contains(new SkullCard())) {optionsOutput[0]=true;}
        if (hand.getMultiSet().contains(new FlowerCard())) {optionsOutput[1]=true;}

        return optionsOutput;
    }

    // TODO document. TODO catch exception potentially throws by popCard if there is no flower/skull and it is played
    public void placeCard(boolean isSkull) {
        Card toPlay = popCard(isSkull);
        playedCards.add(toPlay);
    }

    private void placeCard(int isSkull) {
        boolean isSkullBoolean = false;
        if (isSkull==0) {isSkullBoolean=false;}
        else if (isSkull==1) {isSkullBoolean=true;}
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
        scnr.close();
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
