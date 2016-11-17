package entities.wow.proyectosi;

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
	
	@OneToMany(mappedBy="party",cascade=CascadeType.REMOVE)
	private Set<WowCharacter> members;

	
	public int getId() {
		return id;
	}
	
	public Set<WowCharacter> getMembers() {
		return members;
	}
	
	public void setMembers(Set<WowCharacter> members) {
		this.members = members;
	}
}