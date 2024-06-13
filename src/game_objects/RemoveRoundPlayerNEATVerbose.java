package src.game_objects;

public class RemoveRoundPlayerNEATVerbose extends RemoveRoundPlayerNEAT {

    public RemoveRoundPlayerNEATVerbose(Hand hand, String name, int genomeID) {
        super(hand, name, genomeID);
    }

    @Override
    public int decide(Player[] players) {
        int decision = super.decide(players);
        System.out.println(name + " has removed a card");
        return decision;
    }
}
