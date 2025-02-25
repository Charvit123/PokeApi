package com.example.power;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;

@Entity
@Table(name = "power")
public class Power {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto
    private Integer id;
    private String name;

    public Power() {
    }

    public Power(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
