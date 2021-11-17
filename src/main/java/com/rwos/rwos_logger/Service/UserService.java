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
        MemberEvent getUser = new MemberEvent();
        getUser = memberEventRepository.getEmployeeLastLoggedData(member.getUserId());
        System.out.println(getUser.toString());
        if(!Objects.isNull(getUser)) {
            MemberEvent memberEvent = new MemberEvent();
            memberEvent = member.getMember_events().get(0);
            memberEvent.setUserId_fk(member.getUserId());
            memberEventRepository.save(memberEvent);
        }
        else{
            teamMemberRepository.save(member);
        }
    }

    public List<MemberEvent> getAllEmployeeStausDetails() {
        return memberEventRepository.getAllEmployeeStatus();
    }

    public String getNameByUserId(Long userId){
        return teamMemberRepository.getEmployeeNameByUserId(userId);
    }

    public MemberEvent getLastLoggedData(TeamMember member){
        return memberEventRepository.getEmployeeLastLoggedData(member.getUserId());
    }

}
