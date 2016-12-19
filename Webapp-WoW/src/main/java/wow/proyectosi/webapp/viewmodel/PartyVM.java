package wow.proyectosi.webapp.viewmodel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;


import entities.wow.proyectosi.Party;
import entities.wow.proyectosi.WowCharacter;
import wow.proyectosi.TransactionUtils;
import wow.proyectosi.webapp.util.DesktopEntityManagerManager;

/**
 * ViewModel Class for Party.
 * @author Javier Villalobos Santamarina
 * @version 1.0
 */
public class PartyVM {
	private Party currentParty = null;
	
	/**
	 * Return the current Party in the desktop.
	 * @return the Current Party.
	 */
		public Party getCurrentParty() {
			return currentParty;
		}
		
		/**
		 * Return a List containing all the Party rows in the database.
		 * @return List with all the Party.
		 */
		public List<Party> getParties() {
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			return em.createQuery("SELECT r FROM Party r", Party.class).getResultList();
		}
		
		/**
		 * Deletes a Party from the database.
		 * @param party	Party to be deleted.
		 */
		@Command
		@NotifyChange("parties")
		public void delete(@BindingParam("p") Party party) {
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			TransactionUtils.doTransaction(em, __ -> {
				Set<WowCharacter> wowCharactersCopy = new HashSet<>(party.getWowCharacters());
				
				for (WowCharacter myWowCharacter: wowCharactersCopy) {
					myWowCharacter.setParty(null);
				}
				
				em.remove(party);
			});
		}
		
		/**
		 * Instantiate the currentParty to an empty Party.
		 */
		@Command
		@NotifyChange("currentParty")
		public void newParty() {
			this.currentParty = new Party();
		}
		
		/**
		 * Persists or updates the currentParty Party in the database.
		 */
		@Command
		@NotifyChange({"parties", "currentParty"})
		public void save() {
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			TransactionUtils.doTransaction(em, __ -> {
				em.persist(this.currentParty);
			});
			this.currentParty = null;
		}
		
		/**
		 * Set the currentParty Party to null.
		 */
		@Command
		@NotifyChange("currentParty")
		public void cancel() {
			this.currentParty = null;
		}
		
		/**
		 * Set the currentParty Party to the selected Party for edition.
		 * @param party	Party to be edited.
		 */
		@Command
		@NotifyChange("currentParty")
		public void edit(@BindingParam("p") Party party) {
			this.currentParty = party;
		}
}
