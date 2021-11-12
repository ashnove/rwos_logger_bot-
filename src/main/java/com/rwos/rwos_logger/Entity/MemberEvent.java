package com.rwos.rwos_logger.Entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class MemberEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long member_event_id;
    String event_type;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date event_timestamp;

    @PrePersist
    private void onCreate() {
        event_timestamp = new Date();
    }

    private Long userId_fk;

    public Long getUserId_fk() {
        return userId_fk;
    }

    public void setUserId_fk(Long userId_fk) {
        this.userId_fk = userId_fk;
    }

    public Long getMember_event_id() {
        return member_event_id;
    }

    public void setMember_event_id(Long member_event_id) {
        this.member_event_id = member_event_id;
    }

    public Date getEvent_timestamp() {
        return event_timestamp;
    }

    public void setEvent_timestamp(Date event_timestamp) {
        this.event_timestamp = event_timestamp;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

}
