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

import entities.wow.proyectosi.Raid;
import entities.wow.proyectosi.Boss;

/**
 * JUnit Test Class for test Raid CRUDL.
 * @author Javier Villalobos Santamarina
 * @version 1.0
 */
public class RaidTest extends SQLBasedTest{
	private static EntityManagerFactory emf;
	
	/**
	 * Sets up the entity manager factory that will be used in the tests.
	 * @throws Exception	If an exception occurs.
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
	}	
	
	/**
	 * Close the entity manager factory at the end of the tests.
	 * @throws Exception	If an exception occurs.
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(emf!=null && emf.isOpen()) emf.close();
	}
	
	/**
	 * Deletes all the registers of the Boss table and Raid table in the database after the
	 * completion of every test.
	 * @throws SQLException	If an sqlexception occurs.
	 */
	@After
	public void deleteTestRaid() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("Delete From Boss");
		statement.executeUpdate("Delete From Raid");
	}

	/**
	 * Junit test for create.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testCreateRaid() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Boss(name,level) values('Ragnaros',80)", 
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);
		
		Raid r = new Raid();		
		r.setName("Test Raid");
		r.setLevel(100);
		r.setLocation("Test location");
		r.setNumPlayers(2);
		doTransaction(emf, em->{
			em.persist(r);
			Boss b = em.find(Boss.class, id);
			b.setRaid(r);
		});
		
		
		r.getBosses();
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM Raid WHERE id = "+r.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
		
		statement = jdbcConnection.createStatement();
		
		rs = statement.executeQuery(
				"SELECT raid FROM Boss e WHERE id = "+id);
		rs.next();
		
		assertEquals(r.getId(), rs.getInt("raid"));
			
	}

	/**
	 * Junit test for read.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testFindRaid() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Raid(name,level,location,numPlayers) values('Test Raid',100,'Test Location',2)", 
				Statement.RETURN_GENERATED_KEYS);
		int raidId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Boss(name,level,raid) values('Ragnaros',80,"+ raidId +")", 
				Statement.RETURN_GENERATED_KEYS);
		int bossId = getLastInsertedId(statement);
		
		Raid r = emf.createEntityManager().find(Raid.class, raidId);
		
		assertEquals(1, r.getBosses().size());
		assertEquals(bossId, r.getBosses().iterator().next().getId());
		assertEquals(r, r.getBosses().iterator().next().getRaid());
	}
	
	/**
	 * Junit test for update.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testUpdateRaid() throws SQLException{
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Raid(name,level,location,numPlayers) values('Test Raid',100,'Test Location',2)",
				Statement.RETURN_GENERATED_KEYS
				);
		
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			Raid r = em.find(Raid.class, id);
			r.setName("Test Raid Updated");
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM Raid WHERE id = "+id);
		rs.next();
		
		assertEquals("Test Raid Updated", rs.getString("name"));
		assertEquals(id, rs.getInt("id"));
	}
	
	
	private Raid aDetachedRaid = null;
	
	/**
	 * Junit test for update a detached entity.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testUpdateByMerge() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Raid(name,level,location,numPlayers) values('Test Raid',100,'Test Location',2)", 
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			aDetachedRaid = em.find(Raid.class, id);
		});
		// e is detached, because the entitymanager em is closed (see doTransaction)
		
		aDetachedRaid.setName("Test Raid Updated");
		
		doTransaction(emf, em -> {
			em.merge(aDetachedRaid);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM Raid WHERE id = "+id);
		rs.next();
		
		assertEquals("Test Raid Updated", rs.getString("name"));
		assertEquals(id, rs.getInt("id"));
	}
	
	/**
	 * Junit test for delete.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testDeleteRaid() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Raid(name,level,location,numPlayers) values('Test Raid',100,'Test Location',2)", 
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			Raid r = em.find(Raid.class, id);
			em.remove(r);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM Raid WHERE id = "+id);
		rs.next();
		
		assertEquals(0, rs.getInt("total"));
	}
	
	/**
	 * Junit test for list.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testListRaids() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Raid(name,level,location,numPlayers) values('Test Raid',100,'Test Location',2)", 
				Statement.RETURN_GENERATED_KEYS);
		//prepare database for test
		statement.executeUpdate(
				"Insert Into Raid(name,level,location,numPlayers) values('Test Raid 2',100,'Test Location 2',2)", 
				Statement.RETURN_GENERATED_KEYS);
		
		List<Raid> raids = emf.createEntityManager()
			.createQuery("SELECT r FROM Raid r ORDER BY r.name", Raid.class)
			.getResultList();
		
		//check
		assertEquals(2, raids.size());
		assertEquals("Test Raid", raids.get(0).getName());
		assertEquals("Test Raid 2", raids.get(1).getName());
	}
}
