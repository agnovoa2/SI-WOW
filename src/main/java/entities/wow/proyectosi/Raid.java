package entities.wow.proyectosi;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Raid {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@OneToMany(mappedBy="raid",cascade=CascadeType.REMOVE)
	private Set<Boss> bosses;
	
	public int getId() {
		return id;
	}
	
	public Set<Boss> getBosses() {
		return bosses;
	}
	
	public void setBosses(Set<Boss> bosses) {
		this.bosses = bosses;
	}
}
