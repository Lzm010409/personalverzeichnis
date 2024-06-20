package de.lukegoll.personalverzeichnis.web.controller.employees;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

import com.google.common.io.Files;
import de.lukegoll.personalverzeichnis.domain.entities.*;
import de.lukegoll.personalverzeichnis.domain.exceptions.DocumentServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmployeeServiceException;
import de.lukegoll.personalverzeichnis.domain.services.CapabilityTypeService;
import de.lukegoll.personalverzeichnis.domain.services.DepartmentService;
import de.lukegoll.personalverzeichnis.domain.services.DocumentService;
import de.lukegoll.personalverzeichnis.domain.services.EmployeeService;
import de.lukegoll.personalverzeichnis.web.dto.EmployeeFilterDTO;
import de.lukegoll.personalverzeichnis.web.form.employee.EmployeeForm;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    private final DepartmentService departmentService;

    private final CapabilityTypeService capabilityTypeService;
    private final DocumentService documentService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, DepartmentService departmentService, CapabilityTypeService capabilityTypeService, DocumentService documentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
        this.capabilityTypeService = capabilityTypeService;
        this.documentService = documentService;
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


    @GetMapping("/uploaddocuments")
    public String uploadDocument(Model model, @RequestParam("employeeid") UUID id) {
        model.addAttribute("employee", id);
        return "employees/documentUpload";
    }

    @PostMapping("/uploaddocuments")
    public String postUploadDocument(Model model, @RequestParam("employeeId") UUID id, @RequestParam("file") MultipartFile multipartFile
            , RedirectAttributes redirectAttributes) {
        Optional<Employee> optional = employeeService.findById(id);
        if (optional.isPresent()) {
            try {
                Document document = new Document(multipartFile.getOriginalFilename(), multipartFile.getSize(),
                        Files.getFileExtension(multipartFile.getOriginalFilename() != null ? multipartFile.getOriginalFilename() : "pdf"), multipartFile.getBytes(), optional.get());
                Employee employee = optional.get();
                employee.attachDocument(document);
                employeeService.save(employee);
                log.info("Document zum Mitarbeiter: " + id + " wurde erfolgreich hochgeladen!");
            } catch (IOException e) {
                log.error("Document konnte nicht hochgeladen werden..." + e.getMessage(), e);
                redirectAttributes.addFlashAttribute("error", "Dokument konnte nicht hochgeladen werden!");

            }
        }
        redirectAttributes.addFlashAttribute("success", "Dokument wurde erfolgreich hochgeladen!");
        return "redirect:/" + id;
    }

    @GetMapping(path = "/showfile")
    @ResponseBody
    public void showFile(@RequestParam("documentid") UUID documentId, HttpServletResponse httpServletResponse,
                         RedirectAttributes redirectAttributes) {
        if (documentId == null) {
            redirectAttributes.addFlashAttribute("danger", "Es wurde eine leere Dokumenten-ID übergeben...");
        }
        try {
            Optional<Document> optionalDocument = documentService.findById(documentId);
            if (optionalDocument.isPresent()) {
                Document document = optionalDocument.get();
                httpServletResponse.setContentType("application/pdf");
                httpServletResponse.setHeader("Content-Disposition", "inline; filename=" + document.getFileName());
                byte[] bytes = document.getBlobData();
                httpServletResponse.getOutputStream().write(bytes, 0, bytes.length);
                httpServletResponse.getOutputStream().flush();
                log.info("Dokument wird geöffnet...");
            } else {
                log.error("Datei konnte nicht geöffnet werden, da das Dokument nicht gefunden wurde...");
            }

        } catch (Exception e) {
            log.error("Ein unbekannter Fehler ist aufgetreten..." + e.getMessage(), e);
        }

    }

    @GetMapping("/deletefile")
    public String deleteFile(@RequestParam("documentid") UUID documentId, @RequestParam("employeeid") UUID employeeId, RedirectAttributes redirectAttributes) {
        if (documentId == null) {
            redirectAttributes.addFlashAttribute("danger", "Fehler beim löschen des Dokuments, die ID war null");
            return "redirect:/" + employeeId;
        }
        try {
            Optional<Document> documentOptional = documentService.findById(documentId);
            Optional<Employee> employeeOptional = employeeService.findById(employeeId);
            if (documentOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("danger", "Dokument konnte nicht gelöscht werden, da es bicht gefunden wurde...");
                return "redirect:/" + employeeId;
            }
            if (employeeOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("danger", "Dokument konnte nicht gelöscht werden, da der Mitarbeiter nicht gefunden wurde...");
                return "redirect:/";
            }


            Employee employee = employeeOptional.get();
            Document document = documentOptional.get();

            employee.removeDocument(document);
            documentService.deleteById(documentId);
            employeeService.save(employee);
            redirectAttributes.addFlashAttribute("success", "Dokument wurde gelöscht");
            return "redirect:/" + employeeId;
        } catch (DocumentServiceException e) {
            log.error("Es ist ein Fehler bei einer Datenbankabfrage im Zusammenhang mit der Löschung des Dokumentes aufgetreten..." + e.getMessage(), e);
            redirectAttributes.addFlashAttribute("danger", "Es ist ein technischer Fehler aufgetreten...");
            return "redirect:/" + employeeId;
        } catch (EmployeeServiceException e) {
            log.error("Es ist ein Fehler bei einer Datenbankabfrage im Zusammenhang mit der Speicherung des Mitarbeiters aufgetreten..." + e.getMessage(), e);
            redirectAttributes.addFlashAttribute("danger", "Es ist ein technischer Fehler aufgetreten...");
            return "redirect:/" + employeeId;
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
