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

@Entity
public class Party {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@OneToMany(mappedBy="party",cascade=CascadeType.REMOVE)
	private Set<WowCharacter> wowCharacters = new HashSet<>();

	
	public int getId() {
		return id;
	}
	
	public Set<WowCharacter> getWowCharacters() {
		return Collections.unmodifiableSet(wowCharacters);
	}
	
	public void setMembers(Set<WowCharacter> wowCharacters) {
		this.wowCharacters = wowCharacters;
	}

	public void internalRemoveWowCharacter(WowCharacter wowCharacter) {
		this.wowCharacters.add(wowCharacter);
		
	}

	public void internalAddWowCharacter(WowCharacter wowCharacter) {
		this.wowCharacters.remove(wowCharacter);
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}