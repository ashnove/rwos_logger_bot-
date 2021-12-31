package com.rwos.rwos_logger.Repository;

import java.util.List;

import com.rwos.rwos_logger.DTO.StatusResponse;
import com.rwos.rwos_logger.Entity.TeamMember;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
/*
* author: Ashutosh
*/
@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {

    String GET_CURRENT_STATUS_OF_EMPLOYEES = "SELECT member_name, event_type, event_timestamp FROM (SELECT *, RANK() OVER(PARTITION BY user_id ORDER BY event_timestamp DESC) userRank FROM team_member a INNER JOIN member_event b ON a.user_id = b.user_id_fk)userTable WHERE userRank = '1'";
    // String GET_LAST_LOGGED_STATUS_OF_EMPLOYEE = "SELECT member_name, event_type, event_timestamp FROM (SELECT * FROM (SELECT *, RANK() OVER(PARTITION BY user_id ORDER BY event_timestamp DESC) userRank FROM team_member a INNER JOIN member_event b ON a.user_id = b.user_id_fk)userTable WHERE userRank = '1')dataTable WHERE user_id_fk = ?1";
    String GET_EMPLOYEE_NAME_BY_USER_ID = "SELECT member_name FROM team_member WHERE user_id = ?1";
    @Query("SELECT new com.rwos.rwos_logger.DTO.StatusResponse(c.member_name, p.event_type, p.event_timestamp ) FROM TeamMember c JOIN c.member_events p")
    public List<StatusResponse> getStatusInfo();

    @Query(value = GET_CURRENT_STATUS_OF_EMPLOYEES, nativeQuery = true)
    public List<Object[]> getCurrentStatusInfo();

    @Query(nativeQuery = true, value = GET_EMPLOYEE_NAME_BY_USER_ID)
    public String getEmployeeNameByUserId(Long userId);
}
