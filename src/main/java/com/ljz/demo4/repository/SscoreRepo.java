package com.ljz.demo4.repository;

import com.ljz.demo4.entity.Sscore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SscoreRepo extends JpaRepository<Sscore,Integer> {
    Sscore findByTestnoAndSno(Integer testno, Integer sno);
    List<Sscore> findByTestno(Integer testno);

    List<Sscore> findBySno(Integer sno);
    Sscore findBySsno(Integer sno);
}
