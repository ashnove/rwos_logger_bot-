package com.rwos.rwos_logger.Repository;

import java.util.List;
import java.util.Optional;

import com.rwos.rwos_logger.Entity.MemberEvent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// @Repository
public interface MemberEventRepository extends JpaRepository<MemberEvent, Long> {

}
