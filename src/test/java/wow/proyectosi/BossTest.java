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

import entities.wow.proyectosi.Boss;

public class BossTest extends SQLBasedTest{
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
	public void testCreateBoss() throws SQLException {
		
		
		final Boss b = new Boss();		
		
		doTransaction(emf, em->{
				b.setName("Ragnaros");
				em.persist(b);
		});
		
		
		//check
		Statement statement = jdbcConnection.createStatement();
		ResultSet rs = statement.executeQuery(
				"Select Count(*) as total From Boss Where id = " + b.getId());
		rs.next();
		
		assertEquals(1, rs.getInt("total"));
	}
	@Test
	public void testFindBossById() throws SQLException{
		//prepare database for test
		Statement statement = jdbcConnection.createStatement();
		int id = statement.executeUpdate("Insert Into Boss(name) values('Ragnaros')",Statement.RETURN_GENERATED_KEYS);
		
		//test code
		Boss b = emf.createEntityManager().find(Boss.class, id);
		
		//assert code
		assertEquals("Ragnaros", b.getName());
		assertEquals(id, b.getId());
	}
}
