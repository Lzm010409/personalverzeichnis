package de.lukegoll.personalverzeichnis.web.controller.employees;

import de.lukegoll.personalverzeichnis.domain.entities.Department;
import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.entities.Employment;
import de.lukegoll.personalverzeichnis.domain.entities.Salutation;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmployeeServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmploymentServiceException;
import de.lukegoll.personalverzeichnis.domain.services.DepartmentService;
import de.lukegoll.personalverzeichnis.domain.services.EmployeeService;
import de.lukegoll.personalverzeichnis.domain.services.EmploymentService;
import de.lukegoll.personalverzeichnis.web.form.employee.EmployeeForm;
import de.lukegoll.personalverzeichnis.web.form.employee.EmploymentForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping()
@Slf4j
public class EmploymentController {

    private final EmploymentService employmentService;
    private final EmployeeService employeeService;

    private final DepartmentService departmentService;

    @Autowired
    public EmploymentController(EmploymentService employmentService, EmployeeService employeeService, DepartmentService departmentService) {
        this.employmentService = employmentService;
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    @GetMapping("/{id}/addEmployment")
    public String addEmployment(Model model, @PathVariable("id") UUID employeeId, RedirectAttributes redirectAttributes) {
        try {
            if (employeeId == null) {
                redirectAttributes.addFlashAttribute("danger", "Es konnte keine Anstellung hinzugefügt werden, da die ID des Mitarbeiters nicht bekannt ist.");
                return "redirect:/";
            }
            model.addAttribute("employeeId", employeeId);
            model.addAttribute("employmentForm", new EmploymentForm(new Employment(LocalDate.now(), null, null, null), null));
            model.addAttribute("departments", departmentService.findAll());
            return "employees/employments/editEmployment";
        } catch (Exception e) {
            log.error("Employmentseite konnte nicht geladen werden! " + e);
            redirectAttributes.addFlashAttribute("danger", "Anstellungsseite konnte nicht geladen werde...");
            return "redirect:/" + employeeId;
        }
    }

    @GetMapping("/{id}/{employmentId}/editEmployment")
    public String editEmployment(Model model, @PathVariable("id") UUID employeeId, @PathVariable("employmentId") UUID employmentId, RedirectAttributes redirectAttributes) {
        try {
            if (employmentId == null) {
                redirectAttributes.addFlashAttribute("danger", "Die Anstellung konnte nicht bearbeitet werden, da die ID nicht vorhanden war.");
                return "redirect:/";
            }
            Optional<Employment> employmentOptional = employmentService.findById(employmentId);
            if (employmentOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("danger", "Anstellung konnte nicht bearbeitet werden, da Sie nicht gefunden wurde");
                if (employeeId != null) {
                    return "redirect:/" + employeeId;
                }
                return "redirect:/";
            }
            Employment employment = employmentOptional.get();
            model.addAttribute("employeeId", employeeId);
            model.addAttribute("employmentForm", new EmploymentForm(employment, employment.getDepartment() == null ? null : employment.getDepartment().getId()));
            model.addAttribute("departments", departmentService.findAll());
            return "employees/employments/editEmployment";
        } catch (Exception e) {
            log.error("Employmentseite konnte nicht geladen werden! " + e);
            redirectAttributes.addFlashAttribute("danger", "Anstellungsseite konnte nicht geladen werden...");
            return "redirect:/" + employeeId;
        }
    }

    @PostMapping("/{id}/saveEmployment")
    public String saveEmployment(@ModelAttribute("employmentForm") EmploymentForm employmentForm, @PathVariable("id") UUID employeeId
            , RedirectAttributes redirectAttributes) {
        if (employeeId == null) {
            redirectAttributes.addFlashAttribute("danger", "Anstellung konnte nicht gespeichert werden, da Sie keinem Mitarbeiter zugeordnet werden konnte...");
            return "redirect:/";
        }
        try {
            Optional<Employee> optionalEmployee = employeeService.findById(employeeId);
            if (optionalEmployee.isEmpty()) {
                redirectAttributes.addFlashAttribute("danger", "Anstellung konnte nicht gefunden werden, da der dazugehörige Mitarbeiter nicht gefunden wurde...");
                return "redirect:/";
            }
            Department department = null;
            if (employmentForm.getDepartmentId() != null) {
                Optional<Department> departmentOptional = departmentService.findById(employmentForm.getDepartmentId());
                if (departmentOptional.isPresent()) {
                    department = departmentOptional.get();
                }
            }
            Employment employment = employmentForm.getEmployment();
            employment.setDepartment(department);
            if (employment.getId() == null) {
                employmentService.save(employment);
                Employee employee = optionalEmployee.get();
                employee.attachEmployment(employment);
                employeeService.save(employee);
            } else {
                Employee employee = employeeService.findById(employeeId).get();
                employment.setEmployee(employee);
                employmentService.save(employment);
            }

            if (department != null && !department.getEmployments().contains(employment)) {
                department.attachEmployment(employment);
                departmentService.save(department);
            }


            redirectAttributes.addFlashAttribute("success", "Anstellung erfolgreich angelegt");
            return "redirect:/" + employeeId;
        } catch (EmploymentServiceException e) {
            redirectAttributes.addFlashAttribute("danger", "Es ist ein Fehler beim speichern der Anstellung aufgetreten.");
            log.error("Es ist ein Fehler beim speichern der Anstellung aufgetreten." + e);
            return "redirect:/" + employeeId;
        } catch (EmployeeServiceException e) {
            redirectAttributes.addFlashAttribute("danger", "Es ist ein Fehler beim speichern des Mitarbeiters aufgetreten.");
            log.error("Es ist ein Fehler beim speichern des Mitarbeiter aufgetreten." + e);
            return "redirect:/" + employeeId;
        }


    }




    @GetMapping("/{id}/{employmentId}/deleteEmployment")
    public String deleteEmployment(Model model, @PathVariable("id") UUID employeeId,
                                    @PathVariable("employmentId") UUID employmentId, RedirectAttributes redirectAttributes) {
        try {
            if (employmentId == null) {
                redirectAttributes.addFlashAttribute("danger", "Die Anstellung konnte nicht gelöscht werden, da keine ID vorhanden war.");
                if (employeeId == null) {
                    return "redirect:/";
                } else {
                    return "redirect:/" + employeeId;
                }
            }
            Employee employee = employeeService.findById(employeeId).get();
            Employment employment = employmentService.findById(employmentId).get();
            Department department = employment.getDepartment();
            employee.removeEmployment(employment);
            employeeService.save(employee);
            employmentService.deleteById(employmentId);
            if (department != null) {
                department.removeEmployment(employment);
                departmentService.save(department);
            }
            redirectAttributes.addFlashAttribute("success", "Anstellung erfolgreich gelöscht");
            return "redirect:/" + employeeId;
        } catch (EmploymentServiceException e) {
            log.error("Anstellung konnte nicht gelöscht werden! " + e);
            redirectAttributes.addFlashAttribute("danger", "Anstellung konnte nicht gelöscht werden...");
            return "redirect:/" + employeeId;
        }
    }


}
