package com.rwos.rwos_logger.Controller;

import javax.validation.Valid;

import com.rwos.rwos_logger.Entity.MemberEvent;
import com.rwos.rwos_logger.Repository.MemberEventRepository;
import com.rwos.rwos_logger.Repository.TeamMemberRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private MemberEventRepository memberEventRepository;

    // @GetMapping("{instructorId}/courses")
    // public List<MemberEvent> getCoursesByInstructor(@PathVariable(value =
    // "memberId") Long memberId) {
    // return memberEventRepository.findByTeamMemberId(memberId);
    // }

    // @PostMapping("members/{memberId}/events")
    // public MemberEvent createEvent(@PathVariable(value = "memberId") Long
    // memberId,
    // @Valid @RequestBody MemberEvent event) {
    // return teamMemberRepository.findById(memberId).map(teamMember -> {
    // event.setTeamMember(teamMember);
    // return memberEventRepository.save(event);
    // }).orElseThrow(() -> null);
    // }

}
