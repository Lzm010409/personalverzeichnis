package de.lukegoll.personalverzeichnis.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class TimesheetWrapper {


    private List<Timesheet> timesheets;

    private LocalDateTime start;

    private LocalDateTime end;

    private int hours;

    private int minutes;
}
