package wow.proyectosi.webapp.viewmodel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;


import entities.wow.proyectosi.Raid;
import entities.wow.proyectosi.Boss;
import wow.proyectosi.TransactionUtils;
import wow.proyectosi.webapp.util.DesktopEntityManagerManager;

public class RaidVM {
	private Raid currentRaid = null;
		
		public Raid getCurrentRaid() {
			return currentRaid;
		}
		
		public List<Raid> getRaids() {
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			return em.createQuery("SELECT r FROM Raid r", Raid.class).getResultList();
		}
		
		@Command
		@NotifyChange("raids")
		public void delete(@BindingParam("r") Raid raid) {
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			TransactionUtils.doTransaction(em, __ -> {
				Set<Boss> bossesCopy = new HashSet<>(raid.getBosses());
				
				for (Boss myBoss: bossesCopy) {
					myBoss.setRaid(null);
				}
				
				em.remove(raid);
			});
		}
		
		@Command
		@NotifyChange("currentRaid")
		public void newRaid() {
			this.currentRaid = new Raid();
		}
		
		@Command
		@NotifyChange({"raids", "currentRaid"})
		public void save() {
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			TransactionUtils.doTransaction(em, __ -> {
				em.persist(this.currentRaid);
			});
			this.currentRaid = null;
		}
		
		@Command
		@NotifyChange("currentRaid")
		public void cancel() {
			this.currentRaid = null;
		}
		
		@Command
		@NotifyChange("currentRaid")
		public void edit(@BindingParam("r") Raid raid) {
			this.currentRaid = raid;
		}
}
