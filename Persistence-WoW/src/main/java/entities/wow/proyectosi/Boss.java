package entities.wow.proyectosi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * JPA Entity Class that represents a Boss in the WoW Universe.
 * @author Alejandro Gutiérrez Novoa
 * @version 1.0
 */
@Entity
public class Boss {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	private int level;

	@ManyToOne
	@JoinColumn(name = "raid")
	private Raid raid;
	

	/**
	 * Return an Integer that represents the unique id of the Boss.
	 * @return id of the Boss.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Return a String that represents the name of the Boss.
	 * @return name of the Boss.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name of the Boss.
	 * @param name	new name for the Boss.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Return an Integer that represents the level of the Boss.
	 * @return level of the Boss.
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Set the level of the Boss.
	 * @param level	New level for the Boss.
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * Return the Raid of the Boss.
	 * @return Raid of the Boss.
	 */
	public Raid getRaid() {
		return this.raid;
	}
	
	/**
	 * Set the Raid for the Boss.
	 * @param raid	new Raid for the Boss.
	 */
	public void setRaid(Raid raid) {
		if(this.raid != null){
			this.raid.internalRemoveBoss(this);
		}
		
		this.raid = raid;

		if(this.raid != null){
			this.raid.internalAddBoss(this);
		}
	}
}
