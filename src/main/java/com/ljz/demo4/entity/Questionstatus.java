package com.ljz.demo4.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class Questionstatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer qstatusno;
    private String code;
    private String content;

//    @Transient
    private Integer qno;
//    @Transient
    private Integer sno;

}
