package com.ljz.demo4.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class Sscore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ssno;
    private Integer testno;
    private Integer sno;
    private String code_0;
    private String code_1;
    private String code_2;
    private String code_3;
    private String code_4;
    private Double score;
}
