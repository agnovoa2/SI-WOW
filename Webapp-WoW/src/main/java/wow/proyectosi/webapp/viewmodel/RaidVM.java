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

/**
 * ViewModel Class for Raid.
 * @author Javier Villalobos Santamarina
 * @version 1.0
 */
public class RaidVM {
	private Raid currentRaid = null;
		
	/**
	 * Return the current Raid in the desktop.
	 * @return the Current Raid.
	 */
		public Raid getCurrentRaid() {
			return currentRaid;
		}
		
		/**
		 * Return a List containing all the Raid rows in the database.
		 * @return List with all the Raid.
		 */
		public List<Raid> getRaids() {
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			return em.createQuery("SELECT r FROM Raid r", Raid.class).getResultList();
		}
		
		/**
		 * Deletes a Raid from the database.
		 * @param raid	Raid to be deleted.
		 */
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
		
		
		/**
		 * Instantiate the currentRaid to an empty Raid.
		 */
		@Command
		@NotifyChange("currentRaid")
		public void newRaid() {
			this.currentRaid = new Raid();
		}
		
		/**
		 * Persists or updates the currentRaid Raid in the database.
		 */
		@Command
		@NotifyChange({"raids", "currentRaid"})
		public void save() {
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			TransactionUtils.doTransaction(em, __ -> {
				em.persist(this.currentRaid);
			});
			this.currentRaid = null;
		}
		
		/**
		 * Set the currentRaid Raid to null.
		 */
		@Command
		@NotifyChange("currentRaid")
		public void cancel() {
			this.currentRaid = null;
		}
		
		/**
		 * Set the currentRaid Raid to the selected Raid for edition.
		 * @param raid	Raid to be edited.
		 */
		@Command
		@NotifyChange("currentRaid")
		public void edit(@BindingParam("r") Raid raid) {
			this.currentRaid = raid;
		}
}
