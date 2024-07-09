package de.lukegoll.personalverzeichnis.domain.services.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmployeeServiceImplTest {
    private EmployeeServiceImpl employeeService;

    private final static UUID TESTID = UUID.fromString("906baf22-59d6-4d3f-9807-c6bb188dab8c");

    @Autowired
    public EmployeeServiceImplTest(EmployeeServiceImpl employeeService) {
        this.employeeService = employeeService;
    }


    @Test
    @Order(1)
    void save() {
        Employee employee = new Employee();
        employeeService.save(employee);
        assertEquals(11, employeeService.findAll().size());
    }

    @Test
    @Order(2)
    void findAll() {
        assertEquals(11, employeeService.findAll().size());
    }

    @Test
    @Order(3)
    void findPaged() {
        Page<Employee> page = employeeService.findPaged(Pageable.ofSize(1));
        assertEquals(11, page.getTotalElements());
        assertEquals(11, page.getTotalPages());
    }

    @Test
    @Order(4)
    void findAllWhichArentStampedIn() {
        List<Employee> employeeList = employeeService.findAllWhichArentStampedIn();
        assertEquals(11, employeeList.size());
    }

    @Test
    @Order(5)
    void fetchWithDocuments() {
        Optional<Employee> employee = employeeService.fetchWithDocuments(TESTID);
        assertFalse(employee.isPresent());
    }

    @Test
    @Order(6)
    void findById() {
        Optional<Employee> employee = employeeService.findById(TESTID);
        assertTrue(employee.isPresent());
    }

    @Test
    @Order(7)
    void deleteById() {
        employeeService.deleteById(TESTID);
        Optional<Employee> employee = employeeService.findById(TESTID);
        assertTrue(employee.isEmpty());
    }

    @Test
    @Order(8)
    void deleteAll() {
        employeeService.deleteAll();
        assertEquals(0, employeeService.findAll().size());
    }
}