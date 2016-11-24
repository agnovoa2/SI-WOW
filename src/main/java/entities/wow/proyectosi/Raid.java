package entities.wow.proyectosi;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Raid {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	private int level;
	private String location;
	private int numPlayers;

	@OneToMany(fetch = FetchType.EAGER, mappedBy="raid",cascade=CascadeType.REMOVE)
	private Set<Boss> bosses = new HashSet<>();
	
	public int getId() {
		return id;
	}
	
	public Set<Boss> getBosses() {
		return Collections.unmodifiableSet(this.bosses);
	}
	
	public void setBosses(Set<Boss> bosses) {
		this.bosses = bosses;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getNumPlayers() {
		return numPlayers;
	}

	public void setNumPlayers(int numPlayers) {
		this.numPlayers = numPlayers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addBoss(Boss boss){
		boss.setRaid(this);
	}
	
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
