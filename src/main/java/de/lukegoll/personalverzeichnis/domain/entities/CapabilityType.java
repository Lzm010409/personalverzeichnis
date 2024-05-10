package de.lukegoll.personalverzeichnis.domain.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="capability_types")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapabilityType extends AbstractEntity{
	
	@Column(name = "capability_type")
	private String name; 
	
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "capability_type", referencedColumnName = "id")
	private CapabilityType capabilityType;

}
