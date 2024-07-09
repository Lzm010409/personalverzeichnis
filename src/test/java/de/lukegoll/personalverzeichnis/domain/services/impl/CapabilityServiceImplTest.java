package de.lukegoll.personalverzeichnis.domain.services.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CapabilityServiceImplTest {

    CapabilityServiceImpl capabilityService;

    @Autowired
    public CapabilityServiceImplTest(CapabilityServiceImpl capabilityService) {
        this.capabilityService = capabilityService;
    }

    @BeforeEach
    public void setUp() {
        Capability capability = new Capability();
        capability.setWeight(1);
        capabilityService.save(capability);
    }

    @Test
    void save() {
        Capability capability = new Capability();
        capability.setWeight(1);
        capabilityService.save(capability);
        List<Capability> capabilityList = capabilityService.findAll();
        assertEquals(2, capabilityList.size());
    }

    @Test
    void findAll() {
        List<Capability> capabilityList = capabilityService.findAll();
        assertEquals(1, capabilityList.size());
    }

    @Test
    void findPaged() {
        Page<Capability> pageable = capabilityService.findPaged(Pageable.ofSize(1));
        assertEquals(3, pageable.getTotalElements());
        assertEquals(3, pageable.getTotalPages());
    }

    @Test
    void deleteAll() {
        capabilityService.deleteAll();
        assertTrue(capabilityService.findAll().isEmpty());
    }
}