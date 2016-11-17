package wow.proyectosi;

import static org.junit.Assert.assertEquals;
import static wow.proyectosi.TransactionUtils.doTransaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}
