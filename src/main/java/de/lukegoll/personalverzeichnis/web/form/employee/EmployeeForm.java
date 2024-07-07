package de.lukegoll.personalverzeichnis.web.form.employee;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeForm {


    private Employee employee;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    private UUID departmentId;

    public static EmployeeFormBuilder builder() {
        return new EmployeeFormBuilder();
    }

    public static class EmployeeFormBuilder {
        private Employee employee;
        private LocalDate startDate;
        private UUID departmentId;

        EmployeeFormBuilder() {
        }

        public EmployeeFormBuilder employee(Employee employee) {
            this.employee = employee;
            return this;
        }

        public EmployeeFormBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public EmployeeFormBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public EmployeeForm build() {
            return new EmployeeForm(this.employee, this.startDate, this.departmentId);
        }

        public String toString() {
            return "EmployeeForm.EmployeeFormBuilder(employee=" + this.employee + ", startDate=" + this.startDate + ", departmentId=" + this.departmentId + ")";
        }
    }
}
