package com.rwos.rwos_logger.Entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
/*
* author: Ashutosh
*/
@Entity
public class TeamMember {

    @Id
    Long userId;

    String member_name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId_fk", referencedColumnName = "userId")
    private List<MemberEvent> member_events;

    public TeamMember() {
        member_events = new ArrayList<>();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public List<MemberEvent> getMember_events() {
        return member_events;
    }

    public void setMember_events(List<MemberEvent> member_events) {
        this.member_events = member_events;
    }

    @Override
    public String toString() {
        return "TeamMember [member_id=" + userId + ", member_name=" + member_name + "]";
    }
}
