/**
 * Factory class for creating and retrieving play strategies.
 * This class provides a mechanism to get instances of different play strategies using the Singleton pattern.
 * This ensures that only one instance of each strategy is created and reused.
 */
public class PlayStrategyFactory {

    private static PlayStrategyFactory playStrategyFactory = null;

    private PlayStrategyFactory(){

    }

    public static PlayStrategyFactory getInstance(){
        return playStrategyFactory == null ? playStrategyFactory = new PlayStrategyFactory() : playStrategyFactory;
    }

    /**
     * Returns an instance of the requested play strategy.
     * If the strategy instance does not exist, it creates one and returns it.
     * If the strategy instance already exists, it returns the existing instance.
     *
     * @param type The type of play strategy requested. Valid values are "Random", "Basic", "Clever", and "Fundamental".
     * @return An instance of the requested play strategy or null if the type is not recognized.
     */
    public IPlayStrategy getStrategy(String type) {
        if (type.equals("Random")) return getRandomStrategy();
        if (type.equals("Basic")) return getBasicStrategy();
        if (type.equals("Clever")) return getCleverStrategy();
        if (type.equals("Fundamental")) return getFundamentalStrategy();
        return null;
    }

    private IPlayStrategy getRandomStrategy(){
        return new RandomStrategy();
    }

    private IPlayStrategy getBasicStrategy(){
        return new BasicStrategy();
    }

    private IPlayStrategy getCleverStrategy(){
        return new CleverStrategy();
    }

    private IPlayStrategy getFundamentalStrategy(){
        return new FundamentalStrategy();
    }
}
