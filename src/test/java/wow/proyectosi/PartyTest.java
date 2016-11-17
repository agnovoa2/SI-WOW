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

import entities.wow.proyectosi.Party;

public class PartyTest extends SQLBasedTest{
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
	public void testCreateParty() throws SQLException {
		
		
		final Party i = new Party();		
		
		doTransaction(emf, em->{
				em.persist(i);
		});
		
		
		//check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"Select Count(*) as total from Party Where id = " + i.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
		statement.executeUpdate(
				"Delete From Party Where id = " + i.getId());
	}
	@Test
	public void testFindPartyById() throws SQLException{
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Party(id) values(1)"
				,Statement.RETURN_GENERATED_KEYS);
		int id = 1;
		
		//test code
		Party i = emf.createEntityManager().find(Party.class, id);

		//assert code
		assertEquals(id, i.getId());
		
		statement.executeUpdate(
				"Delete From Party Where id = " + i.getId());
	}

}
