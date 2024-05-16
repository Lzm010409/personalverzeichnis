package de.lukegoll.personalverzeichnis.web.form.timesheet;

import de.lukegoll.personalverzeichnis.domain.entities.Employment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimesheetForm {

    private String employeeId;

}
