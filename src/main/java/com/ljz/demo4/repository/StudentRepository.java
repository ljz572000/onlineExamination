package com.ljz.demo4.repository;

import com.ljz.demo4.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Student findBySno(Integer sno);
}
