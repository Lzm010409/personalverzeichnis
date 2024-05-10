package de.lukegoll.personalverzeichnis.domain.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="employments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employment extends AbstractEntity{
	
	@Column(name = "start_date")
	private LocalDate startDate;
	
	@Column(name = "end_date")
	private LocalDate endDate; 
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "employee",nullable = false)
	private Employee employee; 
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "department",nullable = false)
	private Department department;


}
