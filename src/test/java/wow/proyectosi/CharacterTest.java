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

import entities.wow.proyectosi.WowCharacter;

public class CharacterTest extends SQLBasedTest{
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
	public void testCreateItem() throws SQLException {
		
		
		final WowCharacter i = new WowCharacter();		
		
		doTransaction(emf, em->{
				i.setName("Test Character");
				i.setLevel(50);
				i.setGender("Male");
				i.setRace("Human");
				i.setCharacterClass("Warrior");
				i.setFaction("Alliance");
				em.persist(i);
		});
		
		
		//check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"Select Count(*) as total from WowCharacter Where id = " + i.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
		statement.executeUpdate(
				"Delete From WowCharacter Where id = " + i.getId());
	}
	@Test
	public void testFindItemById() throws SQLException{
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into WowCharacter(id,name,level,gender,race,characterClass,faction) values(2,'Test character',50,'Male','Human','Warrior','Alliance')"
				,Statement.RETURN_GENERATED_KEYS);
		int id = 2;
		
		//test code
		WowCharacter i = emf.createEntityManager().find(WowCharacter.class, id);

		//assert code
		assertEquals("Test character", i.getName());
		assertEquals(id, i.getId());
		
		statement.executeUpdate(
				"Delete From WowCharacter Where id = " + i.getId());
	}
}
