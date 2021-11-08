package com.rwos.rwos_logger.Repository;

import com.rwos.rwos_logger.Entity.Users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

    // public List<Users> findByUserName(String userName);

    public Users findByUserName(String userName);

}
