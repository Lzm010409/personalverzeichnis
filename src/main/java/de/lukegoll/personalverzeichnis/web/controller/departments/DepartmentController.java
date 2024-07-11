package de.lukegoll.personalverzeichnis.web.controller.departments;

import de.lukegoll.personalverzeichnis.domain.entities.Department;
import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.entities.Employment;
import de.lukegoll.personalverzeichnis.domain.entities.Salutation;
import de.lukegoll.personalverzeichnis.domain.exceptions.DepartmentServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmployeeServiceException;
import de.lukegoll.personalverzeichnis.domain.services.DepartmentService;
import de.lukegoll.personalverzeichnis.domain.services.EmployeeService;
import de.lukegoll.personalverzeichnis.web.form.employee.EmployeeForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/department")
@Slf4j
public class DepartmentController {


    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping()
    public String getDepartment(Model model, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "1", name = "pageNo") int pageNo, @RequestParam(defaultValue = "10", name = "pageSize") int pageSize,
                                RedirectAttributes redirectAttributes
    ) {
        try {
            Page<Department> page;
            if (Objects.nonNull(keyword) && !keyword.isEmpty()) {
                page = departmentService.findPagedByKeyword(keyword, PageRequest.of(pageNo - 1, pageSize));
                model.addAttribute("departments", page.getContent());
            } else {
                page = departmentService.findPaged(PageRequest.of(pageNo - 1, pageSize));
                model.addAttribute("departments", page.getContent());
            }
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalElements", page.getTotalElements());
            model.addAttribute("title", "Abteilungsverwaltung");
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            log.error("Abteilungsseite konnte nicht geladen werden! " + e);
            redirectAttributes.addFlashAttribute("danger", "Abteilungsseite konnte nicht geladen werden..." + e.getMessage());
            return "redirect:/";
        }
        return "departments/departments";
    }


    @GetMapping("/{id}")
    public String editDepartment(@PathVariable("id") UUID departmentId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<Department> departmentOptional = departmentService.findById(departmentId);
            if (departmentOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("danger", "Abteilung wurde nicht gefunden...");
                return "redirect:/department";
            }
            model.addAttribute("department", departmentOptional.get());
            return "departments/editDepartment";

        } catch (DepartmentServiceException e) {
            log.error("Fehler beim Abrufen des Departments..." + departmentId);
            return "redirect:/department";
        }
    }

    @GetMapping("/addDepartment")
    public String addDepartment(Model model) {
        try {
            Department department = new Department();
            model.addAttribute("department", department);
            return "departments/editDepartment";

        } catch (DepartmentServiceException e) {
            log.error("Fehler bei der Erstellung eines neuen Kontakts");
            return "redirect:/";
        }
    }

    @PostMapping("/saveDepartment")
    public String saveDepartment(@ModelAttribute("department") Department department, RedirectAttributes redirectAttributes) {
        try {
            departmentService.save(department);
            redirectAttributes.addFlashAttribute("success", "Abteilung gespeichert...");
            return "redirect:/department";
        } catch (DepartmentServiceException e) {
            log.error("Fehler beim speichern der Abteilung..." + e.getMessage() + e);
            redirectAttributes.addFlashAttribute("danger", "Fehler beim speichern der Abteilung, versuchen Sie es später nochmal.");
            return "redirect:/department";
        }
    }

    @GetMapping("/{id}/deleteDepartment")
    public String deleteDepartment(RedirectAttributes redirectAttributes, @PathVariable("id") UUID departmentId) {
        if (departmentId == null) {
            redirectAttributes.addFlashAttribute("danger", "Abteilungs-ID nicht vorhanden. Mitarbeiter konnte nicht gelöscht werden.");
            return "redirect:/department";
        }
        try {
            departmentService.deleteById(departmentId);
            redirectAttributes.addFlashAttribute("success", "Abteilung gelöscht...");
            return "redirect:/department";
        } catch (DataIntegrityViolationException e) {
            log.error("Abteilung konnte nicht gelöscht werden, da noch Mitarbeiter mit dieser verknüpft sind...");
            redirectAttributes.addFlashAttribute("danger", "Abteilung konnte nicht gelöscht werden, da noch Mitarbeiter mit dieser Abteilung exstieren.");
            return "redirect:/department";
        } catch (DepartmentServiceException e) {
            log.error("Abteilung konnte nicht gelöscht werden..." + departmentId + e);
            redirectAttributes.addFlashAttribute("danger", "Abteilung konnte nicht gelöscht werden...");
            return "redirect:/department";
        }
    }
    @GetMapping("/cancel")
    public String cancel(Model model) {
        return "redirect:/department";
    }
}
