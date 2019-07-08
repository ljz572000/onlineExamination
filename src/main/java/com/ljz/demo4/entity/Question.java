package com.ljz.demo4.entity;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer qno;
    private String qname;
    private String content;
    private String inputrule;
    private String outputrule;
    private String inputexample;
    private String outputexample;
    private String rating;
}
