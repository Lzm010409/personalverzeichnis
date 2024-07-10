package de.lukegoll.personalverzeichnis.web.controller.employees;

import de.lukegoll.personalverzeichnis.domain.entities.*;
import de.lukegoll.personalverzeichnis.domain.exceptions.DocumentServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmployeeServiceException;
import de.lukegoll.personalverzeichnis.domain.services.CapabilityTypeService;
import de.lukegoll.personalverzeichnis.domain.services.DepartmentService;
import de.lukegoll.personalverzeichnis.domain.services.DocumentService;
import de.lukegoll.personalverzeichnis.domain.services.EmployeeService;
import de.lukegoll.personalverzeichnis.web.controller.capabilities.CapabilityTypeController;
import de.lukegoll.personalverzeichnis.web.dto.EmployeeFilterDTO;
import de.lukegoll.personalverzeichnis.web.form.employee.EmployeeForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CapabilityTypeService capabilityTypeService;
    @MockBean
    EmployeeService employeeService;
    @MockBean
    DepartmentService departmentService;
    @MockBean
    DocumentService documentService;

    @Autowired
    public EmployeeControllerTest(CapabilityTypeService capabilityTypeService, EmployeeService employeeService, DepartmentService departmentService, DocumentService documentService) {
        this.capabilityTypeService = capabilityTypeService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.documentService = documentService;
    }

    @Test
    void getEmployees() throws Exception {
        Page<Employee> employees = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 1);
        when(employeeService.findPaged(PageRequest.of(0, 10))).thenReturn(employees);
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk());
    }


    @Test
    void getEmployeesTypesWithKeyword() throws Exception {
        Page<Employee> employees = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 1);
        when(employeeService.findFiltered(new EmployeeFilterDTO("test"), PageRequest.of(0, 10))).thenReturn(employees);
        mockMvc.perform(MockMvcRequestBuilders.get("/").param("keyword", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void getEmployeesTypesWithException() throws Exception {
        when(employeeService.findPaged(PageRequest.of(0, 10))).thenThrow(EmployeeServiceException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/").param("keyword", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void editEmployee() throws Exception {
        UUID testID = UUID.randomUUID();
        when(employeeService.findById(testID)).thenReturn(Optional.of(new Employee()));
        mockMvc.perform(MockMvcRequestBuilders.get("/{id}", testID)
        ).andExpect(status().isOk());
    }

    @Test
    void editEmployeeWithEmptyEmployee() throws Exception {
        UUID testID = UUID.randomUUID();
        when(employeeService.findById(testID)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/{id}", testID)
                ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("danger", "Mitarbeiter wurde nicht gefunden..."));
    }

    @Test
    void editEmployeeWithException() throws Exception {
        UUID testID = UUID.randomUUID();
        when(employeeService.findById(testID)).thenThrow(EmployeeServiceException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/{id}", testID)
                ).andExpect(status().isFound()) // 302 status code for redirect
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void addEmployee() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/addEmployee"))
                .andExpect(status().isOk());
    }

    @Test
    void uploadDocument() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/uploaddocuments").param("employeeid", UUID.randomUUID().toString()))
                .andExpect(status().isOk());
    }

    @Test
    void showFile() throws Exception {
        UUID testID = UUID.randomUUID();
        Document document = new Document();
        document.setFileName("test.pdf");
        document.setBlobData(new byte[]{});
        when(documentService.findById(testID)).thenReturn(Optional.of(document));
        mockMvc.perform(MockMvcRequestBuilders.get("/showfile").param("documentid", testID.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteFile() throws Exception {
        UUID employeeID = UUID.randomUUID();
        UUID documentID = UUID.randomUUID();
        Document document = new Document();
        Employee employee = new Employee();
        when(documentService.findById(documentID)).thenReturn(Optional.of(document));
        when(employeeService.findById(employeeID)).thenReturn(Optional.of(employee));
        when(documentService.deleteById(documentID)).thenReturn(true);
        when(employeeService.deleteById(employeeID)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.get("/deletefile").param("documentid", documentID.toString()).param("employeeid", employeeID.toString()))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("success", "Dokument wurde gelöscht"))
                .andExpect(redirectedUrl("/" + employeeID));

    }

    @Test
    void deleteFileWithEmptyDocument() throws Exception {
        UUID employeeID = UUID.randomUUID();
        UUID documentID = UUID.randomUUID();
        Employee employee = new Employee();
        when(documentService.findById(documentID)).thenReturn(Optional.empty());
        when(employeeService.findById(employeeID)).thenReturn(Optional.of(employee));
        mockMvc.perform(MockMvcRequestBuilders.get("/deletefile").param("documentid", documentID.toString()).param("employeeid", employeeID.toString()))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger", "Dokument konnte nicht gelöscht werden, da es bicht gefunden wurde..."))
                .andExpect(redirectedUrl("/" + employeeID));

    }

    @Test
    void deleteFileWithEmptyEmployee() throws Exception {
        UUID employeeID = UUID.randomUUID();
        UUID documentID = UUID.randomUUID();
        Employee employee = new Employee();
        Document document = new Document();
        when(documentService.findById(documentID)).thenReturn(Optional.of(document));
        when(employeeService.findById(employeeID)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/deletefile").param("documentid", documentID.toString()).param("employeeid", employeeID.toString()))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger", "Dokument konnte nicht gelöscht werden, da der Mitarbeiter nicht gefunden wurde..."))
                .andExpect(redirectedUrl("/"));

    }

    @Test
    void deleteFileWithDocumentServiceException() throws Exception {
        UUID employeeID = UUID.randomUUID();
        UUID documentID = UUID.randomUUID();
        when(documentService.findById(documentID)).thenThrow(DocumentServiceException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/deletefile").param("documentid", documentID.toString()).param("employeeid", employeeID.toString()))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger", "Es ist ein technischer Fehler aufgetreten..."))
                .andExpect(redirectedUrl("/" + employeeID));

    }

    @Test
    void deleteFileWithEmployeeServiceException() throws Exception {
        UUID employeeID = UUID.randomUUID();
        UUID documentID = UUID.randomUUID();
        when(employeeService.findById(employeeID)).thenThrow(EmployeeServiceException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/deletefile").param("documentid", documentID.toString()).param("employeeid", employeeID.toString()))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger", "Es ist ein technischer Fehler aufgetreten..."))
                .andExpect(redirectedUrl("/" + employeeID));

    }

    @Test
    void saveEmployee() throws Exception {
        EmployeeForm employeeForm = new EmployeeForm();
        Employee employee = new Employee();
        employeeForm.setEmployee(employee);
        UUID departmentID = UUID.randomUUID();
        LocalDate startDate = LocalDate.now();
        employeeForm.setDepartmentId(departmentID);
        employeeForm.setStartDate(startDate);
        Department department = new Department();
        department.setEmployments(new ArrayList<>());
        when(departmentService.findById(departmentID)).thenReturn(Optional.of(department));
        when(departmentService.save(department)).thenReturn(department);
        when(employeeService.save(employee)).thenReturn(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/saveEmployee").flashAttr("employee",employeeForm))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("success","Mitarbeiter gespeichert..."))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void saveEmployeeWithEmployeeServiceException() throws Exception {
        EmployeeForm employeeForm = new EmployeeForm();
        Employee employee = new Employee();
        employeeForm.setEmployee(employee);
        UUID departmentID = UUID.randomUUID();
        LocalDate startDate = LocalDate.now();
        employeeForm.setDepartmentId(departmentID);
        employeeForm.setStartDate(startDate);
        Department department = new Department();
        department.setEmployments(new ArrayList<>());
        when(departmentService.findById(departmentID)).thenReturn(Optional.of(department));
        when(departmentService.save(department)).thenReturn(department);
        when(employeeService.save(employee)).thenThrow(EmployeeServiceException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/saveEmployee").flashAttr("employee",employeeForm))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Fehler beim speichern des Mitarbeiters, versuchen Sie es später nochmal."))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void deleteEmployee() throws Exception {
        UUID employeeID = UUID.randomUUID();
        when(employeeService.deleteById(employeeID)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.get("/{id}/deleteEmployee",employeeID))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("success","Mitarbeiter gelöscht..."))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void deleteEmployeeWithEmployeeServiceException() throws Exception {
        UUID employeeID = UUID.randomUUID();
        when(employeeService.deleteById(employeeID)).thenThrow(EmployeeServiceException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/{id}/deleteEmployee",employeeID))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Mitarbeiter konnte nicht gelöscht werden..."))
                .andExpect(redirectedUrl("/"));
    }
}