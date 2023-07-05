package eu.domibus.ep.edelivery.plugin.rest.entity;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "RS_PLUGIN_TB_MESSAGE_LOG")
@NamedQueries({
        @NamedQuery(name = "MessageLogEntity.findByMessageId",
                query = "select messageLogEntity from MessageLogEntity messageLogEntity where messageLogEntity.messageId=:MESSAGE_ID"),
        @NamedQuery(name = "MessageLogEntity.findAll",
                query = "select messageLogEntity from MessageLogEntity messageLogEntity order by messageLogEntity.received asc"),
        @NamedQuery(name = "MessageLogEntity.findAllByFinalRecipient",
                query = "select messageLogEntity from MessageLogEntity messageLogEntity where messageLogEntity.finalRecipient=:FINAL_RECIPIENT order by messageLogEntity.received asc"),
        @NamedQuery(name = "MessageLogEntity.deleteByMessageId",
                query = "DELETE FROM MessageLogEntity messageLogEntity where messageLogEntity.messageId=:MESSAGE_ID"),
        @NamedQuery(name = "MessageLogEntity.deleteByMessageIds",
                query = "DELETE FROM MessageLogEntity messageLogEntity where messageLogEntity.messageId in :MESSAGE_IDS")
})
public class MessageLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PK")
    private long entityId;

    @Column(name = "MESSAGE_ID")
    private String messageId;

    @Column(name = "FINAL_RECIPIENT")
    private String finalRecipient;

    @Column(name = "RECEIVED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date received;

    public MessageLogEntity() {
    }

    public MessageLogEntity(String messageId, String finalRecipient) {
        this.messageId = messageId;
        this.finalRecipient = finalRecipient;
        this.received = new Date();
    }

    public long getEntityId() {
        return entityId;
    }

    public void setEntityId(long entityId) {
        this.entityId = entityId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getFinalRecipient() {
        return finalRecipient;
    }

    public void setFinalRecipient(String finalRecipient) {
        this.finalRecipient = finalRecipient;
    }

    public Date getReceived() {
        return received;
    }

    public void setReceived(Date received) {
        this.received = received;
    }
}
