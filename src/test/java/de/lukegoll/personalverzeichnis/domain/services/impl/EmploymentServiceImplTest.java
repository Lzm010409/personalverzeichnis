package de.lukegoll.personalverzeichnis.domain.services.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Employment;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EmploymentServiceImplTest {


    private EmploymentServiceImpl employmentService;

    @Autowired
    public EmploymentServiceImplTest(EmploymentServiceImpl employmentService) {
        this.employmentService = employmentService;
    }

    @Test
    @Order(1)
    void save() {
        employmentService.save(new Employment());
        assertTrue(employmentService.findAll().size() > 0);
    }

    @Test
    @Order(2)
    void findAll() {
        assertEquals(1,employmentService.findAll().size());
    }

    @Test
    @Order(3)
    void findPaged() {
        Page<Employment> page = employmentService.findPaged(Pageable.ofSize(1));
        assertEquals(1,page.getTotalElements());
        assertEquals(1,page.getNumberOfElements());
    }

    @Test
    @Order(4)
    void deleteAll() {
        employmentService.deleteAll();
        assertTrue(employmentService.findAll().size() == 0);
    }
}