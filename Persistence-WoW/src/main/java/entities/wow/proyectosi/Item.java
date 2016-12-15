package entities.wow.proyectosi;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * JPA Entity Class that represents an Item in the WoW Universe.
 * @author Alejandro Gutiérrez Novoa
 * @version 1.0
 */
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
	private Set<WowCharacter> wowcharacters;
	
	/**
	 * Return an Integer that represents the unique id of the item.
	 * @return id of the Item.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Set the id of the Item.
	 * @param id	new id for the Item.
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Return a string that represents the name of the item.
	 * @return name of the Item.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the Item.
	 * @param name	new name for the Item.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Return a string that represents the type of the item.
	 * @return type of the Item.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set the type of the Item.
	 * @param type	new type for the Item.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Return a string that represents the slot where belongs the item.
	 * @return slot of the Item.
	 */
	public String getSlot() {
		return slot;
	}

	/**
	 * Set the slot of the Item.
	 * @param slot	new slot for the Item.
	 */
	public void setSlot(String slot) {
		this.slot = slot;
	}

	/**
	 * Return an Integer that represents the sell price value of the item.
	 * In WoW universe the actual currencies are Gold Silver and Copper.
	 * To simplify this field we use the next format: Gold * 10000 + Silver * 100 + Copper
	 * @return sell price value of the Item.
	 */
	public int getSellPrice() {
		return sellPrice;
	}

	/**
	 * Set the sell price value of the item.
	 * In WoW universe the actual currencies are Gold Silver and Copper.
	 * To simplify this field we use the next format: Gold * 10000 + Silver * 100 + Copper
	 * @param sellPrice	new sell price for the Item.
	 */
	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	/**
	 * Return an Integer that represents the level of the Item.
	 * @return level of the Item.
	 */
	public int getItemLevel() {
		return itemLevel;
	}

	/**
	 * Set the level of the Item.
	 * @param itemLevel	new level for the Item.
	 */
	public void setItemLevel(int itemLevel) {
		this.itemLevel = itemLevel;
	}
	
	/**
	 * Return an unmodifiable Set of WowCharacter that have the Item.
	 * @return unmodifiable Set of WowCharacter.
	 */
	public Set<WowCharacter> getWowCharacters() {
		return Collections.unmodifiableSet(this.wowcharacters);
	}
	
	/**
	 * Adds a WowCharacter to the set of WowCharacter that are using this Item.
	 * @param wowCharacter	WowCharacter to be inserted in the WowCharacter Set.
	 */
	public void addWowCharacter(WowCharacter wowCharcater){
		wowCharcater.internalAddItem(this);
		this.wowcharacters.add(wowCharcater);
	}
	
	void internalAddWowCharacter(WowCharacter wowCharacter){
		this.wowcharacters.add(wowCharacter);
	}
	
	/**
	 * Removes a WowCharacter to the set of WowCharacter that are not using this Item anymore.
	 * @param wowCharacter	WowCharacter to be removed for the WowCharacter Set.
	 */
	public void removeWowCharacter(WowCharacter wowCharcater){
		wowCharcater.internalRemoveItem(this);
		this.wowcharacters.remove(wowCharcater);
	}
	
	void internalRemoveWowCharacter(WowCharacter wowCharacter){
		this.wowcharacters.remove(wowCharacter);
	}
}
