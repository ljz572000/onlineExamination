package com.ljz.demo4.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Data
@Entity
@Table
public class Testpaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer testno;
    private String testname;
    private Integer qno_0;
    private double qscore_0 = 20;
    private Integer qno_1;
    private double qscore_1 = 20;
    private Integer qno_2;
    private double qscore_2 = 20;
    private Integer qno_3;
    private double qscore_3 = 20;
    private Integer qno_4;
    private double qscore_4 = 20;
    private Date startdate;
    private Date enddate;
    private Time testtime;
    private Integer tno;
    private Integer gid;
}
