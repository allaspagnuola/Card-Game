import java.util.ArrayList;

/**
 * Abstract class representing a play strategy.
 * This class provides a base for different play strategies and uses the "Fundamental" strategy as the default.
 */
public abstract class CompositePlayStrategy implements IPlayStrategy {

    /**
     * The default play strategy, set to "Fundamental".
     */
    protected ArrayList<IPlayStrategy> strategies = new ArrayList<>();

    CompositePlayStrategy(){
        strategies.add(PlayStrategyFactory.getInstance().getStrategy("Fundamental"));
    }
}
