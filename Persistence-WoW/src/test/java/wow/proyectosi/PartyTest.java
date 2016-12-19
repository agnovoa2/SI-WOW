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

import entities.wow.proyectosi.WowCharacter;
import entities.wow.proyectosi.Party;

/**
 * JUnit Test Class for test Party CRUDL.
 * @author Javier Villalobos Santamarina
 * @version 1.0
 */
public class PartyTest extends SQLBasedTest{
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
	 * Deletes all the registers of the WowCharcter table and Party table in the database after the
	 * completion of every test.
	 * @throws SQLException	If an sqlexception occurs.
	 */
	@After
	public void deleteTestParty() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("Delete From WowCharacter");
		statement.executeUpdate("Delete From Party");
	}

	/**
	 * Junit test for create.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testCreateParty() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into WowCharacter(name,level,gender,race,characterClass,faction) values('Test character',50,'Male','Human','Warrior','Alliance')", 
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);
		
		Party p = new Party();
		p.setName("Test Party");
		doTransaction(emf, em->{
			em.persist(p);
			WowCharacter c = em.find(WowCharacter.class, id);
			c.setParty(p);
		});
		
		
		p.getWowCharacters();
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM Party WHERE id = "+p.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
		
		statement = jdbcConnection.createStatement();
		
		rs = statement.executeQuery(
				"SELECT party FROM WowCharacter e WHERE id = "+id);
		rs.next();
		
		assertEquals(p.getId(), rs.getInt("party"));
			
	}
	
	/**
	 * Junit test for read.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testFindParty() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Party(name) values('Test Party')", 
				Statement.RETURN_GENERATED_KEYS);
		int partyId = getLastInsertedId(statement);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into WowCharacter(name,level,gender,race,characterClass,faction,party) values('Test character',50,'Male','Human','Warrior','Alliance',"+partyId+")", 
				Statement.RETURN_GENERATED_KEYS);
		int wowCharacterId = getLastInsertedId(statement);
		
		Party p = emf.createEntityManager().find(Party.class, partyId);
		
		assertEquals(1, p.getWowCharacters().size());
		assertEquals(wowCharacterId, p.getWowCharacters().iterator().next().getId());
		assertEquals(p, p.getWowCharacters().iterator().next().getParty());
	}
	
	/**
	 * Junit test for update.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testUpdateParty() throws SQLException{
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Party(name) values('Test Party')",
				Statement.RETURN_GENERATED_KEYS
				);
		
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			Party p = em.find(Party.class, id);
			p.setName("Test Party Updated");
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet ps = statement.executeQuery(
				"SELECT * FROM Party WHERE id = "+id);
		ps.next();
		
		assertEquals("Test Party Updated", ps.getString("name"));
		assertEquals(id, ps.getInt("id"));
	}
	
	
	private Party aDetachedParty = null;
	
	/**
	 * Junit test for update a detached entity.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testUpdateByMerge() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Party(name) values('Test Party')",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			aDetachedParty = em.find(Party.class, id);
		});
		// e is detached, because the entitymanager em is closed (see doTransaction)
		
		aDetachedParty.setName("Test Party Updated");
		
		doTransaction(emf, em -> {
			em.merge(aDetachedParty);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM Party WHERE id = "+id);
		rs.next();
		
		assertEquals("Test Party Updated", rs.getString("name"));
		assertEquals(id, rs.getInt("id"));
	}	

	/**
	 * Junit test for delete.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testDeleteParty() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Party(name) values('Test Party')", 
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			Party p = em.find(Party.class, id);
			em.remove(p);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM Party WHERE id = "+id);
		rs.next();
		
		assertEquals(0, rs.getInt("total"));
	}
	
	/**
	 * Junit test for list.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testListWowParties() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Party(name) values('Test Party')",
				Statement.RETURN_GENERATED_KEYS);
		//prepare database for test
		statement.executeUpdate(
				"Insert Into Party(name) values('Test Party 2')",
				Statement.RETURN_GENERATED_KEYS);
		
		List<Party> parties = emf.createEntityManager()
			.createQuery("SELECT p FROM Party p ORDER BY p.id", Party.class)
			.getResultList();
		
		//check
		assertEquals(2, parties.size());
		assertEquals("Test Party", parties.get(0).getName());
		assertEquals("Test Party 2", parties.get(1).getName());
	}
}
