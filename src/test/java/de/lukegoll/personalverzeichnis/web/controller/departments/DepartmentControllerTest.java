package de.lukegoll.personalverzeichnis.web.controller.departments;

import de.lukegoll.personalverzeichnis.domain.entities.CapabilityType;
import de.lukegoll.personalverzeichnis.domain.entities.Department;
import de.lukegoll.personalverzeichnis.domain.exceptions.CapabilityTypeServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.DepartmentServiceException;
import de.lukegoll.personalverzeichnis.domain.services.impl.DepartmentServiceImpl;
import de.lukegoll.personalverzeichnis.web.controller.capabilities.CapabilityTypeController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DepartmentController.class)
class DepartmentControllerTest {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    DepartmentServiceImpl departmentService;

    @Autowired
    public DepartmentControllerTest(DepartmentServiceImpl departmentService) {
        this.departmentService = departmentService;
    }


    @Test
    void getDepartmens() throws Exception {
        Page<Department> departments = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 1);
        when(departmentService.findPaged(PageRequest.of(0, 10))).thenReturn(departments);
        mockMvc.perform(MockMvcRequestBuilders.get("/department"))
                .andExpect(status().isOk());
    }


    @Test
    void getDepartmensWithKeyword() throws Exception {
        Page<Department> departments = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 1);
        when(departmentService.findPagedByKeyword("test", PageRequest.of(0, 10))).thenReturn(departments);
        mockMvc.perform(MockMvcRequestBuilders.get("/department").param("keyword", "test"))
                .andExpect(status().isOk());
    }

    @Test
    void getDepartmensWithException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/department"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("danger", "Abteilungsseite konnte nicht geladen werden...Cannot invoke \"org.springframework.data.domain.Page.getContent()\" because \"page\" is null"));
    }


    @Test
    void editDepartment() throws Exception {
        UUID testID = UUID.randomUUID();
        when(departmentService.findById(testID)).thenReturn(Optional.of(new Department()));
        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}", testID)
        ).andExpect(status().isOk());
    }

    @Test
    void editDepartmentWithEmptyCapability() throws Exception {
        UUID testID = UUID.randomUUID();
        when(departmentService.findById(testID)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}", testID)
                ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/department"))
                .andExpect(flash().attribute("danger", "Abteilung wurde nicht gefunden..."));
        ;
    }

    @Test
    void editDepartmentWithException() throws Exception {
        UUID testID = UUID.randomUUID();
        when(departmentService.findById(testID)).thenThrow(DepartmentServiceException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}", testID)
                ).andExpect(status().isFound())
                .andExpect(redirectedUrl("/department"));
    }

    @Test
    void addDepartment() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/department/addDepartment")
        ).andExpect(status().is(200));
    }

    @Test
    void saveDepartment() throws Exception {
        Department department  = new Department();
        when(departmentService.save(department)).thenReturn(department);
        mockMvc.perform(post("/department/saveDepartment")
                .flashAttr("department",department)
        ).andExpect(status().is(302));
    }
    @Test
    void saveDepartmentWithException() throws Exception {
        Department department  = new Department();
        when(departmentService.save(department)).thenThrow(DepartmentServiceException.class);

        mockMvc.perform(post("/department/saveDepartment")
                        .flashAttr("department", department))
                .andExpect(status().isFound()) // 302 status code for redirect
                .andExpect(redirectedUrl("/department"))
                .andExpect(flash().attribute("danger", "Fehler beim speichern der Abteilung, versuchen Sie es später nochmal."));
    }

    @Test
    void deleteDepartment() throws Exception {
        UUID testID= UUID.randomUUID();
        when(departmentService.deleteById(testID)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.get("/department/{id}/deleteDepartment",testID)
        ).andExpect(status().is(302));
    }

    @Test
    void deleteCapabilityWithException() throws Exception {
        UUID testID= UUID.randomUUID();
        when(departmentService.deleteById(testID)).thenThrow(DepartmentServiceException.class);
        mockMvc.perform(get("/department/{id}/deleteDepartment",testID)
                )
                .andExpect(status().isFound()) // 302 status code for redirect
                .andExpect(redirectedUrl("/department"))
                .andExpect(flash().attribute("danger", "Abteilung konnte nicht gelöscht werden..."));
    }
}