package com.example.pokemon;

import com.example.power.Power;

import javax.persistence.*;

@Entity
@Table(name = "pokemon")
public class Pokemon {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private java.lang.Integer id;
  private String name;
  @ManyToOne
  @JoinColumn(referencedColumnName = "id",name="power")
  private Power power;
  private String imageUrl;

  public Pokemon() {}

  public Pokemon(java.lang.Integer id, String name, Power power, String imageUrl) {
    this.id = id;
    this.name = name;
    this.power = power;
    this.imageUrl = imageUrl;
  }

  public java.lang.Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Power getPower() {
    return power;
  }

  public void setPower(Power power) {
    this.power = power;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
