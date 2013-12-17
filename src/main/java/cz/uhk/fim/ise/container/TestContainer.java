package cz.uhk.fim.ise.container;

import cz.uhk.fim.ise.container.agents.TestAgent;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class TestContainer extends Container {

    public TestContainer(String host, Integer port) {
        super(host, port);
    }

    protected void setup() {
        for (Integer count = 1; count <= 50000; count++) {
            addAgent(new TestAgent(this));
        }
    }
}