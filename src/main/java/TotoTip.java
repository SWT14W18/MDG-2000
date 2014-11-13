import java.util.Map;

public class TotoTip extends Tip
{
    private Map<Long, TotoValue> tips;
    
    public TotoTip(Map<Long, TotoValue> tips)
    {
        this.tips = tips;
    }
    
}
