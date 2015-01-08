package de.tudresden.swt14ws18.useraccountmanager;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import scala.util.Random;
import de.tudresden.swt14ws18.Lotterie;

/**
 * die Klasse "Community" leitet sich wie auch der ConcreteCustomer vom Customer ab und besitzt somit einen Namen und ein Passwort.
 * 
 * Die Community bildet die Gewinngemeinschaft nach, als welche sich Kudnen zusammenschließen können, um gemeinsam am Glücksspiel teilzunehmen. Der
 * Ersteller der Gruppe ist gleichzeitig der Administrator der Gruppe, der Tippscheine kaufen kann. Die restlichen "normalen" Mitglieder können sich
 * an dem Tipp beteiligen, jedoch prozentual nur so weit bis die Grenze erreicht ist, die der Admin gerne selbst beteiligt sein möchte. (z.B. Admin
 * kauft Tippschein mit 20% festgelegter Beteiligung für sich. Alle anderen können die verbleibenden 80% unter sich aufteilen)
 * 
 * Communities sind genau wie ConcreteCustomer an genau einen UserAccount gebunden, besitzen jedoch keinen BankAccount, da das Geld direkt von den
 * Konten der ConcreteCustomer kommt.
 * 
 * @param members
 *            : Ein HashSet der Mitglieder der Gemeinschaft.
 * 
 * @param admin
 *            : Der Administrator der Gruppe
 * 
 * @param userAccount
 *            : Der Useraccount, der der Gemeinschaft zugeordnet ist
 * 
 *
 *
 * @author Reinhard_2
 *
 */
@Entity
public class Community extends Customer {

    @Id
    @GeneratedValue
    private long id;

    @ManyToMany
    private Set<ConcreteCustomer> members = new HashSet<>();

    @ManyToOne
    private ConcreteCustomer admin;

    private String communityName;
    private String communityPassword;

    @Deprecated
    public Community() {
    };

    /**
     * Konstruktor!
     * 
     * @param name
     *            -> Name der Gemeinschaft (frei wählbar)
     * @param password
     *            -> Das Passwort, mit dem man in die Gruppe gelangt (vom Admin(ersteller) festgelegt)
     * @param admin
     *            -> der Kunde, der der Admin der Gruppe sein soll (im Normalfall der Ersteller)
     */
    public Community(String name, String password, ConcreteCustomer admin) {
        super(name, password);
        this.communityName = name;
        this.communityPassword = password;
        this.admin = admin;
        addMember(admin);
    }

    public ConcreteCustomer getAdmin() {
        return admin;
    }

    /**
     * Wenn ein Kunde hinzugefügt werden soll, muss geprüft werden, ob er schon Mitglied der Gruppe ist.
     * 
     * @param newMember
     *            -> Das neue Mitglied vom Typ "ConcreteCustomer"
     * @return true wenn das Mitglied hinzugefügt wurde, false wenn nicht
     */
    public boolean addMember(ConcreteCustomer newMember) {
        if (!isMember(newMember)) {
            return members.add(newMember);
        } else {
            return false;
        }
    }

    /**
     * Ist ein Kunde bereits Mitglied?
     * 
     * @param concreteCustomer
     *            -> für diesen Kuden prüfen
     * @return true wenn der Kunde Mitglied der Gruppe ist, false wenn nicht
     */
    public boolean isMember(ConcreteCustomer concreteCustomer) {
        return members.contains(concreteCustomer);
    }

    /**
     * Ein Kunde tritt aus der Gruppe aus? Dann wird er aus der Memberslist gelöscht
     * 
     * @param leaver
     *            -> Der Kunde, der aus der Gruppe austritt
     * @return true wenn etwas entfernt wurde, false wenn nicht
     */
    public boolean removeMember(ConcreteCustomer leaver) {
        return members.remove(leaver);
    }

    /**
     * Gibt den Namen der Gruppe aus
     * 
     * @return der Gruppenname
     */
    public String getCommunityName() {
        return communityName;
    }

    /**
     * Gibt das Passwort der Gruppe aus
     * 
     * @return das Gruppenpasswort
     */
    public String getCommunityPassword() {
        return communityPassword;
    }

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static Random rnd = new Random();

    /**
     * Gruppenpasswort wird erstellt
     * 
     * @return ein neuer String bestehend aus 6 Zeichen, der aus den Groß- und Kleinbuchstaben A-Z sowie den Zahlen 0-9 bestehen kann.
     */
    public static String createPassword() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    /**
     * Gibt die Mitglieder einer Gruppe aus
     * 
     * @return ein Set aller Mitglieder der Gruppe
     */
    public Set<ConcreteCustomer> getMemberList() {
        return members;
    }

    /**
     * Gibt das Passwort als Klartext zurück, wenn das Mitglied Gruppenadmin ist
     * 
     * @return wenn man Gruppenadmin ist: das Gruppenpasswort, sonst "******"
     */
    public String getPasswordHtml() {
        ConcreteCustomer c = Lotterie.getInstance().getCustomerRepository()
                .findByUserAccount(Lotterie.getInstance().getAuthenticationManager().getCurrentUser().get());
        return getAdmin().equals(c) ? getCommunityPassword() : "******";
    }

    /**
     * Gibt die Id zurück
     * 
     * @return Gruppen-ID
     */
    public long getId() {
        return id;
    }
    

    /**
     * Ist das Mitglied Gruppenadmin?
     * 
     * @return Falls Gruppenadmin: true, sonst false
     */
    public boolean isAdmin() {
    	ConcreteCustomer c = Lotterie.getInstance().getCustomerRepository()
                .findByUserAccount(Lotterie.getInstance().getAuthenticationManager().getCurrentUser().get());
    	if (getAdmin().equals(c)) return true;
    	
    	return false;
    }
    
    /**
     * Ist das Mitglied kein Gruppenadmin?
     * 
     * @return Falls Gruppenadmin: false, sonst true
     */
    public boolean isNoAdmin(){
    	if (isAdmin()) return false;
    	return true;
    }
}
