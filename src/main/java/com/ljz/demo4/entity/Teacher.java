package com.ljz.demo4.entity;


import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
public class Teacher {
    @Id
    private Integer tno;
    private String tname;
    private String password;
    private String phone;
}
