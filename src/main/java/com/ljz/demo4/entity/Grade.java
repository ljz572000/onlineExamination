package com.ljz.demo4.entity;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "grade")
public class Grade {
    @Id
    private Integer gid;
    private String gname;

    @OneToMany(mappedBy = "grade")
    private List<Student> students;

    @Override
    public String toString() {
        return "Grade{" +
                "gid=" + gid +
                ", gname='" + gname + '\'' +
                '}';
    }
}
