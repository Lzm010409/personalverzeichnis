package de.lukegoll.personalverzeichnis.domain.specification.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.web.dto.EmployeeFilterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeSpecificationHelperTest {

    @Test
    void createSpecifiaction() {
        EmployeeSpecificationHelper employeeSpecificationHelper = new EmployeeSpecificationHelper();
        Specification<Employee> employeeSpecification = employeeSpecificationHelper.createSpecifiaction(new EmployeeFilterDTO("test"));
        assertNotNull(employeeSpecification);
    }

    @Test
    void createSpecifiactionWithEmptyFilter() {
        EmployeeSpecificationHelper employeeSpecificationHelper = new EmployeeSpecificationHelper();
        Specification<Employee> employeeSpecification = employeeSpecificationHelper.createSpecifiaction(new EmployeeFilterDTO(""));
        assertNotNull(employeeSpecification);
    }

}