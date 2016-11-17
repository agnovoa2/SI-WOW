package wow.proyectosi;

import static org.junit.Assert.assertEquals;
import static wow.proyectosi.TransactionUtils.doTransaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entities.wow.proyectosi.Raid;

public class RaidTest extends SQLBasedTest{
	private static EntityManagerFactory emf;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
	}	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(emf!=null && emf.isOpen()) emf.close();
	}
	@Test
	public void testCreateRaid() throws SQLException {
		
		
		final Raid i = new Raid();		
		
		doTransaction(emf, em->{
				i.setName("Test Raid");
				i.setLevel(100);
				i.setLocation("Test location");
				i.setNumPlayers(2);
				em.persist(i);
		});
		
		
		//check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"Select Count(*) as total from Raid Where id = " + i.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
		statement.executeUpdate(
				"Delete From Raid Where id = " + i.getId());
	}
	@Test
	public void testFindRaidById() throws SQLException{
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Raid(id,name,level,location,numPlayers) values(1,'Test Raid',100,'Test Location',2)"
				,Statement.RETURN_GENERATED_KEYS);
		int id = 1;
		
		//test code
		Raid i = emf.createEntityManager().find(Raid.class, id);

		//assert code
		assertEquals("Test Raid", i.getName());
		assertEquals(id, i.getId());
		
		statement.executeUpdate(
				"Delete From Raid Where id = " + i.getId());
	}
}
