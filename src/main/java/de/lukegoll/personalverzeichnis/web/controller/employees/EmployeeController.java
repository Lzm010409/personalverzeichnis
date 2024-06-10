package de.lukegoll.personalverzeichnis.web.controller.employees;

import java.time.LocalDate;
import java.util.*;

import de.lukegoll.personalverzeichnis.domain.entities.Department;
import de.lukegoll.personalverzeichnis.domain.entities.Employment;
import de.lukegoll.personalverzeichnis.domain.entities.Salutation;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmployeeServiceException;
import de.lukegoll.personalverzeichnis.domain.services.CapabilityTypeService;
import de.lukegoll.personalverzeichnis.domain.services.DepartmentService;
import de.lukegoll.personalverzeichnis.domain.services.EmployeeService;
import de.lukegoll.personalverzeichnis.web.dto.EmployeeFilterDTO;
import de.lukegoll.personalverzeichnis.web.form.employee.EmployeeForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    private final DepartmentService departmentService;

    private final CapabilityTypeService capabilityTypeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService, CapabilityTypeService capabilityTypeService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.capabilityTypeService = capabilityTypeService;
    }

    @GetMapping()
    private String getEmployees(Model model, @RequestParam(required = false) String keyword,
                                @RequestParam(required = false) String capabilityType, @RequestParam(defaultValue = "1", name = "pageNo") int pageNo, @RequestParam(defaultValue = "10", name = "pageSize") int pageSize
            , @RequestParam(required = false, name = "caseId") Long caseId) {
        try {
            Page<Employee> page;
            if (Objects.nonNull(keyword) && !keyword.isEmpty()) {
                page = employeeService.findFiltered(new EmployeeFilterDTO(keyword), PageRequest.of(pageNo - 1, pageSize));
                model.addAttribute("employees", page.getContent());
            } else {
                page = employeeService.findPaged(PageRequest.of(pageNo - 1, pageSize));
                model.addAttribute("employees", page.getContent());
            }
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalElements", page.getTotalElements());
            model.addAttribute("title", "Mitarbeiterverwaltung");
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            log.error("Mitarbeiterseite konnte nicht geladen werden! " + e);
        }
        return "employees/verwaltung";
    }


    @GetMapping("/{id}")
    public String editEmployee(@PathVariable("id") UUID employeeId, Model model, RedirectAttributes redirectAttributes) {
        try {
            EmployeeForm employeeForm = new EmployeeForm();
            Optional<Employee> employee = employeeService.findById(employeeId);
            if (employee.isEmpty()) {
                redirectAttributes.addFlashAttribute("danger", "Mitarbeiter wurde nicht gefunden...");
                return "redirect:/";
            }
            employeeForm.setEmployee(employee.get());
            model.addAttribute("employeeForm", employeeForm);
            model.addAttribute("salutation", Salutation.values());
            return "employees/editEmployee";

        } catch (EmployeeServiceException e) {
            log.error("Der Employee wurde nicht gefunden..." + employeeId);
            return "redirect:/";
        }
    }

    @GetMapping("/addEmployee")
    public String editEmployee(Model model) {
        try {
            Employee employee = new Employee();
            EmployeeForm employeeForm = new EmployeeForm();
            employeeForm.setEmployee(employee);
            employeeForm.setStartDate(LocalDate.now());
            model.addAttribute("employeeForm", employeeForm);
            model.addAttribute("salutation", Salutation.values());
            model.addAttribute("departments", departmentService.findAll());
            return "employees/editEmployee";

        } catch (EmployeeServiceException e) {
            log.error("Fehler bei der Erstellung eines neuen Kontakts");
            return "redirect:/";
        }
    }

    @PostMapping("/saveEmployee")
    public String saveEmpployee(@ModelAttribute("employee") EmployeeForm employeeForm, RedirectAttributes redirectAttributes) {
        try {
            Employee employee = employeeForm.getEmployee();
            Department department = null;
            if (employeeForm.getDepartmentId() != null) {
                Optional<Department> departmentOptional = departmentService.findById(employeeForm.getDepartmentId());
                if (departmentOptional.isPresent()) {
                    department = departmentOptional.get();
                }
            }
            if (employeeForm.getStartDate() != null) {
                Employment employment = new Employment(employeeForm.getStartDate(), null, employeeForm.getEmployee(), department);
                employee.setEmployments(Arrays.asList(employment));
                if (department != null) {
                    department.attachEmployment(employment);
                    departmentService.save(department);
                }
            }
            employeeService.save(employee);
            redirectAttributes.addFlashAttribute("success", "Mitarbeiter gespeichert...");
            return "redirect:/";
        } catch (EmployeeServiceException e) {
            log.error("Fehler beim speichern des Kontaktes..." + e.getMessage() + e);
            redirectAttributes.addFlashAttribute("danger", "Fehler beim speichern des Mitarbeiters, versuchen Sie es später nochmal.");
            return "redirect:/";
        }
    }

    @GetMapping("/{id}/deleteEmployee")
    public String deleteEmployee(RedirectAttributes redirectAttributes, @PathVariable("id") UUID employeeId) {
        if (employeeId == null) {
            redirectAttributes.addFlashAttribute("danger", "Mitarbeiter ID nicht vorhanden. Mitarbeiter konnte nicht gelöscht werden.");
            return "redirect:/";
        }
        try {
            employeeService.deleteById(employeeId);
            redirectAttributes.addFlashAttribute("success", "Mitarbeiter gelöscht...");
            return "redirect:/";
        } catch (EmployeeServiceException e) {
            log.error("Mitarbeiter konnte nicht gelöscht werden..." + employeeId + e);
            redirectAttributes.addFlashAttribute("danger", "Mitarbeiter konnte nicht gelöscht werden...");
            return "redirect:/";
        }
    }

}
