package DB.components.cards;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.MapKeyColumn;
import java.util.Map;
import java.util.TreeMap;

@Embeddable
public class DeckInfo {
    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @Column(name = "CARD_PLAYED_CNT")
    private Map<String, Integer> cardPlayedCount = new TreeMap<String, Integer>();

    @Column(name = "WIN_CNT")
    private Integer numberOfWins;

    @Column(name = "PLAY_CNT")
    private Integer numberOfPlayedGames;

    public int getCardPlayedCount(Card card) {
        return cardPlayedCount.getOrDefault(card.getName(), 0);
    }

    public void setCardPlayedCount(Card card, int cnt) {
        cardPlayedCount.put(card.getName(), cnt);
    }

    public int getNumberOfWins() {
        if (numberOfWins == null)
            numberOfWins = 0;
        return numberOfWins;
    }

    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    public int getNumberOfPlayedGames() {
        if (numberOfPlayedGames == null)
            numberOfPlayedGames = 0;
        return numberOfPlayedGames;
    }

    public void setNumberOfPlayedGames(int numberOfPlayedGames) {
        this.numberOfPlayedGames = numberOfPlayedGames;
    }

    public DeckInfo(){

    }
}
