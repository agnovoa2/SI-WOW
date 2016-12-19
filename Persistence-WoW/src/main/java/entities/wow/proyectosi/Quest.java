package entities.wow.proyectosi;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * JPA Entity Class that represents a Quest in the WoW Universe.
 *
 * @author Andrés Vieira Vázquez
 * @version 1.0
 */
@Entity
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String questName;
    private String questDescription;
    private int minLevel;
    private int expReward;
    private int goldReward;
    private int silverReward;
    private int copperReward;

    @ManyToMany
    @JoinTable(name = "wowcharacter_quest", joinColumns = @JoinColumn(name = "quest_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "wowcharacter_id", referencedColumnName = "id"))
    private List<WowCharacter> wowcharacters;


    /**
     * Return an Integer that represents the unique id of the Quest.
     *
     * @return id of the Quest.
     */
    public int getId() {
        return id;
    }

    /**
     * Return a String that represents the name of a Quest.
     *
     * @return name of the Quest.
     */
    public String getQuestName() {
        return questName;
    }

    /**
     * Set the name of the Quest.
     *
     * @param questName new name for the Quest.
     */
    public void setQuestName(String questName) {
        this.questName = questName;
    }

    /**
     * Return a String that represents the description of a Quest.
     *
     * @return description of the Quest.
     */
    public String getQuestDescription() {
        return questDescription;
    }

    /**
     * Set the description of the Quest.
     *
     * @param questDescription new description for the Quest.
     */
    public void setQuestDescription(String questDescription) {
        this.questDescription = questDescription;
    }

    /**
     * Return an int that represents the experience reward of a Quest.
     *
     * @return experience reward of the Quest.
     */
    public int getExpReward() {
        return expReward;
    }

    /**
     * Set the experience reward of the Quest.
     *
     * @param expReward new experience reward for the Quest.
     */
    public void setExpReward(int expReward) {
        this.expReward = expReward;
    }

    /**
     * Return an int that represents the gold reward of a Quest.
     *
     * @return gold reward of the Quest.
     */
    public int getGoldReward() {
        return goldReward;
    }

    /**
     * Set the gold reward of the Quest.
     *
     * @param goldReward new gold reward for the Quest.
     */
    public void setGoldReward(int goldReward) {
        this.goldReward = goldReward;
    }

    /**
     * Return an int that represents the silver reward of a Quest.
     *
     * @return silver reward of the Quest.
     */
    public int getSilverReward() {
        return silverReward;
    }

    /**
     * Set the silver reward of the Quest.
     *
     * @param silverReward new silver reward for the Quest.
     */
    public void setSilverReward(int silverReward) {
        this.silverReward = silverReward;
    }

    /**
     * Return an int that represents the copper reward of a Quest.
     *
     * @return copper reward of the Quest.
     */
    public int getCopperReward() {
        return copperReward;
    }

    /**
     * Set the copper reward of the Quest.
     *
     * @param copperReward new copper reward for the Quest.
     */
    public void setCopperReward(int copperReward) {
        this.copperReward = copperReward;
    }

    /**
     * Return a List that represents the characters doing this Quest.
     *
     * @return character list for the Quest.
     */
    public List<WowCharacter> getWowcharacters() {
        return wowcharacters;
    }

    /**
     * Set the character list doing this Quest.
     *
     * @param wowcharacters the list of characters doing this Quest.
     */
    public void setWowcharacters(List<WowCharacter> wowcharacters) {
        this.wowcharacters = wowcharacters;
    }

    /**
     * Set the id for this Quest.
     *
     * @param id new id for the Quest.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Return an int that represents the minimum level required for a Quest.
     *
     * @return minimum level required of the Quest.
     */
    public int getMinLevel() {
        return minLevel;
    }

    /**
     * Set the minimum level for this Quest.
     *
     * @param minLevel new minimum level for the Quest.
     */
    public void setMinLevel(int minLevel) {
        this.minLevel = minLevel;
    }

    /**
     * Add a character for this Quest.
     *
     * @param wc new character for the Quest.
     */
    public void addWowCharacter(WowCharacter wc) {
        wc.internalAddQuest(this);
        this.wowcharacters.add(wc);
    }

    public void internalAddWowCharacter(WowCharacter wowcharacter) {
        this.wowcharacters.add(wowcharacter);
    }

    public void internalRemoveWowCharacter(WowCharacter wowcharacter) {
        this.wowcharacters.remove(wowcharacter);
    }
}