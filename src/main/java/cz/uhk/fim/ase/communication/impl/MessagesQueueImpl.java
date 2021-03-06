package cz.uhk.fim.ase.communication.impl;

import cz.uhk.fim.ase.communication.MessagesQueue;
import cz.uhk.fim.ase.model.AgentEntity;
import cz.uhk.fim.ase.model.MessageEntity;
import cz.uhk.fim.ase.platform.Monitor;
import cz.uhk.fim.ase.platform.ServiceLocator;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tomáš Kolinger <tomas@kolinger.name>
 */
public class MessagesQueueImpl implements MessagesQueue {

    private Map<String, Queue<MessageEntity>> queue = new ConcurrentHashMap<>();
    private Monitor monitor = ServiceLocator.getMonitor();

    public void addMessage(MessageEntity message) {
        monitor.increaseReceivedMessagesCount(message.getReceivers().size());
        for (AgentEntity receiver : message.getReceivers()) {
            Queue<MessageEntity> messages;
            if (queue.containsKey(receiver.getId())) {
                messages = queue.get(receiver.getId());
                messages.add(message);
            } else {
                messages = new LinkedList<MessageEntity>();
                messages.add(message);
                queue.put(receiver.getId(), messages);
            }
        }
    }

    public MessageEntity search(String agentId) {
        if (queue.containsKey(agentId)) {
            Queue<MessageEntity> messages = queue.get(agentId);
            return messages.poll();
        }
        return null;
    }

    public MessageEntity searchByType(String agentId, Integer type) {
        if (queue.containsKey(agentId)) {
            Queue<MessageEntity> messages = queue.get(agentId);
            if (messages != null) {
                for (MessageEntity message : messages) {
                    if (message.getType().equals(type)) {
                        messages.remove(message);
                        return message;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public long getSize() {
        long sum = 0;
        for (Queue<MessageEntity> agentQueue : queue.values()) {
            sum += agentQueue.size();
        }
        return sum;
    }
}
