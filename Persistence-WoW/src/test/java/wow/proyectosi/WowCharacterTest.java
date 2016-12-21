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

import entities.wow.proyectosi.WowCharacter;

/**
 * JUnit Test Class for test WowCharacterTest CRUDL.
 *
 * @author Andrés Vieira Vázquez
 * @version 1.0
 */
public class WowCharacterTest extends SQLBasedTest {
    private static EntityManagerFactory emf;

    /**
     * Sets up the entity manager factory that will be used in the tests.
     *
     * @throws Exception If an exception occurs.
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        emf = Persistence.createEntityManagerFactory("si-database");
    }

    /**
     * Close the entity manager factory at the end of the tests.
     *
     * @throws Exception If an exception occurs.
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        if (emf != null && emf.isOpen()) emf.close();
    }

    /**
     * Junit test for create.
     *
     * @throws SQLException If an sql exception occurs.
     */
    @Test
    public void testCreateWowCharacter() throws SQLException {


        final WowCharacter i = new WowCharacter();

        doTransaction(emf, em -> {
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

    /**
     * Junit test for read.
     *
     * @throws SQLException If an sql exception occurs.
     */
    @Test
    public void testFindWowCharacterById() throws SQLException {
        //prepare database for test
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate(
                "Insert Into WowCharacter(id,name,level,gender,race,characterClass,faction) values(2,'Test character',50,'Male','Human','Warrior','Alliance')"
                , Statement.RETURN_GENERATED_KEYS);
        int id = 2;

        //test code
        WowCharacter i = emf.createEntityManager().find(WowCharacter.class, id);

        //assert code
        assertEquals("Test character", i.getName());
        assertEquals(id, i.getId());

        statement.executeUpdate(
                "Delete From WowCharacter Where id = " + i.getId());
    }


    /**
     * Junit test for update.
     *
     * @throws SQLException If an sql exception occurs.
     */
    @Test
    public void testUpdateWowCharacter() throws SQLException {
        //prepare database for test
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate(
                "Insert Into WowCharacter(characterClass, faction, gender, level, name, race) values('Warrior','Alliance', 'Male', '50', 'somename', 'Human')", Statement.RETURN_GENERATED_KEYS
        );

        int id = getLastInsertedId(statement);

        doTransaction(emf, em -> {
            WowCharacter wc = em.find(WowCharacter.class, id);
            wc.setName("CharacterTest");
        });

        //check
        statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM WowCharacter WHERE id = " + id);
        rs.next();

        assertEquals("CharacterTest", rs.getString("name"));
        assertEquals("Human", rs.getString("race"));
        assertEquals(id, rs.getInt("id"));
    }

    /**
     * Junit test for update a detached entity.
     *
     * @throws SQLException    If an sql exception occurs.
     */
    private WowCharacter aDetachedWowCharacter = null;

    @Test
    public void testUpdateByMerge() throws SQLException {
        //prepare database for test
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate(
                "Insert Into WowCharacter(characterClass, faction, gender, level, name, race) values('Warrior','Alliance', 'Male', '50', 'somename', 'Human')", Statement.RETURN_GENERATED_KEYS
        );
        int id = getLastInsertedId(statement);

        doTransaction(emf, em -> {
            aDetachedWowCharacter = em.find(WowCharacter.class, id);
        });
        // e is detached, because the entitymanager em is closed (see doTransaction)

        aDetachedWowCharacter.setName("CharacterTest");

        doTransaction(emf, em -> {
            em.merge(aDetachedWowCharacter);
        });

        //check
        statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT * FROM WowCharacter WHERE id = " + id);
        rs.next();

        assertEquals("CharacterTest", rs.getString("name"));
        assertEquals(id, rs.getInt("id"));
    }

    /**
     * Junit test for delete.
     *
     * @throws SQLException If an sql exception occurs.
     */
    @Test
    public void testDeleteWowCharacter() throws SQLException {
        //prepare database for test
        Statement statement = jdbcConnection.createStatement();
        statement.executeUpdate(
                "Insert Into WowCharacter(characterClass, faction, gender, level, name, race) values('Warrior','Alliance', 'Male', '50', 'somename', 'Human')", Statement.RETURN_GENERATED_KEYS
        );
        int id = getLastInsertedId(statement);

        doTransaction(emf, em -> {
            WowCharacter b = em.find(WowCharacter.class, id);
            em.remove(b);
        });

        //check
        statement = jdbcConnection.createStatement();
        ResultSet rs = statement.executeQuery(
                "SELECT COUNT(*) as total FROM WowCharacter WHERE id = " + id);
        rs.next();

        assertEquals(0, rs.getInt("total"));
    }

    /**
     * Junit test for list.
     *
     * @throws SQLException If an sql exception occurs.
     */
    @Test
    public void testListWowCharacters() throws SQLException {
        //prepare database for test
        Statement statement = jdbcConnection.createStatement();
        //Ensure table is clean to test
        statement.executeUpdate(
                "Delete from WowCharacter", Statement.RETURN_GENERATED_KEYS
        );
        statement.executeUpdate(
                "Insert Into WowCharacter(characterClass, faction, gender, level, name, race) values('Warrior','Alliance', 'Male', '50', 'Guy1', 'Human')", Statement.RETURN_GENERATED_KEYS
        );
        //prepare database for test
        statement.executeUpdate(
                "Insert Into WowCharacter(characterClass, faction, gender, level, name, race) values('Warrior','Alliance', 'Male', '50', 'Guy2', 'Human')", Statement.RETURN_GENERATED_KEYS
        );

        List<WowCharacter> wowCharacters = emf.createEntityManager()
                .createQuery("SELECT wc FROM WowCharacter wc ORDER BY wc.name", WowCharacter.class)
                .getResultList();

        //check
        assertEquals(2, wowCharacters.size());
        assertEquals("Guy1", wowCharacters.get(0).getName());
        assertEquals("Guy2", wowCharacters.get(1).getName());
    }

}
