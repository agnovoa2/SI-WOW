package wow.proyectosi.webapp.viewmodel;

import static wow.proyectosi.webapp.util.DesktopEntityManagerManager.getDesktopEntityManager;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import entities.wow.proyectosi.Boss;
import entities.wow.proyectosi.Raid;
import wow.proyectosi.TransactionUtils;
import wow.proyectosi.webapp.util.DesktopEntityManagerManager;

/**
 * ViewModel Class for Boss.
 * @author Alejandro Gutiérrez Novoa
 * @version 1.0
 */
public class BossVM {
	private Boss currentBoss = null;

	/**
	 * Return the current Boss in the desktop.
	 * @return the Current Boss.
	 */
	public Boss getCurrentBoss() {
		return currentBoss;
	}

	/**
	 * Return a List containing all the Boss rows in the database.
	 * @return List with all the Boss.
	 */
	public List<Boss> getBosses() {
		EntityManager em = getDesktopEntityManager();
		return em.createQuery("SELECT b FROM Boss b", Boss.class).getResultList();
	}
	
	/**
	 * Return a List containing all the Raid rows in the database.
	 * @return List with all the Raid.
	 */
	public List<Raid> getRaids() {
		EntityManager em = getDesktopEntityManager();
		return em.createQuery("SELECT r FROM Raid r", Raid.class).getResultList();
	}

	/**
	 * Deletes a Boss from the database.
	 * @param boss	Boss to be deleted.
	 */
	@Command
	@NotifyChange("bosses")
	public void delete(@BindingParam("b") Boss boss) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(boss);
		});
	}

	/**
	 * Instantiate the currentBoss to an empty Boss.
	 */
	@Command
	@NotifyChange("currentBoss")
	public void newBoss() {
		this.currentBoss = new Boss();
	}

	/**
	 * Persists or updates the currentBoss Boss in the database.
	 */
	@Command
	@NotifyChange({ "bosses", "currentBoss" })
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentBoss);
		});
		this.currentBoss = null;
	}

	/**
	 * Set the currentBoss Boss to null.
	 */
	@Command
	@NotifyChange("currentBoss")
	public void cancel() {
		this.currentBoss = null;
	}

	/**
	 * Set the currentBoss Boss to the selected Boss for edition.
	 * @param boss	Boss to be edited.
	 */
	@Command
	@NotifyChange("currentBoss")
	public void edit(@BindingParam("b") Boss boss) {
		this.currentBoss = boss;
	}
}
