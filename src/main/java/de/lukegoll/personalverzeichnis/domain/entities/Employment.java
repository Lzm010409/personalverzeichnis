package de.lukegoll.personalverzeichnis.domain.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="employments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employment {
	
	@Column(name = "department")
	private String department ; 
	
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(name = "end_date")
	private LocalDate endDate; 
	


}
