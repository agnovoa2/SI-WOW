package wow.proyectosi;

import static org.junit.Assert.assertEquals;
import static wow.proyectosi.TransactionUtils.doTransaction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import entities.wow.proyectosi.Quest;
import entities.wow.proyectosi.WowCharacter;

public class QuestTest extends SQLBasedTest {
	private static EntityManagerFactory emf;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (emf != null && emf.isOpen())
			emf.close();
	}

	@Test
	public void testCreateQuest() throws SQLException {

		final Quest i = new Quest();

		doTransaction(emf, em -> {
			i.setQuestName("¡Profana este fuego!");
			i.setQuestDescription("Test Description");
			i.setMinLevel(1);
			i.setExpReward(80);
			i.setGoldReward(19);
			i.setSilverReward(50);
			i.setCopperReward(0);
			em.persist(i);
		});

		// check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("Select Count(*) as total from Quest Where id = " + i.getId());
		rs.next();

		assertEquals(1, rs.getInt("total"));
		statement.executeUpdate("Delete From Quest Where id = " + i.getId());
	}

	@Test
	public void testFindQuestById() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Quest(questName,questDescription,minLevel,expReward,goldReward,silverReward,copperReward) values('QuestName', 'Test description', 1, 80, 19, 50, 0)",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		// test code
		Quest i = emf.createEntityManager().find(Quest.class, id);

		// assert code
		assertEquals("QuestName", i.getQuestName());
		assertEquals(id, i.getId());

		statement.executeUpdate("Delete From Quest Where id = " + i.getId());
	}

	// U
	@Test
	public void testUpdateQuest() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Quest(questName,questDescription,minLevel,expReward,goldReward,silverReward,copperReward) values('¡Profana este fuego!', 'Test description', 1, 80, 19, 50, 0)",
				Statement.RETURN_GENERATED_KEYS);

		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Quest q = em.find(Quest.class, id);
			q.setQuestName("QuestTest");
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Quest WHERE id = " + id);
		rs.next();

		assertEquals("QuestTest", rs.getString("questName"));
		assertEquals("Test description", rs.getString("questDescription"));
		assertEquals(id, rs.getInt("id"));
	}

	// U
	private Quest aDetachedQuest = null;

	@Test
	public void testUpdateByMerge() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Quest(questName,questDescription,minLevel,expReward,goldReward,silverReward,copperReward) values('¡Profana este fuego!', 'Test description', 1, 80, 19, 50, 0)",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			aDetachedQuest = em.find(Quest.class, id);
		});
		// e is detached, because the entitymanager em is closed (see
		// doTransaction)

		aDetachedQuest.setQuestName("QuestTest");

		doTransaction(emf, em -> {
			em.merge(aDetachedQuest);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT * FROM Quest WHERE id = " + id);
		rs.next();

		assertEquals("QuestTest" , rs.getString("questName"));
		assertEquals(id, rs.getInt("id"));
	}

	// D
	@Test
	public void testDeleteQuest() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"Insert Into Quest(questName,questDescription,minLevel,expReward,goldReward,silverReward,copperReward) values('¡Profana este fuego!', 'Test description', 1, 80, 19, 50, 0)",
				Statement.RETURN_GENERATED_KEYS);
		int id = getLastInsertedId(statement);

		doTransaction(emf, em -> {
			Quest q = em.find(Quest.class, id);
			em.remove(q);
		});

		// check
		statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery("SELECT COUNT(*) as total FROM Quest WHERE id = " + id);
		rs.next();

		assertEquals(0, rs.getInt("total"));
	}

	// L
	@Test
	public void testListQuests() throws SQLException {
		// prepare database for test
		Statement statement = jdbcConnection.createStatement();
		// Ensure table is clean to test
		statement.executeUpdate("Delete from Quest", Statement.RETURN_GENERATED_KEYS);
		statement.executeUpdate(
				"Insert Into Quest(questName,questDescription,minLevel,expReward,goldReward,silverReward,copperReward) values('Quest1', 'Test description', 1, 80, 19, 50, 0)",
				Statement.RETURN_GENERATED_KEYS);
		// prepare database for test
		statement.executeUpdate(
				"Insert Into Quest(questName,questDescription,minLevel,expReward,goldReward,silverReward,copperReward) values('Quest2', 'Test description', 1, 80, 19, 50, 0)",
				Statement.RETURN_GENERATED_KEYS);

		List<Quest> quests = emf.createEntityManager()
				.createQuery("SELECT q FROM Quest q ORDER BY q.questName", Quest.class).getResultList();

		// check
		assertEquals(2, quests.size());
		assertEquals("Quest1", quests.get(0).getQuestName());
		assertEquals("Quest2", quests.get(1).getQuestName());
	}

}
