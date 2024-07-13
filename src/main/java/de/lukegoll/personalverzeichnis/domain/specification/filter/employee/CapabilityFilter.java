package de.lukegoll.personalverzeichnis.domain.specification.filter.employee;

import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import de.lukegoll.personalverzeichnis.domain.entities.CapabilityType;
import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.specification.filter.JPAFilter;
import de.lukegoll.personalverzeichnis.web.dto.EmployeeFilterDTO;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class CapabilityFilter implements JPAFilter<Specification<Employee>, String> {
    @Override
    public Specification<Employee> filter(String capabilityName) {
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
}
