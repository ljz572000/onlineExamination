package com.ljz.demo4.repository;

import com.ljz.demo4.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Teacher findByTno(Integer tno);
}
