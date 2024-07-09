package de.lukegoll.personalverzeichnis.domain.services.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Employment;
import de.lukegoll.personalverzeichnis.domain.entities.Timesheet;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TimesheetServiceImplTest {


    private TimesheetServiceImpl timesheetService;

    @Autowired
    public TimesheetServiceImplTest(TimesheetServiceImpl timesheetService) {
        this.timesheetService = timesheetService;
    }

    @Test
    @Order(1)
    void save() {
        timesheetService.save(new Timesheet(LocalDateTime.now(),null,null));
        assertTrue(timesheetService.findAll().size() > 0);
    }

    @Test
    @Order(2)
    void findAll() {
        assertEquals(1,timesheetService.findAll().size());
    }

    @Test
    @Order(3)
    void findPaged() {
        Page<Timesheet> page = timesheetService.findPaged(Pageable.ofSize(1));
        assertEquals(1,page.getTotalElements());
        assertEquals(1,page.getNumberOfElements());
    }

    @Test
    @Order(4)
    void deleteAll() {
        timesheetService.deleteAll();
        assertTrue(timesheetService.findAll().size() == 0);
    }
}