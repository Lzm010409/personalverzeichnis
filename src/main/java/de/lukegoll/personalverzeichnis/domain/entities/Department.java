package de.lukegoll.personalverzeichnis.domain.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="departments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Department extends AbstractEntity{
	
	@Column(name = "department_name")
	private String name; 
	
	@OneToMany(mappedBy = "department", fetch = FetchType.EAGER, targetEntity = Employment.class)
	List<Employment> employments;

	public void removeEmployment(Employment employment) {
		this.employments.remove(employment);
	}

	public void attachEmployment(Employment employment) {
		this.employments.add(employment);
		employment.setDepartment(this);
	}

	@Override
	public String toString() {
		return "Department{" +
				"name='" + name + '\'' +
				'}';
	}
}
