import ch.aplu.jcardgame.Hand;

/**
 * Factory class to create and return instances of different types of players.
 * This class provides a method to instantiate players based on a specified type.
 */
public class PlayerFactory {

    private static PlayerFactory playerFactory = null;

    private PlayerFactory(){

    }

    public static PlayerFactory getInstance(){
        return playerFactory == null ? playerFactory = new PlayerFactory() : playerFactory;
    }

    /**
     * Returns a player of the specified type with the given hand of cards.
     *
     * @param type The type of player to be created. Valid types are "random", "basic", "clever", and "human".
     * @param hand The hand of cards for the player.
     * @return A player of the specified type or null if the type is not recognized.
     */
    public Player getPlayer(String type, Hand hand){
        if(type.equals("random")) return getRandomPlayer(hand);
        if(type.equals("basic")) return getBasicPlayer(hand);
        if(type.equals("clever")) return getCleverPlayer(hand);
        if(type.equals("human")) return getHumanPlayer(hand);
        // set default player type as random when the player configuration type in property is empty
        return getRandomPlayer(hand);
    }

    private Player getRandomPlayer(Hand hand){
        return new RandomPlayer(hand);
    }

    private Player getBasicPlayer(Hand hand){
        return new BasicPlayer(hand);
    }

    private Player getCleverPlayer(Hand hand){
        return new CleverPlayer(hand);
    }

    private Player getHumanPlayer(Hand hand){
        return new HumanPlayer(hand);
    }
}
