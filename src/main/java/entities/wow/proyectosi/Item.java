package entities.wow.proyectosi;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Item {

	@Id
	private int id;
	
	private String name;
	private String type;
	private String slot;
	private int sellPrice;
	private int itemLevel;

	@ManyToMany
	@JoinTable(name = "wowcharacter_equipment", joinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "wowcharacter_id", referencedColumnName = "id"))
	private List<WowCharacter> wowcharacters;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	public int getItemLevel() {
		return itemLevel;
	}

	public void setItemLevel(int itemLevel) {
		this.itemLevel = itemLevel;
	}
	
	public List<WowCharacter> getWowCharacters() {
		return wowcharacters;
	}
	
	public void setWowCharacters(List<WowCharacter> characters) {
		this.wowcharacters = characters;
	}
}
