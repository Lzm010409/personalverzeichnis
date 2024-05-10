package de.lukegoll.personalverzeichnis.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Capability extends AbstractEntity{
	
	@OneToOne(mappedBy = "capabilityType")
	private CapabilityType capabilityType; 
	
	@Column(name = "weight")
	private long weight;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee",nullable = false)
	private Employee employee;

}
