package com.rwos.rwos_logger.Controller;

import java.util.List;
import java.util.Objects;

import com.rwos.rwos_logger.DTO.StatusResponse;
import com.rwos.rwos_logger.Entity.TeamMember;
import com.rwos.rwos_logger.Repository.TeamMemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class MemberController {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @GetMapping("/members")
    public List<TeamMember> getInstructors() {
        return teamMemberRepository.findAll();
    }

    @PostMapping("/logUser")
    public TeamMember addLog(@RequestBody TeamMember member) {
        TeamMember getTeamMember = teamMemberRepository.findByUserId(member.getUserId());

        if (Objects.isNull(getTeamMember))
            return teamMemberRepository.save(member);
        else {
            getTeamMember.getMember_events().addAll(member.getMember_events());

            return teamMemberRepository.save(getTeamMember);
        }
    }

    @GetMapping("/getStatus")
    public List<StatusResponse> getStausDetails() {
        return teamMemberRepository.getStatusInfo();
    }
}
