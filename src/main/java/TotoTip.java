import java.util.Map;

public class TotoTip extends Tip
{
    private Map<Long, TotoValue> tips;
    
    public TotoTip(Game game, Map<Long, TotoValue> tips)
    {
        super(game);
        this.tips = tips;
    }
    
}
