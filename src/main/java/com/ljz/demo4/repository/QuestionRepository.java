package com.ljz.demo4.repository;

import com.ljz.demo4.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findByQno(Integer qno);
}
