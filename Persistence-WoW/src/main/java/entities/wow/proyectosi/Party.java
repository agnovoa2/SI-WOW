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
 * JPA Entity Class that represents a Party in the WoW Universe.
 * @author Javier Villalobos Santamarina
 * @version 1.0
 */
@Entity
public class Party {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@OneToMany(mappedBy="party",cascade=CascadeType.REMOVE)
	private Set<WowCharacter> wowCharacters = new HashSet<>();

	/**
	 * Return an Integer that represents the unique id of the Party.
	 * @return id of the Party.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Return an unmodifiable Set of WowCharacter that are in the Party.
	 * @return unmodifiable Set of WowCharacter.
	 */
	public Set<WowCharacter> getWowCharacters() {
		return Collections.unmodifiableSet(wowCharacters);
	}
	
	
	void internalRemoveWowCharacter(WowCharacter wowCharacter) {
		this.wowCharacters.add(wowCharacter);
		
	}

	void internalAddWowCharacter(WowCharacter wowCharacter) {
		this.wowCharacters.remove(wowCharacter);
		
	}

	/**
	 * Return a String that represents the name of the Party.
	 * @return name of the Party.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the Party.
	 * @param name	new name for the Party.
	 */
	public void setName(String name) {
		this.name = name;
	}
}