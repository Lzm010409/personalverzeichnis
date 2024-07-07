package de.lukegoll.personalverzeichnis.utils.dateutil;

import de.lukegoll.personalverzeichnis.domain.entities.Timesheet;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SortTimesheetsTest {


    List<Timesheet> timesheets = Arrays.asList(

            new Timesheet(LocalDateTime.of(2024, 1, 1, 10, 0)
                    , LocalDateTime.of(2024, 1, 1, 10, 10), null),
            new Timesheet(LocalDateTime.of(2024, 1, 1, 12, 0)
                    , LocalDateTime.of(2024, 1, 1, 12, 50), null),
            new Timesheet(LocalDateTime.of(2024, 1, 2, 12, 0)
                    , LocalDateTime.of(2024, 1, 2, 12, 50), null),
            new Timesheet(LocalDateTime.of(2024, 1, 3, 12, 0)
                    , LocalDateTime.of(2024, 1, 3, 12, 50), null),
            new Timesheet(LocalDateTime.of(2024, 1, 4, 12, 0)
                    , LocalDateTime.of(2024, 1, 4, 12, 50), null)


    );


    @Test
    void sortTimesheets() {
        List<List<Timesheet>> lists = SortTimesheets.sortTimesheets(timesheets);
        assertEquals(4, lists.size());
    }

    @Test
    void sortTimesheetsWithNullValues() {
        List<List<Timesheet>> lists = SortTimesheets.sortTimesheets(null);
        assertNull(lists);
    }

    @Test
    void sortTimesheetsWithEmptyValues() {
        List<List<Timesheet>> lists = SortTimesheets.sortTimesheets(new ArrayList<>());
        assertNull(lists);
    }
}