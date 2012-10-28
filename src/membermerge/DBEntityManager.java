/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package membermerge;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Cyrill
 */
public class DBEntityManager {


    public static EntityManager createInstance(String path) {
            
                Map<String, String> dbProps = new HashMap<String, String>();
                String dbPath = path;
                dbPath = dbPath.substring(0, dbPath.length() - 6);//strips file extension
                dbProps.put("javax.persistence.jdbc.url", "jdbc:h2:" + dbPath + ";ifexists=true");
                dbProps.put("javax.persistence.jdbc.password", "");
                dbProps.put("javax.persistence.jdbc.driver", "org.h2.Driver");
                dbProps.put("javax.persistence.jdbc.user", "");

            EntityManagerFactory fact = Persistence.createEntityManagerFactory("DBLibraryPU", dbProps);
            return fact.createEntityManager();
    }
}
