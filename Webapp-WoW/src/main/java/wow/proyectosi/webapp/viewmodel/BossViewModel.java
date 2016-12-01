package wow.proyectosi.webapp.viewmodel;

import static java.util.Collections.unmodifiableList;
import static wow.proyectosi.TransactionUtils.doTransaction;
import static wow.proyectosi.webapp.util.DesktopEntityManagerManager.getDesktopEntityManager;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import entities.wow.proyectosi.Boss;
import entities.wow.proyectosi.Raid;

public class BossViewModel {
	private int level;
	private String name;
	private Raid selectedRaid;
	private Boss selectedBoss;
	private List<Raid> raids = new LinkedList<Raid>();
	private EntityManager em = getDesktopEntityManager();
	private List<Boss> bosses = new LinkedList<Boss>();

	@Command("create")
	@NotifyChange("bosses")
	public void createBoss() {
		doTransaction(em, transaction->{
			Boss b = new Boss();
			b.setLevel(level);
			b.setName(name);
			b.setRaid(selectedRaid);
			em.persist(b);
		});
	}

	// ------ getters and setters -----
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Raid getselectedRaid() {
		return selectedRaid;
	}

	public void setselectedRaid(Raid selectedRaid) {
		this.selectedRaid = selectedRaid;
	}

	public List<Raid> getRaids() {
		raids = em.createQuery("Select r From Raid r", Raid.class).getResultList();
		return unmodifiableList(raids);
	}

	public void setRaids(List<Raid> raids) {
		this.raids = raids;
	}

	public List<Boss> getBosses() {
		bosses = em.createQuery("Select b From Boss b", Boss.class).getResultList();
		return unmodifiableList(bosses);
	}

	public void setBosses(List<Boss> bosses) {
		this.bosses = bosses;
	}

	public Raid getSelectedRaid() {
		return selectedRaid;
	}

	public void setSelectedRaid(Raid selectedRaid) {
		this.selectedRaid = selectedRaid;
	}

	public Boss getSelectedBoss() {
		return selectedBoss;
	}

	public void setSelectedBoss(Boss selectedBoss) {
		this.selectedBoss = selectedBoss;
	}
}
