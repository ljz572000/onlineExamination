package com.ljz.demo4.repository;

import com.ljz.demo4.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepo extends JpaRepository<Grade,Integer> {
    Grade findByGid(Integer id);
}

