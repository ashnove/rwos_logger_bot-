package com.rwos.rwos_logger.Repository;

import javax.transaction.Transactional;

import com.rwos.rwos_logger.Entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // public List<Users> findByUserName(String userName);

    @Transactional
    @Modifying
    @Query("update User e set e.dateLog = ?1, e.status = ?2, e.timeLog = ?3 where e.userId = ?4")
    public void updateUserStatus(String dateLog, String status, String timeLog, Long userId);

    public Object findByUserId(Long userId);

}
