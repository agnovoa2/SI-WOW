package entities.wow.proyectosi;

import java.util.Collections;
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
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private int level;
	private String gender;
	private String race;
	private String characterClass;
	private String faction;
	
	

	@ManyToMany(mappedBy="wowcharacters")
	private Set<Item> items;
	
	@ManyToMany(mappedBy="wowcharacters")
	private Set<Quest> quests;
	
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
	
	public void removeItem(Item item){
		item.internalRemoveWowCharacter(this);
		this.items.remove(item);
	}
	
	void internalRemoveItem(Item item) {
		this.items.remove(item);
	}
}