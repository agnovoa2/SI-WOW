package entities.wow.proyectosi;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Quest {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	private String questName;
	private String questDescription;
	private int minLevel;
	private int expReward;
	private int goldReward;
	private int silverReward;
	private int copperReward;
	
	@ManyToMany
	@JoinTable(name = "wowcharacter_quest", joinColumns = @JoinColumn(name = "quest_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "wowcharacter_id", referencedColumnName = "id"))
	private List<WowCharacter> wowcharacters;

	public int getId() {
		return id;
	}

	public String getQuestName() {
		return questName;
	}

	public void setQuestName(String questName) {
		this.questName = questName;
	}

	public String getQuestDescription() {
		return questDescription;
	}

	public void setQuestDescription(String questDescription) {
		this.questDescription = questDescription;
	}

	public int getExpReward() {
		return expReward;
	}

	public void setExpReward(int expReward) {
		this.expReward = expReward;
	}

	public int getGoldReward() {
		return goldReward;
	}

	public void setGoldReward(int goldReward) {
		this.goldReward = goldReward;
	}

	public int getSilverReward() {
		return silverReward;
	}

	public void setSilverReward(int silverReward) {
		this.silverReward = silverReward;
	}

	public int getCopperReward() {
		return copperReward;
	}

	public void setCopperReward(int copperReward) {
		this.copperReward = copperReward;
	}

	public List<WowCharacter> getWowcharacters() {
		return wowcharacters;
	}

	public void setWowcharacters(List<WowCharacter> wowcharacters) {
		this.wowcharacters = wowcharacters;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(int minLevel) {
		this.minLevel = minLevel;
	}
}