package entities.wow.proyectosi;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 * JPA Entity Class that represents a Character in the WoW Universe.
 *
 * @author Andrés Vieira Vázquez
 * @version 1.0
 */
@Entity
public class WowCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int level;
    private String gender;
    private String race;
    private String characterClass;
    private String faction;


    @ManyToMany(mappedBy = "wowcharacters")
    private Set<Item> items = new HashSet<Item>();

    @ManyToMany(mappedBy = "wowcharacters")
    private Set<Quest> quests = new HashSet<Quest>();

    @ManyToOne
    @JoinColumn(name = "party")
    private Party party;

    /**
     * Returns an integer that represent the unique id of the character
     *
     * @return id of the WowCharacter
     */
    public int getId() {
        return id;
    }

    /**
     * Returns a String that represent the name of the character
     *
     * @return name of the WowCharacter
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the WowCharacter.
     *
     * @param name new name for the WowCharacter.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns an integer that represent the level of the character
     *
     * @return level of the WowCharacter
     */
    public int getLevel() {
        return level;
    }

    /**
     * Set the level of the WowCharacter.
     *
     * @param level new level for the WowCharacter.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Returns a string representing the gender of the character
     *
     * @return gender of the WowCharacter
     */
    public String getGender() {
        return gender;
    }

    /**
     * Set the gender of the WowCharacter.
     *
     * @param gender new gender for the WowCharacter.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Returns a string representing the race of the character
     *
     * @return race of the WowCharacter
     */
    public String getRace() {
        return race;
    }

    /**
     * Set the race of the WowCharacter.
     *
     * @param race new race for the WowCharacter.
     */
    public void setRace(String race) {
        this.race = race;
    }

    /**
     * Returns an string representing the class of the character
     *
     * @return class of the WowCharacter
     */
    public String getCharacterClass() {
        return characterClass;
    }

    /**
     * Set the class of the WowCharacter.
     *
     * @param characterClass new class for the WowCharacter.
     */
    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    /**
     * Returns an string representing the faction of the character
     *
     * @return faction of the WowCharacter
     */
    public String getFaction() {
        return faction;
    }

    /**
     * Set the faction of the WowCharacter.
     *
     * @param faction new faction for the WowCharacter.
     */
    public void setFaction(String faction) {
        this.faction = faction;
    }

    /**
     * Returns a set representing the quests of the character
     *
     * @return quest of the WowCharacter
     */
    public Set<Quest> getQuests() {
        return Collections.unmodifiableSet(quests);
    }

    /**
     * Set the quests of the WowCharacter.
     *
     * @param quests new quests for the WowCharacter.
     */
    public void setQuests(Collection<Quest> quests) {
        // remove my quests not in quests
        Set<Quest> myQuestsCopy = new HashSet<>(this.quests);
        for (Quest employee : myQuestsCopy) {
            if (!quests.contains(employee)) {
                this.removeQuest(employee);
            }
        }

        // add new quests (since it is a set, no repetitions are possible)
        for (Quest employee : quests) {
            this.addQuest(employee);
        }

    }

    /**
     * Add the quest to the WowCharacter.
     *
     * @param q new quest for the WowCharacter.
     */
    public void addQuest(Quest q) {
        q.internalAddWowCharacter(this);
        this.quests.add(q);
    }

    /**
     * Remove an quest of the WowCharacter.
     *
     * @param q quest to remove from the WowCharacter.
     */
    public void removeQuest(Quest q) {
        q.internalRemoveWowCharacter(this);
        this.quests.remove(q);
    }

    void internalAddQuest(Quest q) {
        this.quests.add(q);
    }

    /**
     * Set the id of the WowCharacter.
     *
     * @param id new id for the WowCharacter.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns a set representing the items of the character
     *
     * @return items of the WowCharacter
     */
    public Set<Item> getItems() {
        return Collections.unmodifiableSet(items);
    }

    /**
     * Add the item to the WowCharacter.
     *
     * @param item new item for the WowCharacter.
     */
    public void addItem(Item item) {
        item.internalAddWowCharacter(this);
        this.items.add(item);
    }

    void internalAddItem(Item item) {
        this.items.add(item);
    }

    /**
     * Remove an item of the WowCharacter.
     *
     * @param item item to remove from the WowCharacter.
     */
    public void removeItem(Item item) {
        item.internalRemoveWowCharacter(this);
        this.items.remove(item);
    }

    void internalRemoveItem(Item item) {
        this.items.remove(item);
    }

    /**
     * Returns the party of the character
     *
     * @return party of the WowCharacter
     */
    public Party getParty() {
        return this.party;
    }

    /**
     * Set the party of the WowCharacter.
     *
     * @param party new party for the WowCharacter.
     */
    public void setParty(Party party) {
        if (this.party != null) {
            this.party.internalRemoveWowCharacter(this);
        }

        this.party = party;

        if (this.party != null) {
            this.party.internalAddWowCharacter(this);
        }
    }
}