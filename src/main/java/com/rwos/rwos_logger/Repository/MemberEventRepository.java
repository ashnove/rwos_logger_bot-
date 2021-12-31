package com.rwos.rwos_logger.Repository;

import java.util.List;

import com.rwos.rwos_logger.Entity.MemberEvent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
/*
* author: Ashutosh
*/

// @Repository
public interface MemberEventRepository extends JpaRepository<MemberEvent, Long> {
    String GET_LAST_LOGGED_STATUS_OF_EMPLOYEE = "SELECT member_event_id, event_type, event_timestamp, user_id_fk FROM (SELECT * FROM (SELECT *, RANK() OVER(PARTITION BY user_id ORDER BY event_timestamp DESC) userRank FROM team_member a INNER JOIN member_event b ON a.user_id = b.user_id_fk)userTable WHERE userRank = '1')dataTable WHERE user_id_fk = ?1";
    String GET_CURRENT_STATUS_OF_EMPLOYEES = "SELECT member_event_id, event_type, event_timestamp, user_id_fk FROM (SELECT *, RANK() OVER(PARTITION BY user_id ORDER BY event_timestamp DESC) userRank FROM team_member a INNER JOIN member_event b ON a.user_id = b.user_id_fk)userTable WHERE userRank = '1'";
    
    @Query(nativeQuery = true, value = GET_LAST_LOGGED_STATUS_OF_EMPLOYEE)
    public MemberEvent getEmployeeLastLoggedData(Long userId);

    @Query(nativeQuery = true, value = GET_CURRENT_STATUS_OF_EMPLOYEES)
    public List<MemberEvent> getAllEmployeeStatus();
}
