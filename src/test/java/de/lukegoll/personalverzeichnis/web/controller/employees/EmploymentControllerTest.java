package de.lukegoll.personalverzeichnis.web.controller.employees;

import de.lukegoll.personalverzeichnis.domain.entities.Department;
import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.entities.Employment;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmployeeServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmploymentServiceException;
import de.lukegoll.personalverzeichnis.domain.services.DepartmentService;
import de.lukegoll.personalverzeichnis.domain.services.EmployeeService;
import de.lukegoll.personalverzeichnis.domain.services.EmploymentService;
import de.lukegoll.personalverzeichnis.web.controller.capabilities.CapabilityTypeController;
import de.lukegoll.personalverzeichnis.web.form.employee.EmploymentForm;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmploymentController.class)
class EmploymentControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    EmploymentService employmentService;
    @MockBean
    EmployeeService employeeService;
    @MockBean
    DepartmentService departmentService;

    @Autowired
    public EmploymentControllerTest(EmploymentService employmentService, EmployeeService employeeService, DepartmentService departmentService) {
        this.employmentService = employmentService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @Test
    void addEmployment() throws Exception {
        UUID empId = UUID.randomUUID();
        mockMvc.perform(get("/{id}/addEmployment",empId))
                .andExpect(status().isOk());
    }

    @Test
    void editEmployment() throws Exception {
        UUID empId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        when(employmentService.findById(empId)).thenReturn(Optional.of(new Employment(LocalDate.now(),LocalDate.now(),
                new Employee(),new Department())));
        when(departmentService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/{id}/{employmentId}/editEmployment", employeeId,empId))
                .andExpect(status().isOk());
    }

    @Test
    void editEmploymentWithEmptyEmployment() throws Exception {
        UUID empId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        when(employmentService.findById(empId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/{id}/{employmentId}/editEmployment", employeeId,empId))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Anstellung konnte nicht bearbeitet werden, da Sie nicht gefunden wurde"))
                .andExpect(redirectedUrl("/"+employeeId));
    }

    @Test
    void editEmploymentWithException() throws Exception {
        UUID empId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        when(employmentService.findById(empId)).thenThrow(EmploymentServiceException.class);
        mockMvc.perform(get("/{id}/{employmentId}/editEmployment", employeeId,empId))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Anstellungsseite konnte nicht geladen werden..."))
                .andExpect(redirectedUrl("/"+employeeId));
    }

    @Test
    void saveEmployment() throws Exception {
        EmploymentForm employmentForm = new EmploymentForm();
        UUID departmentId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        Department department = new Department();
        department.setEmployments(new ArrayList<>());
        Employee employee = new Employee();
        Employment employment = new Employment();
        employmentForm.setEmployment(employment);
        employmentForm.setDepartmentId(departmentId);
        when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));
        when(departmentService.findById(departmentId)).thenReturn(Optional.of(department));
        when(employmentService.save(employment)).thenReturn(employment);
        when(employeeService.save(employee)).thenReturn(employee);
        mockMvc.perform(post("/{id}/saveEmployment",employeeId).flashAttr("employmentForm",employmentForm))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("success","Anstellung erfolgreich angelegt"))
                .andExpect(redirectedUrl("/"+employeeId));
    }

    @Test
    void saveEmploymentWithEmptyEmployee() throws Exception {
        EmploymentForm employmentForm = new EmploymentForm();
        UUID departmentId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        when(employeeService.findById(employeeId)).thenReturn(Optional.empty());
        mockMvc.perform(post("/{id}/saveEmployment",employeeId).flashAttr("employmentForm",employmentForm))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Anstellung konnte nicht gefunden werden, da der dazugehörige Mitarbeiter nicht gefunden wurde..."))
                .andExpect(redirectedUrl("/"));
    }


    @Test
    void saveEmploymentWithEmploymentServiceException() throws Exception {
        EmploymentForm employmentForm = new EmploymentForm();
        UUID departmentId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        Department department = new Department();
        department.setEmployments(new ArrayList<>());
        Employee employee = new Employee();
        Employment employment = new Employment();
        employmentForm.setEmployment(employment);
        employmentForm.setDepartmentId(departmentId);
        when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));
        when(departmentService.findById(departmentId)).thenReturn(Optional.of(department));
        when(employmentService.save(employment)).thenThrow(EmploymentServiceException.class);
        when(employeeService.save(employee)).thenReturn(employee);
        mockMvc.perform(post("/{id}/saveEmployment",employeeId).flashAttr("employmentForm",employmentForm))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Es ist ein Fehler beim speichern der Anstellung aufgetreten."))
                .andExpect(redirectedUrl("/"+employeeId));
    }

    @Test
    void saveEmploymentWithEmployeeServiceException() throws Exception {
        EmploymentForm employmentForm = new EmploymentForm();
        UUID departmentId = UUID.randomUUID();
        UUID employeeId = UUID.randomUUID();
        when(employeeService.findById(employeeId)).thenThrow(EmployeeServiceException.class);
        mockMvc.perform(post("/{id}/saveEmployment",employeeId).flashAttr("employmentForm",employmentForm))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Es ist ein Fehler beim speichern des Mitarbeiters aufgetreten."))
                .andExpect(redirectedUrl("/"+employeeId));
    }



    @Test
    void deleteEmployment() throws Exception {
        UUID employeeId = UUID.randomUUID();
        UUID employmentId = UUID.randomUUID();
        Employee employee = new Employee();
        employee.setEmployments(new ArrayList<>());
        Department department = new Department();
        Employment employment = new Employment();
        employment.setDepartment(department);
        department.setEmployments(new ArrayList<>());
        when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employmentService.findById(employmentId)).thenReturn(Optional.of(employment));
        when(employeeService.save(employee)).thenReturn(employee);
        when(employmentService.save(employment)).thenReturn(employment);
        when(departmentService.save(department)).thenReturn(department);
        mockMvc.perform(get("/{id}/{employmentId}/deleteEmployment",employeeId,employmentId))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("success","Anstellung erfolgreich gelöscht"))
                .andExpect(redirectedUrl("/"+employeeId));
    }
    @Test
    void deleteEmploymentWithEmploymentServiceException() throws Exception {
        UUID employeeId = UUID.randomUUID();
        UUID employmentId = UUID.randomUUID();
        when(employeeService.findById(employeeId)).thenReturn(Optional.of(new Employee()));
        when(employmentService.findById(employmentId)).thenThrow(EmploymentServiceException.class);
        mockMvc.perform(get("/{id}/{employmentId}/deleteEmployment",employeeId,employmentId))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Anstellung konnte nicht gelöscht werden..."))
                .andExpect(redirectedUrl("/"+employeeId));
    }
}