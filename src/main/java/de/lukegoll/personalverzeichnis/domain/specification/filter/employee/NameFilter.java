package de.lukegoll.personalverzeichnis.domain.specification.filter.employee;

import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import de.lukegoll.personalverzeichnis.domain.entities.CapabilityType;
import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.specification.filter.JPAFilter;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class NameFilter implements JPAFilter<Employee, String> {
    @Override
    public Specification<Employee> filter(String nameFilter) {
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
