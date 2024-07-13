package de.lukegoll.personalverzeichnis.domain.specification.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.specification.FilterContext;
import de.lukegoll.personalverzeichnis.domain.specification.filter.employee.CapabilityFilter;
import de.lukegoll.personalverzeichnis.domain.specification.filter.employee.NameFilter;
import de.lukegoll.personalverzeichnis.web.dto.EmployeeFilterDTO;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeFilterContext implements FilterContext<Employee, EmployeeFilterDTO> {
    @Override
    public Specification<Employee> fillContext(EmployeeFilterDTO filterObject) {
        return new CapabilityFilter().filter(filterObject.getStringFilter())
                .or(new NameFilter().filter(filterObject.getStringFilter()));
    }
}
