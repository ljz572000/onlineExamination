package com.ljz.demo4.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class Student {
    @Id
    private Integer sno;
    private String sname;
    private String password;
    @Transient
    private Integer gid;
    private String phone;

    @ManyToOne(targetEntity = Grade.class)
    @JoinColumn(name = "gid")
    private Grade grade;
}
