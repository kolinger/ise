package cz.uhk.fim.ase.reporting.model;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import cz.uhk.fim.ase.platform.ServiceLocator;
import cz.uhk.fim.ase.platform.agents.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.Map;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class Mongo implements Model {

    private Logger logger = LoggerFactory.getLogger(Mongo.class);
    private MongoClient client;
    private DB db;
    private DBCollection collection;

    public Mongo() {
        try {
            client = new MongoClient("10.0.5.24", 27017);
            
            db = client.getDB("ase");
            collection = db.getCollection("reports");
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.error("Connection to mongo database failed", e);
        }
    }

    @Override
    public void save(Map<String, ? extends Agent> agents) {
        logger.debug("Report tick...");
        Long tick = ServiceLocator.getSyncService().getTick();

        for (Agent agent : agents.values()) {
            for (Map.Entry<String, String> entry : agent.getReportValues().entrySet()) {
                BasicDBObject document = new BasicDBObject();
                document.put("tick", tick);
                document.put("node", agent.getEntity().getNode());
                document.put("agent", agent.getEntity().getId());
                document.put("key", entry.getKey());
                document.put("value", entry.getValue());
                collection.insert(document);
            }
        }
    }
}
