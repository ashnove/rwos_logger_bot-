package com.rwos.rwos_logger.Repository;

import java.util.List;

import com.rwos.rwos_logger.DTO.StatusResponse;
import com.rwos.rwos_logger.Entity.TeamMember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    String GET_CURRENT_STATUS_OF_EMPLOYEES = "SELECT new com.rwos.rwos_logger.DTO.StatusResponse(x.member_name, x.event_type, x.event_timestamp ) FROM(select c,p, RANK() over(Partition by c.userId Order By p.event_timestamp DESC) userRank FROM TeamMember c NATURAL JOIN MemberEvent p)x where x.userRank = '1'";

    @Query("SELECT new com.rwos.rwos_logger.DTO.StatusResponse(c.member_name, p.event_type, p.event_timestamp ) FROM TeamMember c JOIN c.member_events p")
    public List<StatusResponse> getStatusInfo();

    @Query(GET_CURRENT_STATUS_OF_EMPLOYEES)
    public List<StatusResponse> getCurrentStatusInfo();

    public TeamMember findByUserId(Long userId);
}
