package de.lukegoll.personalverzeichnis.domain.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Employee extends AbstractEntity {


    @Column(name = "salutation")
    private Salutation salutation;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "house_number")
    private String houseNumber;

    @Column(name = "city_code")
    private String cityCode;

    @Column(name = "city")
    private String city;

    @Column(name = "tel")
    private String tel;

    @Column(name = "mail")
    private String mail;

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER, targetEntity = Employment.class, cascade = CascadeType.ALL)
    private List<Employment> employments = new ArrayList<>();

    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER, targetEntity = Capability.class, cascade = CascadeType.ALL)
    private List<Capability> capabilities = new ArrayList<>();


    public void removeEmployment(Employment employment) {
        this.employments.remove(employment);
    }

    public void attachEmployment(Employment employment) {
        this.employments.add(employment);
        employment.setEmployee(this);
    }

}
