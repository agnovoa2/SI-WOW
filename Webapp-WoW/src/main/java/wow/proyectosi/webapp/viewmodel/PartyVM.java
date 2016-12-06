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

public class PartyVM {
	private Party currentParty = null;
		
		public Party getCurrentParty() {
			return currentParty;
		}
		
		public List<Party> getParties() {
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			return em.createQuery("SELECT r FROM Party r", Party.class).getResultList();
		}
		
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
		
		@Command
		@NotifyChange("currentParty")
		public void newParty() {
			this.currentParty = new Party();
		}
		
		@Command
		@NotifyChange({"parties", "currentParty"})
		public void save() {
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			TransactionUtils.doTransaction(em, __ -> {
				em.persist(this.currentParty);
			});
			this.currentParty = null;
		}
		
		@Command
		@NotifyChange("currentParty")
		public void cancel() {
			this.currentParty = null;
		}
		
		@Command
		@NotifyChange("currentParty")
		public void edit(@BindingParam("p") Party party) {
			this.currentParty = party;
		}
}
