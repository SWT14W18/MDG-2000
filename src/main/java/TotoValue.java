
public enum TotoValue
{
    WINHOME(0),
    DRAW(1),
    WINGUEST(2),
    NONE(3);
    
    private int value;
    
    private TotoValue(int value)
    {
        this.value = value;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public static TotoValue getByValue(int value)
    {
        for(TotoValue v : values())
            if(v.getValue() == value)
                return v;
        
        return null;
    }

    public static TotoValue getByValue(String string)
    {
        return getByValue(Integer.parseInt(string));
    }
}
