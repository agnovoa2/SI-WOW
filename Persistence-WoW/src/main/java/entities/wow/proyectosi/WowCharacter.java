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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(String characterClass) {
        this.characterClass = characterClass;
    }

    public String getFaction() {
        return faction;
    }

    public void setFaction(String faction) {
        this.faction = faction;
    }

    public Set<Quest> getQuests() {
        return Collections.unmodifiableSet(quests);
    }

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

    public void addQuest(Quest q) {
        q.internalAddWowCharacter(this);
        this.quests.add(q);
    }

    public void removeQuest(Quest q) {
        q.internalRemoveWowCharacter(this);
        this.quests.remove(q);
    }

    void internalAddQuest(Quest q) {
        this.quests.add(q);
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Item> getItems() {
        return Collections.unmodifiableSet(items);
    }

    public void addItem(Item item) {
        item.internalAddWowCharacter(this);
        this.items.add(item);
    }

    void internalAddItem(Item item) {
        this.items.add(item);
    }

    public void removeItem(Item item) {
        item.internalRemoveWowCharacter(this);
        this.items.remove(item);
    }

    void internalRemoveItem(Item item) {
        this.items.remove(item);
    }

    public Party getParty() {
        return this.party;
    }

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