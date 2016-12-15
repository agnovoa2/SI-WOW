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

import entities.wow.proyectosi.Boss;

/**
 * JUnit Test Class for test Boss CRUDL.
 * @author Alejandro Gutiérrez Novoa
 * @version 1.0
 */
public class BossTest extends SQLBasedTest{
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
	 * Deletes all the registers of the Boss table in the database after the
	 * completion of every test.
	 * @throws SQLException	If an sqlexception occurs.
	 */
	@After
	public void deleteTestBoss() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("Delete From Boss");
	}
	
	/**
	 * Junit test for create.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testCreateBoss() throws SQLException {
		
		
		final Boss b = new Boss();		
		
		doTransaction(emf, em->{
				b.setName("Ragnaros");
				b.setLevel(80);
				em.persist(b);
		});
		
		
		//check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"Select Count(*) as total From Boss Where id = " + b.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
	}
	
	/**
	 * Junit test for read.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testFindBossById() throws SQLException{
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Boss(name,level) values('Ragnaros',80)",Statement.RETURN_GENERATED_KEYS
				);
		
		int id = getLastInsertedId(statement);
		
		//test code
		Boss b = emf.createEntityManager().find(Boss.class, id);
		
		//assert code
		assertEquals("Ragnaros", b.getName());
		assertEquals(id, b.getId());
	}
	
	/**
	 * Junit test for update.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testUpdateBoss() throws SQLException{
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Boss(name,level) values('Ragnaros',80)",Statement.RETURN_GENERATED_KEYS
				);
		
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			Boss b = em.find(Boss.class, id);
			b.setName("Arthas");
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM Boss WHERE id = "+id);
		rs.next();
		
		assertEquals("Arthas", rs.getString("name"));
		assertEquals(id, rs.getInt("id"));
	}
	
	private Boss aDetachedBoss = null;
	
	/**
	 * Junit test for update a detached entity.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testUpdateByMerge() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Boss(name,level) values('Ragnaros',80)",Statement.RETURN_GENERATED_KEYS
				);
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			aDetachedBoss = em.find(Boss.class, id);
		});
		// e is detached, because the entitymanager em is closed (see doTransaction)
		
		aDetachedBoss.setName("Arthas");
		
		doTransaction(emf, em -> {
			em.merge(aDetachedBoss);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT * FROM Boss WHERE id = "+id);
		rs.next();
		
		assertEquals("Arthas", rs.getString("name"));
		assertEquals(id, rs.getInt("id"));
	}
	
	/**
	 * Junit test for delete.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testDeleteBoss() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Boss(name,level) values('Ragnaros',80)",Statement.RETURN_GENERATED_KEYS
				);
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			Boss b = em.find(Boss.class, id);
			em.remove(b);
		});
		
		//check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT COUNT(*) as total FROM Boss WHERE id = "+id);
		rs.next();
		
		assertEquals(0, rs.getInt("total"));
	}
	
	/**
	 * Junit test for list.
	 * @throws SQLException	If an sql exception occurs.
	 */
	@Test
	public void testListBosses() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Boss(name,level) values('Ragnaros',80)",Statement.RETURN_GENERATED_KEYS
				);
		//prepare database for test
		statement.executeUpdate(
				"Insert Into Boss(name,level) values('Arthas',80)",Statement.RETURN_GENERATED_KEYS
				);
		
		List<Boss> bosses = emf.createEntityManager()
			.createQuery("SELECT b FROM Boss b ORDER BY b.name", Boss.class)
			.getResultList();
		
		//check
		assertEquals(2, bosses.size());
		assertEquals("Arthas", bosses.get(0).getName());
		assertEquals("Ragnaros", bosses.get(1).getName());
	}
}
