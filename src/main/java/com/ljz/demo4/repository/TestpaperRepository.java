package com.ljz.demo4.repository;


import com.ljz.demo4.entity.Testpaper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestpaperRepository extends JpaRepository<Testpaper, Integer> {
    Testpaper findByTestno(Integer tno);
}

