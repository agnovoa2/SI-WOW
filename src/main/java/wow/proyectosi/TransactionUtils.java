package wow.proyectosi;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class TransactionUtils {

	public static void doTransaction(EntityManagerFactory emf, Transaction transaction) {
		EntityTransaction tx = null;
		EntityManager em = emf.createEntityManager();												
		try{
			tx = em.getTransaction();			
			try{
				tx.begin();		
					transaction.run(em);
				tx.commit();						
			}finally{
				if (tx !=null && tx.isActive()){ tx.rollback(); }
			}
		}finally{	em.close(); 	}
	}
	
}
