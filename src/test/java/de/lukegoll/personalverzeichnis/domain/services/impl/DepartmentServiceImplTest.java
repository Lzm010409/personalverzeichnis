package de.lukegoll.personalverzeichnis.domain.services.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Department;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DepartmentServiceImplTest {

    private DepartmentServiceImpl departmentService;

    @Autowired
    public DepartmentServiceImplTest(DepartmentServiceImpl departmentService) {
        this.departmentService = departmentService;
    }
    @Test
    @Order(1)
    void save() {
        Department department = new Department();
        department.setName("test");
        departmentService.save(department);
        assertEquals(11, departmentService.findAll().size());
    }

    @Test
    @Order(2)
    void findAll() {
        List<Department> departmentList = departmentService.findAll();
        assertEquals(11,departmentList.size());
    }

    @Test
    @Order(3)
    void findPaged() {
        Page<Department> page= departmentService.findPaged(Pageable.ofSize(1));
        assertEquals(11,page.getTotalElements());
        assertEquals(1,page.getNumberOfElements());
    }

    @Test
    @Order(5)
    void deleteAll() {
        departmentService.deleteAll();
        assertTrue(departmentService.findAll().isEmpty());
    }

    @Test
    @Order(4)
    void findPagedByKeyword() {
        Page<Department> capabilityTypePage = departmentService.findPagedByKeyword("Finance", Pageable.ofSize(1));
        assertEquals(capabilityTypePage.getTotalElements(), 1);
        assertEquals(capabilityTypePage.getTotalPages(), 1);
    }
}