package com.rwos.rwos_logger.DTO;

import java.util.Date;
/*
* author: Ashutosh
*/
public class StatusResponse {

    private String member_name;
    private String event_type;
    private Date event_timestamp;

    public StatusResponse() {

    }
    public StatusResponse(String member_name, String event_type, Date event_timestamp) {
        this.member_name = member_name;
        this.event_type = event_type;
        this.event_timestamp = event_timestamp;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public Date getEvent_timestamp() {
        return event_timestamp;
    }

    public void setEvent_timestamp(Date event_timestamp) {
        this.event_timestamp = event_timestamp;
    }

}
