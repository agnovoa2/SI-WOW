package wow.proyectosi;

import static org.junit.Assert.assertEquals;
import static wow.proyectosi.TransactionUtils.doTransaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entities.wow.proyectosi.Item;

public class ItemTest extends SQLBasedTest{
	private static EntityManagerFactory emf;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Delete From Item Where id = 23416");
	}	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(emf!=null && emf.isOpen()) emf.close();
	}
	@After
	public void deleteTestItem() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Delete From Item Where id = 23416");
	}
	//C
	@Test
	public void testCreateItem() throws SQLException {
		
		
		final Item i = new Item();		
		
		doTransaction(emf, em->{
				i.setId(23416);
				i.setItemLevel(86);
				i.setName("Corrupted Ashbringer");
				i.setSellPrice(110631);
				i.setSlot("Main Hand");
				i.setType("Two-Hand Sword");
				em.persist(i);
		});
		
		
		//check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"Select Count(*) as total From Item Where id = " + i.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));

	}
	//R
	@Test
	public void testFindItemById() throws SQLException{
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Item(id,itemLevel,name,sellPrice,slot,type) values(23416,80,'Corrupted Ashbringer',110631,'Main Hand','Two-Hand Sword')"
				,Statement.RETURN_GENERATED_KEYS);
		int id = 23416;
		
		//test code
		Item i = emf.createEntityManager().find(Item.class, id);

		//assert code
		assertEquals("Corrupted Ashbringer", i.getName());
		assertEquals(id, i.getId());
		
		statement.executeUpdate(
				"Delete From Item Where id = " + i.getId());
	}
	//U
	@Test
	public void testUpdateItem() throws SQLException{
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Item(id,itemLevel,name,sellPrice,slot,type) values(23416,80,'Corrupted Ashbringer',110631,'Main Hand','Two-Hand Sword')"
				);
		
		int id = 23416;
		
		doTransaction(emf, em -> {
			Item i = em.find(Item.class, id);
			i.setName("Ashbringer");
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM Item WHERE id = "+id);
		rs.next();
		
		assertEquals("Ashbringer", rs.getString("name"));
		assertEquals(id, rs.getInt("id"));
	}
	
	//U
	private Item aDetachedItem = null;
	@Test
	public void testUpdateByMerge() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Item(id,itemLevel,name,sellPrice,slot,type) values(23416,80,'Corrupted Ashbringer',110631,'Main Hand','Two-Hand Sword')"
				);
		int id = 23416;
		
		doTransaction(emf, em -> {
			aDetachedItem = em.find(Item.class, id);
		});
		// e is detached, because the entitymanager em is closed (see doTransaction)
		
		aDetachedItem.setName("Ashbringer");
		
		doTransaction(emf, em -> {
			em.merge(aDetachedItem);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM Item WHERE id = "+id);
		rs.next();
		
		assertEquals("Ashbringer", rs.getString("name"));
		assertEquals(id, rs.getInt("id"));
	}
	
	//D
	@Test
	public void testDeleteItem() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Item(id,itemLevel,name,sellPrice,slot,type) values(23416,80,'Corrupted Ashbringer',110631,'Main Hand','Two-Hand Sword')"
				);
		int id = 23416;
		
		doTransaction(emf, em -> {
			Item i = em.find(Item.class, id);
			em.remove(i);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM Item WHERE id = "+id);
		rs.next();
		
		assertEquals(0, rs.getInt("total"));
	}
	
	//L
	@Test
	public void testListBosses() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Item(id,itemLevel,name,sellPrice,slot,type) values(23416,80,'Corrupted Ashbringer',110631,'Main Hand','Two-Hand Sword')",Statement.RETURN_GENERATED_KEYS
				);
		//prepare database for test
		statement.executeUpdate(
				"Insert Into Item(id,itemLevel,name,sellPrice,slot,type) values(23417,80,'Ashbringer',110631,'Main Hand','Two-Hand Sword')",Statement.RETURN_GENERATED_KEYS
				);
		
		List<Item> items = emf.createEntityManager()
			.createQuery("SELECT i FROM Item i ORDER BY i.name", Item.class)
			.getResultList();
		
		//check
		assertEquals(2, items.size());
		assertEquals("Ashbringer", items.get(0).getName());
		assertEquals("Corrupted Ashbringer", items.get(1).getName());
	}
}
