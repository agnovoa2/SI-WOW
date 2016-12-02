package wow.proyectosi.webapp.viewmodel;

import static wow.proyectosi.webapp.util.DesktopEntityManagerManager.getDesktopEntityManager;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import entities.wow.proyectosi.Item;
import wow.proyectosi.TransactionUtils;
import wow.proyectosi.webapp.util.DesktopEntityManagerManager;

public class ItemVM {
	private Item currentItem = null;

	public Item getCurrentItem() {
		return currentItem;
	}

	public List<Item> getItems() {
		EntityManager em = getDesktopEntityManager();
		return em.createQuery("SELECT i FROM Item i", Item.class).getResultList();
	}

	@Command
	@NotifyChange("items")
	public void delete(@BindingParam("i") Item item) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(item);
		});
	}

	@Command
	@NotifyChange("currentItem")
	public void newItem() {
		this.currentItem = new Item();
	}

	@Command
	@NotifyChange({ "items", "currentItem" })
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentItem);
		});
		this.currentItem = null;
	}

	@Command
	@NotifyChange("currentItem")
	public void cancel() {
		this.currentItem = null;
	}

	@Command
	@NotifyChange("currentItem")
	public void edit(@BindingParam("i") Item item) {
		this.currentItem = item;
	}
}
