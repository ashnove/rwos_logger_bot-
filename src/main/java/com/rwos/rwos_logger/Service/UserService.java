package com.rwos.rwos_logger.Service;

import java.util.List;
import java.util.Objects;

import com.rwos.rwos_logger.Entity.MemberEvent;
import com.rwos.rwos_logger.Entity.TeamMember;
import com.rwos.rwos_logger.Repository.MemberEventRepository;
import com.rwos.rwos_logger.Repository.TeamMemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private MemberEventRepository memberEventRepository;

    public void addLog(TeamMember member) {
        MemberEvent memberEvent = new MemberEvent();
        memberEvent = member.getMember_events().get(0);
        memberEvent.setUserId_fk(member.getUserId());
        memberEventRepository.save(memberEvent);
    }

    public List<Object[]> getStausDetails() {
        return teamMemberRepository.getCurrentStatusInfo();
    }

}
