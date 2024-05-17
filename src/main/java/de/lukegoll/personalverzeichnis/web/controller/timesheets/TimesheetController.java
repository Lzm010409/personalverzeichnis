package de.lukegoll.personalverzeichnis.web.controller.timesheets;

import de.lukegoll.personalverzeichnis.domain.entities.Department;
import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.entities.Timesheet;
import de.lukegoll.personalverzeichnis.domain.entities.TimesheetWrapper;
import de.lukegoll.personalverzeichnis.domain.exceptions.DepartmentServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmployeeServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.TimesheetServiceException;
import de.lukegoll.personalverzeichnis.domain.services.DepartmentService;
import de.lukegoll.personalverzeichnis.domain.services.EmployeeService;
import de.lukegoll.personalverzeichnis.domain.services.TimesheetService;
import de.lukegoll.personalverzeichnis.utils.dateutil.SortTimesheets;
import de.lukegoll.personalverzeichnis.utils.mapper.impl.TimesheetToWrapperMapper;
import de.lukegoll.personalverzeichnis.web.form.timesheet.TimesheetForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/timesheet")
@Slf4j
public class TimesheetController {


    private final TimesheetService timesheetService;
    private final EmployeeService employeeService;

    @Autowired
    public TimesheetController(TimesheetService timesheetService, EmployeeService employeeService) {
        this.timesheetService = timesheetService;
        this.employeeService = employeeService;
    }

    @GetMapping()
    private String getTimesheets(Model model, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "1", name = "pageNo") int pageNo, @RequestParam(defaultValue = "10", name = "pageSize") int pageSize,
                                 RedirectAttributes redirectAttributes
    ) {
        try {

            Page<Timesheet> page = timesheetService.findPaged(PageRequest.of(pageNo - 1, pageSize));
            model.addAttribute("timesheets", page.getContent());
            model.addAttribute("employees", employeeService.findAllWhichArentStampedIn());
            model.addAttribute("timesheetForm", new TimesheetForm(""));
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalElements", page.getTotalElements());
            model.addAttribute("title", "Stundenzettelverwaltung");
        } catch (Exception e) {
            log.error("Stundenzettelverwaltung konnte nicht geladen werden! " + e);
            redirectAttributes.addFlashAttribute("danger", "Stundenzettelverwaltung konnte nicht geladen werden..." + e.getMessage());
            return "redirect:/";
        }
        return "timesheet/timesheets";
    }


    @GetMapping("/{id}/stampOut")
    public String stampOut(@PathVariable("id") UUID timesheetId, RedirectAttributes redirectAttributes) {
        try {
            if (timesheetId == null) {
                redirectAttributes.addFlashAttribute("danger", "Konnte nicht ausgestempelt werden, da keine ID angegeben wurde.");
                return "redirect:/timesheet";
            }
            Optional<Timesheet> optionalTimesheet = timesheetService.findById(timesheetId);
            if (optionalTimesheet.isEmpty()) {
                redirectAttributes.addFlashAttribute("danger", "Konnte nicht ausgestempelt werden, da die Stempelungsdaten nicht gefunden wurden.");
                return "redirect:/timesheet";
            }
            Timesheet timesheet = optionalTimesheet.get();
            timesheet.setEndTime(LocalDateTime.now());
            timesheetService.save(timesheet);
            redirectAttributes.addFlashAttribute("success", "Erfolgreich ausgestempelt...");
            return "redirect:/timesheet";
        } catch (TimesheetServiceException e) {
            log.error("Stempelung konnte aufgrund eines Fehlers nicht bezogen werden..." + e);
            redirectAttributes.addFlashAttribute("danger", "Konnte nicht ausgestempelt werden, da ein Fehler aufgetreten ist: " + e.getMessage());
            return "redirect:/timesheet";
        }
    }

    @GetMapping("/{id}")
    public String getAllTimesheet(@PathVariable("id") UUID employeeId, RedirectAttributes redirectAttributes, Model model) {
        if (employeeId == null) {
            redirectAttributes.addFlashAttribute("danger", "Stundenzettel konnten nicht bezogen werden, da keine Mitarbeiter ID übergeben wurde...");
            return "redirect:/";
        }
        List<Timesheet> timesheets = timesheetService.findAllByEmployeeId(employeeId);
        List<List<Timesheet>> sorted = SortTimesheets.sortTimesheets(timesheets);
        List<TimesheetWrapper> timesheetWrappers = sorted.stream().map(list -> new TimesheetToWrapperMapper().mapTo(list)).toList();
        model.addAttribute("timesheetsWrapper", timesheetWrappers);
        model.addAttribute("title", "Stundenzettel anzeige");
        model.addAttribute("employeeId", employeeId);
        return "timesheet/timesheetsForEmployee";

    }

    @PostMapping("/stampIn")
    public String stampIn(@ModelAttribute("timesheetForm") TimesheetForm timesheetForm, RedirectAttributes redirectAttributes) {
        try {
            if (timesheetForm.getEmployeeId() == null || timesheetForm.getEmployeeId().toString().isEmpty()) {
                redirectAttributes.addFlashAttribute("danger", "Stempelung nicht erfolgt, da kein Mitarbeiter ausgewählt wurde...");
                return "redirect:/timesheet";
            }
            UUID id;
            try {
                id = UUID.fromString(timesheetForm.getEmployeeId());
            } catch (IllegalArgumentException e) {
                log.error("Stempelung konnte nicht erstellt werden, da die Mitarbeiter ID ein ungültiges Format hat.");
                redirectAttributes.addFlashAttribute("danger", "Stempelung konnte nicht erstellt werden, da die Mitarbeiter ID ein ungültiges Format hat.");
                return "redirect:/timesheet";
            }
            Timesheet timesheet = new Timesheet();
            timesheet.setStartTime(LocalDateTime.now());
            Optional<Employee> employeeOptional = employeeService.findById(id);
            if (employeeOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("danger", "Mitarbeiter konnte nicht eingestempelt werden, da er nicht gefunden wurde...");
                return "redirect:/timesheet";
            }
            Employee employee = employeeOptional.get();
            timesheet.setEmployee(employee);
            employee.attachTimesheet(timesheet);
            employeeService.save(employee);
            redirectAttributes.addFlashAttribute("success", "Erfolgreich eingestempelt...");
            return "redirect:/timesheet";
        } catch (EmployeeServiceException e) {
            log.error("Mitarbeiter und die dazugehörige Einstempelung konnte nicht gespeichert werden..." + e);
            redirectAttributes.addFlashAttribute("danger", "Die Einstemeplung konnte nicht mit dem Mitarbeiter verknüpft werden. Vorgang abgebrochen...");
            return "redirect:/timesheet";
        }
    }

}
