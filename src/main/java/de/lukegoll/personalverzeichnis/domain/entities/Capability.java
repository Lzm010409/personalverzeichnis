package de.lukegoll.personalverzeichnis.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name="capabilities")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Capability extends AbstractEntity{

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "capabilitytype")
	private CapabilityType capabilityType; 
	
	@Column(name = "weight")
	private long weight;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee")
	private Employee employee;

}
