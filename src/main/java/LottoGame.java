import java.util.Date;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize(value = "")
public class LottoGame extends Game
{   
    private Date date;
    
    public LottoGame(Date date)
    {
        this.date = date;
    }
    
    @SuppressWarnings("deprecation") //too lazy to use a proper date formatter...
    @Override
    public String getTitle()
    {
        return "Losung vom " + date.getDate() + "." + date.getMonth() + "." + date.getYear();
    }
}
