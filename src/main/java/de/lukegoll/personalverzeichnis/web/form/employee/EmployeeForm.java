package de.lukegoll.personalverzeichnis.web.form.employee;

import de.lukegoll.personalverzeichnis.domain.entities.Department;
import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeForm {


    private Employee employee;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    private UUID departmentId;

}
