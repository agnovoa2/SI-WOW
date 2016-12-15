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

/**
 * ViewModel Class for Item.
 * @author Alejandro Gutiérrez Novoa
 * @version 1.0
 */
public class ItemVM {
	private Item currentItem = null;

	/**
	 * Return the current Item in the desktop.
	 * @return the current Item.
	 */
	public Item getCurrentItem() {
		return currentItem;
	}

	/**
	 * Return a List containing all the Item rows in the database.
	 * @return List with all the Item.
	 */
	public List<Item> getItems() {
		EntityManager em = getDesktopEntityManager();
		return em.createQuery("SELECT i FROM Item i", Item.class).getResultList();
	}

	/**
	 * Deletes an Item from the database.
	 * @param item	Item to be deleted.
	 */
	@Command
	@NotifyChange("items")
	public void delete(@BindingParam("i") Item item) {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(item);
		});
	}

	/**
	 * Instantiate the currentItem to an empty Item.
	 */
	@Command
	@NotifyChange("currentItem")
	public void newItem() {
		this.currentItem = new Item();
	}

	/**
	 * Persists or updates the currentItem Item in the database.
	 */
	@Command
	@NotifyChange({ "items", "currentItem" })
	public void save() {
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentItem);
		});
		this.currentItem = null;
	}

	/**
	 * Set the currentItem Item to null.
	 */
	@Command
	@NotifyChange("currentItem")
	public void cancel() {
		this.currentItem = null;
	}

	/**
	 * Set the currentItem Item to the selected Item for edition.
	 * @param item	Item to be edited.
	 */
	@Command
	@NotifyChange("currentItem")
	public void edit(@BindingParam("i") Item item) {
		this.currentItem = item;
	}
}
