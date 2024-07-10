package de.lukegoll.personalverzeichnis.web.controller.employees;

import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import de.lukegoll.personalverzeichnis.domain.entities.CapabilityType;
import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.exceptions.CapabilityServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.CapabilityTypeServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmployeeServiceException;
import de.lukegoll.personalverzeichnis.domain.services.CapabilityTypeService;
import de.lukegoll.personalverzeichnis.domain.services.EmployeeService;
import de.lukegoll.personalverzeichnis.domain.services.impl.CapabilityServiceImpl;
import de.lukegoll.personalverzeichnis.domain.services.impl.CapabilityTypeServiceImpl;
import de.lukegoll.personalverzeichnis.web.controller.capabilities.CapabilityTypeController;
import de.lukegoll.personalverzeichnis.web.form.employee.CapabilityForm;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CapabilityController.class)
class CapabilityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CapabilityServiceImpl capabilityService;

    @MockBean
    CapabilityTypeService capabilityTypeService;

    @MockBean
    EmployeeService employeeService;

    @Autowired
    public CapabilityControllerTest(CapabilityServiceImpl capabilityService, CapabilityTypeService capabilityTypeService, EmployeeService employeeService) {
        this.capabilityService = capabilityService;
        this.capabilityTypeService = capabilityTypeService;
        this.employeeService = employeeService;
    }

    @Test
    void addCapability() throws Exception {
        UUID test = UUID.randomUUID();
        when(capabilityTypeService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/{id}/addCapability", test))
                .andExpect(status().isOk());
    }

    @Test
    void addCapabilityWithException() throws Exception {
        UUID test = UUID.randomUUID();
        when(capabilityTypeService.findAll()).thenThrow(NullPointerException.class);
        mockMvc.perform(get("/{id}/addCapability", test))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"+test))
                .andExpect(flash().attribute("danger", "Fähigkeitsseite konnte nicht geladen werde..."));
    }

    @Test
    void editCapability() throws Exception {
        UUID employeeId = UUID.randomUUID();
        UUID capabilityId = UUID.randomUUID();
        when(capabilityService.findById(capabilityId)).thenReturn(Optional.of(new Capability()));
        mockMvc.perform(get("/{id}/{capabilityId}/editCapability", employeeId, capabilityId))
                .andExpect(status().isOk());
    }

    @Test
    void editCapabilityWithEmptyCapability() throws Exception {
        UUID employeeId = UUID.randomUUID();
        UUID capabilityId = UUID.randomUUID();
        when(capabilityService.findById(capabilityId)).thenReturn(Optional.empty());
        mockMvc.perform(get("/{id}/{capabilityId}/editCapability", employeeId, capabilityId))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/" + employeeId))
                .andExpect(flash().attribute("danger", "Fähigkeit konnte nicht bearbeitet werden, da Sie nicht gefunden wurde"));
    }

    @Test
    void editCapabilityWithException() throws Exception {
        UUID employeeId = UUID.randomUUID();
        UUID capabilityId = UUID.randomUUID();
        when(capabilityService.findById(capabilityId)).thenThrow(NullPointerException.class);
        mockMvc.perform(get("/{id}/{capabilityId}/editCapability", employeeId, capabilityId))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/" + employeeId))
                .andExpect(flash().attribute("danger", "Fähigkeitsseite konnte nicht geladen werden..."));
    }

    @Test
    void saveCapability() throws Exception {
        CapabilityForm capabilityForm = new CapabilityForm();
        Capability capability = new Capability();
        Employee employee = new Employee();
        CapabilityType capabilityType = new CapabilityType();
        UUID capabilityId = UUID.randomUUID();
        capabilityForm.setCapabilityTypeId(capabilityId);
        capabilityForm.setCapability(capability);
        UUID employeeId = UUID.randomUUID();
        when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));
        when(capabilityTypeService.findById(capabilityId)).thenReturn(Optional.of(capabilityType));
        when(capabilityService.save(capability)).thenReturn(capability);
        when(employeeService.save(employee)).thenReturn(employee);
        when(capabilityTypeService.save(capabilityType)).thenReturn(capabilityType);
        mockMvc.perform(post("/{id}/saveCapability",employeeId).flashAttr("capabilityForm",capabilityForm))
                .andExpect(redirectedUrl("/"+employeeId))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("success","Fähigkeit erfolgreich angelegt"));
    }

    @Test
    void saveCapabilityWithEmptyEmployee() throws Exception {
        CapabilityForm capabilityForm = new CapabilityForm();
        UUID employeeId = UUID.randomUUID();
        when(employeeService.findById(employeeId)).thenReturn(Optional.empty());
        mockMvc.perform(post("/{id}/saveCapability",employeeId).flashAttr("capabilityForm",capabilityForm))
                .andExpect(redirectedUrl("/"))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Fähigkeit konnte nicht gespeichert werden, da der dazugehörige Mitarbeiter nicht gefunden wurde..."));
    }

    @Test
    void saveCapabilityWithPersistedCapability() throws Exception {
        CapabilityForm capabilityForm = new CapabilityForm();
        Capability capability = new Capability();
        Employee employee = new Employee();
        CapabilityType capabilityType = new CapabilityType();
        UUID capabilityId = UUID.randomUUID();
        capability.setId(capabilityId);
        capabilityForm.setCapabilityTypeId(capabilityId);
        capabilityForm.setCapability(capability);
        UUID employeeId = UUID.randomUUID();
        when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));
        when(capabilityTypeService.findById(capabilityId)).thenReturn(Optional.of(capabilityType));
        when(capabilityService.save(capability)).thenReturn(capability);
        when(employeeService.save(employee)).thenReturn(employee);
        when(capabilityTypeService.save(capabilityType)).thenReturn(capabilityType);
        mockMvc.perform(post("/{id}/saveCapability",employeeId).flashAttr("capabilityForm",capabilityForm))
                .andExpect(redirectedUrl("/"+employeeId))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("success","Fähigkeit erfolgreich angelegt"));
    }

    @Test
    void saveCapabilityWithCapabilityTypeServiceException() throws Exception {
        CapabilityForm capabilityForm = new CapabilityForm();
        UUID capabilityId = UUID.randomUUID();
        capabilityForm.setCapabilityTypeId(capabilityId);
        UUID employeeId = UUID.randomUUID();
        when(employeeService.findById(employeeId)).thenReturn(Optional.of(new Employee()));
        when(capabilityTypeService.findById(capabilityId)).thenThrow(CapabilityTypeServiceException.class);
        mockMvc.perform(post("/{id}/saveCapability",employeeId).flashAttr("capabilityForm",capabilityForm))
                .andExpect(redirectedUrl("/"+employeeId))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Es ist ein Fehler beim speichern der Fähigkeit aufgetreten."));
    }

    @Test
    void saveCapabilityWithEmployeeServiceException() throws Exception {
        CapabilityForm capabilityForm = new CapabilityForm();
        UUID capabilityId = UUID.randomUUID();
        capabilityForm.setCapabilityTypeId(capabilityId);
        UUID employeeId = UUID.randomUUID();
        when(employeeService.findById(employeeId)).thenThrow(EmployeeServiceException.class);
        mockMvc.perform(post("/{id}/saveCapability",employeeId).flashAttr("capabilityForm",capabilityForm))
                .andExpect(redirectedUrl("/"+employeeId))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Es ist ein Fehler beim speichern des Mitarbeiters aufgetreten."));
    }

    @Test
    void deleteCapability() throws Exception {
        UUID employeeId = UUID.randomUUID();
        UUID capabilityId = UUID.randomUUID();
        Employee employee = new Employee();
        CapabilityType capabilityType = new CapabilityType();
        Capability capability = new Capability();
        capability.setCapabilityType(capabilityType);
        when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));
        when(capabilityService.findById(capabilityId)).thenReturn(Optional.of(capability));
        when(employeeService.save(employee)).thenReturn(employee);
        when(capabilityService.deleteById(capabilityId)).thenReturn(true);
        when(capabilityTypeService.save(capabilityType)).thenReturn(capabilityType);
        mockMvc.perform(get("/{id}/{capabilityId}/deleteCapability",employeeId,capabilityId))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"+employeeId))
                .andExpect(flash().attribute("success","Fähigkeit erfolgreich gelöscht"));
    }

    @Test
    void deleteCapabilityWithCapabilityTypeServiceException() throws Exception {
        UUID employeeId = UUID.randomUUID();
        UUID capabilityId = UUID.randomUUID();
        Employee employee = new Employee();
        CapabilityType capabilityType = new CapabilityType();
        Capability capability = new Capability();
        capability.setCapabilityType(capabilityType);
        when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));
        when(capabilityService.findById(capabilityId)).thenReturn(Optional.of(capability));
        when(employeeService.save(employee)).thenReturn(employee);
        when(capabilityService.deleteById(capabilityId)).thenReturn(true);
        when(capabilityTypeService.save(capabilityType)).thenThrow(CapabilityServiceException.class);
        mockMvc.perform(get("/{id}/{capabilityId}/deleteCapability",employeeId,capabilityId))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"+employeeId))
                .andExpect(flash().attribute("danger","Fähigkeit konnte nicht gelöscht werden..."));
    }
}