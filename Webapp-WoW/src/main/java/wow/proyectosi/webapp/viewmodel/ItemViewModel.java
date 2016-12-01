package wow.proyectosi.webapp.viewmodel;

import static java.util.Collections.unmodifiableList;
import static wow.proyectosi.TransactionUtils.doTransaction;
import static wow.proyectosi.webapp.util.DesktopEntityManagerManager.getDesktopEntityManager;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import entities.wow.proyectosi.Item;

public class ItemViewModel {
	private int id;
	private int itemLevel;
	private String name;
	private int sellPrice;
	private String slot;
	private String type;
	private EntityManager em = getDesktopEntityManager();
	private List<Item> items = new LinkedList<Item>();

	@Command("create")
	@NotifyChange("items")
	public void createItem() {
		doTransaction(em, transaction->{
			Item i = new Item();
			i.setId(id);
			i.setItemLevel(itemLevel);
			i.setName(name);
			i.setSellPrice(sellPrice);
			i.setSlot(slot);
			i.setType(type);
			em.persist(i);
		});
	}

	// ------ getters and setters -----
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getItemLevel() {
		return itemLevel;
	}

	public void setItemLevel(int itemLevel) {
		this.itemLevel = itemLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(int sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Item> getItems() {
		items = em.createQuery("Select i From Item i", Item.class).getResultList();
		return unmodifiableList(items);
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
}
