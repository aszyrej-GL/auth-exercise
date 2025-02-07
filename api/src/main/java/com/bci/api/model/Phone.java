package com.bci.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "phones")
public class Phone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Column(name = "number")
    private long number;

    @Column(name = "city_code")
    private int cityCode;

    @Column(name = "country_code")
    private String countryCode;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Phone phone = (Phone) o;
        return number == phone.number && cityCode == phone.cityCode && Objects.equals(countryCode, phone.countryCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, cityCode, countryCode);
    }
}
