package de.lukegoll.personalverzeichnis.domain.specification.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.specification.FilterContext;
import de.lukegoll.personalverzeichnis.web.dto.EmployeeFilterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeSpecificationHelperTest {

    @Test
    void createSpecifiaction() {
        FilterContext<Employee,EmployeeFilterDTO> employeeSpecificationHelper = new EmployeeFilterContext();
        Specification<Employee> employeeSpecification = employeeSpecificationHelper.fillContext(new EmployeeFilterDTO("test"));
        assertNotNull(employeeSpecification);
    }

    @Test
    void createSpecifiactionWithEmptyFilter() {
        FilterContext<Employee,EmployeeFilterDTO> employeeSpecificationHelper = new EmployeeFilterContext();
        Specification<Employee> employeeSpecification = employeeSpecificationHelper.fillContext(new EmployeeFilterDTO(""));
        assertNotNull(employeeSpecification);
    }

}