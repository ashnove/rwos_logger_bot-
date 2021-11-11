package com.rwos.rwos_logger.Service;

import java.util.List;
import java.util.Objects;

import com.rwos.rwos_logger.Entity.TeamMember;
import com.rwos.rwos_logger.Repository.TeamMemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    public void addLog(TeamMember member) {
        TeamMember getTeamMember = teamMemberRepository.findByUserId(member.getUserId());

        if (Objects.isNull(getTeamMember))
            teamMemberRepository.save(member);
        else {
            getTeamMember.getMember_events().addAll(member.getMember_events());
            teamMemberRepository.save(getTeamMember);
        }
    }

    public List<Object[]> getStausDetails() {
        return teamMemberRepository.getCurrentStatusInfo();
    }

    // public void viewStatus(){
    // memberController
    // }

}
