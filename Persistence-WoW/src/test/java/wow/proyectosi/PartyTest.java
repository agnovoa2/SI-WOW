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
	@After
	public void deleteTestItem() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("Delete From WowCharacter");
		statement.executeUpdate("Delete From Party");
	}
	//C
	@Test
	public void testCreateParty() throws SQLException {
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into WowCharacter(name,level,gender,race,characterClass,faction) values('Test character',50,'Male','Human','Warrior','Alliance')", 
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);
		
		Party p = new Party();
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
	//R
	@Test
	public void testFindParty() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Party() values()", 
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
	
	//D
	@Test
	public void testDeleteParty() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Party() values()", 
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
	
	//L
	@Test
	public void testListWowParties() throws SQLException {
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Party() values()", 
				Statement.RETURN_GENERATED_KEYS);
		//prepare database for test
		statement.executeUpdate(
				"Insert Into Party() values()", 
				Statement.RETURN_GENERATED_KEYS);
		
		List<Party> parties = emf.createEntityManager()
			.createQuery("SELECT p FROM Party p ORDER BY p.id", Party.class)
			.getResultList();
		
		//check
		assertEquals(2, parties.size());
		assertEquals(1, parties.get(0).getId());
		assertEquals(2, parties.get(1).getId());
	}
}
