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

import entities.wow.proyectosi.Quest;

public class QuestTest extends SQLBasedTest{
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
		
		
		final Quest i = new Quest();		
		
		doTransaction(emf, em->{
				i.setQuestName("¡Profana este fuego!");
				i.setQuestDescription("Test Description");
				i.setMinLevel(1);
				i.setExpReward(80);
				i.setGoldReward(19);
				i.setSilverReward(50);
				i.setCopperReward(0);
				em.persist(i);
		});
		
		
		//check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"Select Count(*) as total from Quest Where id = " + i.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
		statement.executeUpdate(
				"Delete From Quest Where id = " + i.getId());
	}
	@Test
	public void testFindItemById() throws SQLException{
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Quest(id,questName,questDescription,minLevel,expReward,goldReward,silverReward,copperReward) values(223,'¡Profana este fuego!', 'Test description', 1, 80, 19, 50, 0)"
				,Statement.RETURN_GENERATED_KEYS);
		int id = 223;
		
		//test code
		Quest i = emf.createEntityManager().find(Quest.class, id);

		//assert code
		assertEquals("¡Profana este fuego!", i.getQuestName());
		assertEquals(id, i.getId());
		
		statement.executeUpdate(
				"Delete From Quest Where id = " + i.getId());
	}
}
