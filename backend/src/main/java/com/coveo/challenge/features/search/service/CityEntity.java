package com.coveo.challenge.features.search.service;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "city")
public class CityEntity {
    @Id
    private Integer id;
    private String name;
    private String ascii;
    private String country;

    @ElementCollection
    @CollectionTable(name = "city_alternative_names", joinColumns = @JoinColumn(name = "city_id"))
    @Column(name = "alternative_name")
    private List<String> altName;

    private float latitude;
    private float longitude;

    @ElementCollection
    @CollectionTable(name = "city_admins", joinColumns = @JoinColumn(name = "city_id"))
    @Column(name = "admin")
    private List<String> admins;

    public CityEntity(Integer id, String name, String ascii, String country, List<String> altName, float latitude, float longitude, List<String> admins) {
        this.id = id;
        this.name = name;
        this.ascii = ascii;
        this.country = country;
        this.altName = altName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.admins = admins;
    }

    public CityEntity() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAscii() {
        return ascii;
    }

    public void setAscii(String ascii) {
        this.ascii = ascii;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<String> getAltName() {
        return altName;
    }

    public void setAltName(List<String> altName) {
        this.altName = altName;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }
}
