public abstract class Game
{
    private static long idCounter = 0;
    private long id;
    
    public Game()
    {
        id = idCounter++;
    }
    
    public abstract String getTitle();
    
    public long getID()
    {
        return id;
    }
}
