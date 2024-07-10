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
 * Einfache Implementation des {@linkplain SpecificationHelper} welcher
 * es zulässt dynamisch nach Employees zu filtern
 */
public class EmployeeSpecificationHelper implements SpecificationHelper<Employee, EmployeeFilterDTO> {
    @Override
    public Specification<Employee> createSpecifiaction(EmployeeFilterDTO employeeFilterDTO) {
        return createCapabilityFilter(employeeFilterDTO.getStringFilter())
                .or(createNameFilter(employeeFilterDTO.getStringFilter()));
    }

    public static Specification<Employee> createCapabilityFilter(String capabilityName) {
        return (root, query, builder) -> {
            if (capabilityName == null || capabilityName.isEmpty()) {
                return builder.conjunction();
            }
            Join<Employee, Capability> capabilityJoin = root.join("capabilities");
            Join<Capability, CapabilityType> capabilityTypeJoin = capabilityJoin.join("capabilityType");

            // Build the query: where capabilityType.name = :capabilityTypeName
            return builder.like(
                    builder.lower(capabilityTypeJoin.get("name")), "%" + capabilityName.toLowerCase() + "%");
        };
    }

    public static Specification<Employee> createNameFilter(String nameFilter) {
        return (root, query, builder) -> {
            if (nameFilter == null || nameFilter.isEmpty()) {
                return builder.conjunction();
            }
            return builder.or(
                    builder.equal(builder.lower(root.get("name")), nameFilter.toLowerCase()),
                    builder.equal(builder.lower(root.get("firstName")), nameFilter.toLowerCase()));

        };
    }

}
