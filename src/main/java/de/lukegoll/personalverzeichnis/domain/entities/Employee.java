package de.lukegoll.personalverzeichnis.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends AbstractEntity{
	
	
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

}
