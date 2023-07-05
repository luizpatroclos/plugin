package eu.domibus.ep.edelivery.plugin.rest.dao;

import eu.domibus.ep.edelivery.plugin.rest.entity.MessageLogEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
public class MessageLogDao extends MessageBasicDao<MessageLogEntity> {

    private static final String MESSAGE_ID = "MESSAGE_ID";
    private static final String MESSAGE_IDS = "MESSAGE_IDS";
    private static final String FINAL_RECIPIENT= "FINAL_RECIPIENT";

    public MessageLogDao() {
        super(MessageLogEntity.class);
    }

    /**
     * Find the entry based on a given MessageId.
     *
     * @param messageId the id of the message.
     */
    public MessageLogEntity findByMessageId(String messageId) {
        TypedQuery<MessageLogEntity> query = em.createNamedQuery("MessageLogEntity.findByMessageId", MessageLogEntity.class);
        query.setParameter(MESSAGE_ID, messageId);
        MessageLogEntity messageLogEntity;
        try {
            messageLogEntity = query.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
        return messageLogEntity;
    }

    /**
     * Find all entries in the plugin table limited to maxCount. When maxCount is 0, return all.
     */
    public List<MessageLogEntity> findAll(int maxCount) {
        TypedQuery<MessageLogEntity> query = em.createNamedQuery("MessageLogEntity.findAll", MessageLogEntity.class);
        if(maxCount > 0) {
            return query.setMaxResults(maxCount).getResultList();
        }
        return query.getResultList();
    }

    /**
     * Fins all entries in the plugin table, for finalRecipient, limited to maxCount. When maxCount is 0, return all.
     */
    public List<MessageLogEntity> findAllByFinalRecipient(int maxCount, String finalRecipient) {
        TypedQuery<MessageLogEntity> query = em.createNamedQuery("MessageLogEntity.findAllByFinalRecipient", MessageLogEntity.class);
        query.setParameter(FINAL_RECIPIENT, finalRecipient);
        if(maxCount > 0) {
            return query.setMaxResults(maxCount).getResultList();
        }
        return query.getResultList();
    }

    /**
     * Find all entries in the plugin table.
     */
    public List<MessageLogEntity> findAll() {
        TypedQuery<MessageLogEntity> query = em.createNamedQuery("MessageLogEntity.findAll", MessageLogEntity.class);
        return query.getResultList();
    }

    /**
     * Delete the entry related to a given MessageId.
     *
     * @param messageId the id of the message.
     */
    public void deleteByMessageId(final String messageId) {
        Query query = em.createNamedQuery("MessageLogEntity.deleteByMessageId");
        query.setParameter(MESSAGE_ID, messageId);
        query.executeUpdate();
    }

    /**
     * Delete the entries related to a given MessageIds.
     *
     * @param messageIds the ids of the messages that should be deleted.
     */
    public void deleteByMessageIds(final List<String> messageIds) {
        Query query = em.createNamedQuery("MessageLogEntity.deleteByMessageIds");
        query.setParameter(MESSAGE_IDS, messageIds);
        query.executeUpdate();
    }
}
