package de.lukegoll.personalverzeichnis.utils.mapper.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Timesheet;
import de.lukegoll.personalverzeichnis.domain.entities.TimesheetWrapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimesheetToWrapperMapperTest {


    List<Timesheet> timesheets = Arrays.asList(

            new Timesheet(LocalDateTime.of(2024, 1, 1, 10, 0)
                    , LocalDateTime.of(2024, 1, 1, 10, 10), null),
            new Timesheet(LocalDateTime.of(2024, 1, 1, 12, 0)
                    , LocalDateTime.of(2024, 1, 1, 12, 50), null)


    );

    @Test
    void mapTo() {
        TimesheetToWrapperMapper timesheet = new TimesheetToWrapperMapper();
        timesheets = new ArrayList<>(Arrays.asList(

                new Timesheet(LocalDateTime.of(2024, 1, 1, 10, 0)
                        , LocalDateTime.of(2024, 1, 1, 10, 10), null),
                new Timesheet(LocalDateTime.of(2024, 1, 1, 12, 0)
                        , LocalDateTime.of(2024, 1, 1, 13, 10), null)


        ));
        TimesheetWrapper timesheetWrapper = timesheet.mapTo(timesheets);
        assertEquals(3, timesheetWrapper.getHours());
        assertEquals(10, timesheetWrapper.getMinutes());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), timesheetWrapper.getStart());
        assertEquals(LocalDateTime.of(2024, 1, 1, 13, 10), timesheetWrapper.getEnd());


    }

    @Test
    void mapTo2() {
        TimesheetToWrapperMapper timesheet = new TimesheetToWrapperMapper();

        TimesheetWrapper timesheetWrapper = timesheet.mapTo(timesheets);
        assertEquals(2, timesheetWrapper.getHours());
        assertEquals(50, timesheetWrapper.getMinutes());
        assertEquals(LocalDateTime.of(2024, 1, 1, 10, 0), timesheetWrapper.getStart());
        assertEquals(LocalDateTime.of(2024, 1, 1, 12, 50), timesheetWrapper.getEnd());


    }

    @Test
    void mapToWithNullValues() {
        TimesheetToWrapperMapper timesheet = new TimesheetToWrapperMapper();
        TimesheetWrapper timesheetWrapper = timesheet.mapTo(null);
        assertNull(timesheetWrapper);
    }
}