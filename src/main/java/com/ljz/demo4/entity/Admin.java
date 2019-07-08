package com.ljz.demo4.entity;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
public class Admin {
    @Id
    private Integer ano;
    private String aname;
    private String password;
    private String phone;
}
