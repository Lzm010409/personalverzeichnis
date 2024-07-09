package de.lukegoll.personalverzeichnis.domain.services.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import de.lukegoll.personalverzeichnis.domain.entities.CapabilityType;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CapabilityTypeServiceImplTest {

    private CapabilityTypeServiceImpl capabilityTypeService;

    @Autowired
    public CapabilityTypeServiceImplTest(CapabilityTypeServiceImpl capabilityTypeService) {
        this.capabilityTypeService = capabilityTypeService;
    }
    @Test
    @Order(1)
    void save() {
        CapabilityType capabilityType = new CapabilityType();
        capabilityType.setName("test");
        capabilityTypeService.save(capabilityType);
        assertEquals(11, capabilityTypeService.findAll().size());
    }

    @Test
    @Order(2)
    void findAll() {
        List<CapabilityType> capabilityTypeList = capabilityTypeService.findAll();
        assertEquals(11,capabilityTypeList.size());
    }

    @Test
    @Order(3)
    void findPaged() {
        Page<CapabilityType> page= capabilityTypeService.findPaged(Pageable.ofSize(1));
        assertEquals(11,page.getTotalElements());
        assertEquals(1,page.getNumberOfElements());
    }

    @Test
    @Order(6)
    void deleteAll() {
        capabilityTypeService.deleteAll();
        assertTrue(capabilityTypeService.findAll().isEmpty());
    }

    @Test
    @Order(4)
    void findPagedByKeyword() {
        Page<CapabilityType> capabilityTypePage = capabilityTypeService.findPagedByKeyword("Marketing", Pageable.ofSize(1));
        assertEquals(capabilityTypePage.getTotalElements(), 1);
        assertEquals(capabilityTypePage.getTotalPages(), 1);
    }

    @Test
    @Order(5)
    void findByName() {
        CapabilityType capabilityType = capabilityTypeService.findByName("Marketing");
        assertNotNull(capabilityType);
    }
}