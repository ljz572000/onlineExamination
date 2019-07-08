package com.ljz.demo4.repository;

import com.ljz.demo4.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin, Integer> {
    Admin findByAno(Integer ano);
}
