package de.lukegoll.personalverzeichnis.web.controller.timesheets;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.entities.Timesheet;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmployeeServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.TimesheetServiceException;
import de.lukegoll.personalverzeichnis.domain.services.DepartmentService;
import de.lukegoll.personalverzeichnis.domain.services.EmployeeService;
import de.lukegoll.personalverzeichnis.domain.services.EmploymentService;
import de.lukegoll.personalverzeichnis.domain.services.impl.TimesheetServiceImpl;
import de.lukegoll.personalverzeichnis.web.form.timesheet.TimesheetForm;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = TimesheetController.class)
class TimesheetControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    EmployeeService employeeService;

    @MockBean
    TimesheetServiceImpl timesheetService;

    @Autowired
    public TimesheetControllerTest(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Test
    void getTimesheets() throws Exception {
        Page<Timesheet> timesheets = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 1);
        when(timesheetService.findPaged(PageRequest.of(0, 10))).thenReturn(timesheets);
        mockMvc.perform(MockMvcRequestBuilders.get("/timesheet"))
                .andExpect(status().isOk());
    }

    @Test
    void getTimesheetsWithException() throws Exception {
        Page<Timesheet> timesheets = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 1);
        when(timesheetService.findPaged(PageRequest.of(0, 10))).thenThrow(TimesheetServiceException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/timesheet"))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Stundenzettelverwaltung konnte nicht geladen werden...null"))
                .andExpect(redirectedUrl("/"));
    }
    @Test
    void stampOut() throws Exception {
        UUID timesheetId = UUID.randomUUID();
        Timesheet timesheet = new Timesheet();
        when(timesheetService.findById(timesheetId)).thenReturn(Optional.of(timesheet));
        when(this.timesheetService.save(timesheet)).thenReturn(timesheet);
        mockMvc.perform(MockMvcRequestBuilders.get("/timesheet/{id}/stampOut",timesheetId))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("success","Erfolgreich ausgestempelt..."))
                .andExpect(redirectedUrl("/timesheet"));
    }

    @Test
    void stampOutWithEmptyTimesheet() throws Exception {
        UUID timesheetId = UUID.randomUUID();
        Timesheet timesheet = new Timesheet();
        when(timesheetService.findById(timesheetId)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/timesheet/{id}/stampOut",timesheetId))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Konnte nicht ausgestempelt werden, da die Stempelungsdaten nicht gefunden wurden."))
                .andExpect(redirectedUrl("/timesheet"));
    }
    @Test
    void stampOutWithTimesheetServiceException() throws Exception {
        UUID timesheetId = UUID.randomUUID();
        Timesheet timesheet = new Timesheet();
        when(timesheetService.findById(timesheetId)).thenThrow(TimesheetServiceException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/timesheet/{id}/stampOut",timesheetId))
                .andExpect(status().isFound())
                .andExpect(flash().attribute("danger","Konnte nicht ausgestempelt werden, da ein Fehler aufgetreten ist: null"))
                .andExpect(redirectedUrl("/timesheet"));
    }

    @Test
    void getAllTimesheet() throws Exception {
        UUID employeeId = UUID.randomUUID();
        when(timesheetService.findAllByEmployeeId(employeeId)).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.get("/timesheet/{id}",employeeId))
                .andExpect(status().isOk());
    }

    @Test
    void stampIn() throws Exception {
        TimesheetForm timesheetForm = new TimesheetForm();
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        when(employeeService.findById(employeeId)).thenReturn(Optional.of(employee));
        when(employeeService.save(employee)).thenReturn(employee);
        mockMvc.perform(MockMvcRequestBuilders.post("/timesheet/stampIn")
                .flashAttr("timesheetForm",timesheetForm))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/timesheet"));
    }

    @Test
    void stampInWithEmptyEmployee() throws Exception {
        TimesheetForm timesheetForm = new TimesheetForm();
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee();
        timesheetForm.setEmployeeId(employeeId.toString());
        when(employeeService.findById(employeeId)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.post("/timesheet/stampIn")
                        .flashAttr("timesheetForm",timesheetForm))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/timesheet"))
                .andExpect(flash().attribute("danger","Mitarbeiter konnte nicht eingestempelt werden, da er nicht gefunden wurde..."));
    }
    @Test
    void stampInWithEmployeeServiceException() throws Exception {
        TimesheetForm timesheetForm = new TimesheetForm();
        UUID employeeId = UUID.randomUUID();
        timesheetForm.setEmployeeId(employeeId.toString());
        when(employeeService.findById(employeeId)).thenThrow(EmployeeServiceException.class);
        mockMvc.perform(MockMvcRequestBuilders.post("/timesheet/stampIn")
                        .flashAttr("timesheetForm",timesheetForm))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/timesheet"))
                .andExpect(flash().attribute("danger","Die Einstemeplung konnte nicht mit dem Mitarbeiter verknüpft werden. Vorgang abgebrochen..."));
    }
}