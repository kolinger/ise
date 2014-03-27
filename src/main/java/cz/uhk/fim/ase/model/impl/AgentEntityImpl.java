package cz.uhk.fim.ase.model.impl;

import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.platform.ServiceLocator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class AgentEntityImpl implements AgentEntity {

    private String container;
    private String id;
    private Map<String, String> properties = new HashMap<String, String>();

    public static AgentEntity create(String type) {
        AgentEntity entity = new AgentEntityImpl();
        entity.setContainer(ServiceLocator.getConfig().system.listenerAddress);
        entity.setId(UUID.randomUUID().toString());
        entity.getProperties().put("type", type);
        return entity;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }
}