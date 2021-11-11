package com.rwos.rwos_logger.Repository;

import com.rwos.rwos_logger.Entity.MemberEvent;

import org.springframework.data.jpa.repository.JpaRepository;

// @Repository
public interface MemberEventRepository extends JpaRepository<MemberEvent, Long> {

}
