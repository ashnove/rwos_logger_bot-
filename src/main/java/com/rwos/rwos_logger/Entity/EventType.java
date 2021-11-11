package com.rwos.rwos_logger.Entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EventType {

    @Id
    String event_type;
    String event_description;

    public EventType() {
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

}
