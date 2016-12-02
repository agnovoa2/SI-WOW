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

public class BossVM {
	private Boss currentBoss = null;

	public Boss getCurrentBoss() {
		return currentBoss;
	}

	public List<Boss> getBosses() {
		EntityManager em = getDesktopEntityManager();
		return em.createQuery("SELECT b FROM Boss b", Boss.class).getResultList();
	}
	
	public List<Raid> getRaids() {
		EntityManager em = getDesktopEntityManager();
		return em.createQuery("SELECT r FROM Raid r", Raid.class).getResultList();
	}

	@Command
	@NotifyChange("bosses")
	public void delete(@BindingParam("b") Boss boss) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(boss);
		});
	}

	@Command
	@NotifyChange("currentBoss")
	public void newBoss() {
		this.currentBoss = new Boss();
	}

	@Command
	@NotifyChange({ "bosses", "currentBoss" })
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentBoss);
		});
		this.currentBoss = null;
	}

	@Command
	@NotifyChange("currentBoss")
	public void cancel() {
		this.currentBoss = null;
	}

	@Command
	@NotifyChange("currentBoss")
	public void edit(@BindingParam("b") Boss boss) {
		this.currentBoss = boss;
	}
}
