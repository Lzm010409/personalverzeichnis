package de.lukegoll.personalverzeichnis.domain.specification.impl;


import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import de.lukegoll.personalverzeichnis.domain.entities.CapabilityType;
import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.specification.SpecificationHelper;
import de.lukegoll.personalverzeichnis.web.dto.EmployeeFilterDTO;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * Einfache Implementation des {@linkplain SpecificationHelper} welches {@linkplain Specification}
 * für einen Lead aus einem LeadFilterDTO generiert
 */
public class EmployeeSpecificationHelper implements SpecificationHelper<Employee, EmployeeFilterDTO> {
    @Override
    public Specification<Employee> createSpecifiaction(EmployeeFilterDTO employeeFilterDTO) {
        return createCapabilityFilter(employeeFilterDTO.getStringFilter());
    }

    public static Specification<Employee> createCapabilityFilter(String capabilityName) {
        return (root, query, builder) -> {
            if (capabilityName == null || capabilityName.isEmpty()) {
                return builder.conjunction();
            }
            Join<Employee, Capability> capabilityJoin = root.join("capabilities");
            Join<Capability, CapabilityType> capabilityTypeJoin = capabilityJoin.join("capabilityType");

            // Build the query: where capabilityType.name = :capabilityTypeName
            return builder.equal(capabilityTypeJoin.get("name"), capabilityName);
        };
    }

}
