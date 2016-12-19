package entities.wow.proyectosi;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


/**
 * JPA Entity Class that represents a Raid in the WoW Universe.
 * @author Javier Villalobos Santamarina
 * @version 1.0
 */

@Entity
public class Raid {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	private int level;
	private String location;
	private int numPlayers;

	@OneToMany(mappedBy="raid",cascade=CascadeType.REMOVE)
	private Set<Boss> bosses = new HashSet<>();
	
	/**
	 * Return an Integer that represents the unique id of the Raid.
	 * @return id of the Raid.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Return an unmodifiable Set of Boss that are in the Raid.
	 * @return unmodifiable Set of Boss.
	 */
	public Set<Boss> getBosses() {
		return Collections.unmodifiableSet(this.bosses);
	}
	
	/**
	 * Return an Integer that represents the level of the Raid.
	 * @return level of the Raid.
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Set the level of the Raid.
	 * @param level	new level for the Raid.
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * Return a String that represents the location of the Raid.
	 * @return location of the Raid.
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Set the location of the Raid.
	 * @param location	new location for the Raid.
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	/**
	 * Return an Integer that represents the max number of players of the Raid.
	 * @return number of players of the Raid.
	 */
	public int getNumPlayers() {
		return numPlayers;
	}
	
	/**
	 * Set the number of players of the Raid.
	 * @param numPlayers	new number of players for the Raid.
	 */
	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}
	
	/**
	 * Return a String that represents the name of the Raid.
	 * @return name of the Raid.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the Raid.
	 * @param name	new name for the Raid.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Adds a Boss to the set of Boss that are in the Raid.
	 * @param Boss	Boss to be inserted for the Boss Set.
	 */
	public void addBoss(Boss boss){
		boss.setRaid(this);
	}
	
	/**
	 * Removes a Boss to the set of Boss that are not in the Raid.
	 * @param Boss	Boss to be removed for the Boss Set.
	 */
	public void removeBoss(Boss boss){
		boss.setRaid(null);
	}
	
	void internalAddBoss(Boss boss){
		this.bosses.add(boss);
	}
	
	void internalRemoveBoss(Boss boss){
		this.bosses.remove(boss);
	}
}
