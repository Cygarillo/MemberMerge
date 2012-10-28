/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package membermerge;

import ch.skema.data.MitgliederDBPersistenceInterface;
import ch.skema.data.Preis;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Cyrill
 */
public class IDGenerator {
    
    
    
    public  static <T extends MitgliederDBPersistenceInterface> int getNextID(T t, EntityManager entityManager){

        Query query = entityManager.createNamedQuery(t.getClass().getSimpleName()+".findAll");
        List<T> resultList = query.getResultList();
        int highestID = 0;
        for (T r : resultList) {
            if(r.getId() > highestID)
                highestID = r.getId();
        }
        return ++highestID;
    
    }
    
    public  static int getNextSpezialPreisID(EntityManager em){
        
        int ret = getNextID(new Preis(),em);
        return Math.max(ret, 1000);
    
    }
}
