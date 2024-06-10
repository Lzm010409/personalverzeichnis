package de.lukegoll.personalverzeichnis.web.controller.employees;

import de.lukegoll.personalverzeichnis.domain.entities.*;
import de.lukegoll.personalverzeichnis.domain.exceptions.CapabilityServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.CapabilityTypeServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmployeeServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmploymentServiceException;
import de.lukegoll.personalverzeichnis.domain.services.CapabilityService;
import de.lukegoll.personalverzeichnis.domain.services.CapabilityTypeService;
import de.lukegoll.personalverzeichnis.domain.services.EmployeeService;
import de.lukegoll.personalverzeichnis.domain.services.EmploymentService;
import de.lukegoll.personalverzeichnis.web.form.employee.CapabilityForm;
import de.lukegoll.personalverzeichnis.web.form.employee.EmploymentForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping()
@Slf4j
public class CapabilityController {

    private final CapabilityService capabilityService;
    private final EmployeeService employeeService;

    private final CapabilityTypeService capabilityTypeService;

    @Autowired
    public CapabilityController(CapabilityService capabilityService, EmployeeService employeeService, CapabilityTypeService capabilityTypeService) {
        this.capabilityService = capabilityService;
        this.employeeService = employeeService;
        this.capabilityTypeService = capabilityTypeService;
    }

    @GetMapping("/{id}/addCapability")
    private String addCapability(Model model, @PathVariable("id") UUID employeeId, RedirectAttributes redirectAttributes) {
        try {
            if (employeeId == null) {
                redirectAttributes.addFlashAttribute("danger", "Es konnte keine Fähigkeit hinzugefügt werden, da die ID des Mitarbeiters nicht bekannt ist.");
                return "redirect:/";
            }
            model.addAttribute("employeeId", employeeId);
            model.addAttribute("capabilityForm", new CapabilityForm(new Capability(), null));
            model.addAttribute("capabilityTypes", capabilityTypeService.findAll());
            return "employees/capabilities/editCapability";
        } catch (Exception e) {
            log.error("Fähigkeitsseite konnte nicht geladen werden! " + e);
            redirectAttributes.addFlashAttribute("danger", "Fähigkeitsseite konnte nicht geladen werde...");
            return "redirect:/" + employeeId;
        }
    }

    @GetMapping("/{id}/{capabilityId}/editCapability")
    private String editCapability(Model model, @PathVariable("id") UUID employeeId, @PathVariable("capabilityId") UUID capabilityId, RedirectAttributes redirectAttributes) {
        try {
            if (capabilityId == null) {
                redirectAttributes.addFlashAttribute("danger", "Die Fähigkeit konnte nicht bearbeitet werden, da die ID nicht vorhanden war.");
                return "redirect:/";
            }
            Optional<Capability> capabilityOptional = capabilityService.findById(capabilityId);
            if (capabilityOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("danger", "Fähigkeit konnte nicht bearbeitet werden, da Sie nicht gefunden wurde");
                if (employeeId != null) {
                    return "redirect:/" + employeeId;
                }
                return "redirect:/";
            }
            Capability capability = capabilityOptional.get();
            model.addAttribute("employeeId", employeeId);
            model.addAttribute("capabilityForm", new CapabilityForm(capability,capability.getCapabilityType()==null ? null : capability.getCapabilityType().getId()));
            model.addAttribute("capabilityTypes", capabilityTypeService.findAll());
            return "employees/capabilities/editCapability";
        } catch (Exception e) {
            log.error("Fähigkeitsseite konnte nicht geladen werden! " + e);
            redirectAttributes.addFlashAttribute("danger", "Fähigkeitsseite konnte nicht geladen werde...");
            return "redirect:/" + employeeId;
        }
    }

    @PostMapping("/{id}/saveCapability")
    public String saveDeployment(@ModelAttribute("capabilityForm") CapabilityForm capabilityForm, @PathVariable("id") UUID employeeId
            , RedirectAttributes redirectAttributes) {
        if (employeeId == null) {
            redirectAttributes.addFlashAttribute("danger", "Fähigkeit konnte nicht gespeichert werden, da Sie keinem Mitarbeiter zugeordnet werden konnte...");
            return "redirect:/";
        }
        try {
            Optional<Employee> optionalEmployee = employeeService.findById(employeeId);
            if (optionalEmployee.isEmpty()) {
                redirectAttributes.addFlashAttribute("danger", "Fähigkeit konnte nicht gespeichert werden, da der dazugehörige Mitarbeiter nicht gefunden wurde...");
                return "redirect:/";
            }
            CapabilityType capabilityType = null;
            if (capabilityForm.getCapabilityTypeId() != null) {
                Optional<CapabilityType> capabilityTypeOptional = capabilityTypeService.findById(capabilityForm.getCapabilityTypeId());
                if (capabilityTypeOptional.isPresent()) {
                    capabilityType = capabilityTypeOptional.get();
                }
            }
            Capability capability = capabilityForm.getCapability();
            capability.setCapabilityType(capabilityType);
            if (capability.getId() == null) {
                capabilityService.save(capability);
                Employee employee = optionalEmployee.get();
                employee.attachCapability(capability);
                employeeService.save(employee);
            } else {
                Employee employee = employeeService.findById(employeeId).get();
                capability.setEmployee(employee);
                capabilityService.save(capability);
            }

            if (capabilityType != null && !capabilityType.getCapabilities().contains(capability)) {
                capabilityType.attachCapability(capability);
                capabilityTypeService.save(capabilityType);
            }


            redirectAttributes.addFlashAttribute("success", "Fähigkeit erfolgreich angelegt");
            return "redirect:/" + employeeId;
        } catch (CapabilityTypeServiceException e) {
            redirectAttributes.addFlashAttribute("danger", "Es ist ein Fehler beim speichern der Fähigkeit aufgetreten.");
            log.error("Es ist ein Fehler beim speichern der Fähigkeit aufgetreten." + e);
            return "redirect:/" + employeeId;
        } catch (EmployeeServiceException e) {
            redirectAttributes.addFlashAttribute("danger", "Es ist ein Fehler beim speichern des Mitarbeiters aufgetreten.");
            log.error("Es ist ein Fehler beim speichern des Mitarbeiter aufgetreten." + e);
            return "redirect:/" + employeeId;
        }


    }


    @GetMapping("/{id}/{capabilityId}/deleteCapability")
    private String deleteEmployment(Model model, @PathVariable("id") UUID employeeId,
                                    @PathVariable("capabilityId") UUID capabilityId, RedirectAttributes redirectAttributes) {
        try {
            if (capabilityId == null) {
                redirectAttributes.addFlashAttribute("danger", "Die Fähigkeit konnte nicht gelöscht werden, da keine ID vorhanden war.");
                if (employeeId == null) {
                    return "redirect:/";
                } else {
                    return "redirect:/" + employeeId;
                }
            }
            Employee employee = employeeService.findById(employeeId).get();
            Capability capability = capabilityService.findById(capabilityId).get();
            CapabilityType capabilityType = capability.getCapabilityType();
            employee.removeCapability(capability);
            employeeService.save(employee);
            capabilityService.deleteById(capabilityId);
            if (capabilityType != null) {
                capabilityType.removeCapability(capability);
                capabilityTypeService.save(capabilityType);
            }
            redirectAttributes.addFlashAttribute("success", "Fähigkeit erfolgreich gelöscht");
            return "redirect:/" + employeeId;
        } catch (CapabilityServiceException e) {
            log.error("Fähigkeit konnte nicht gelöscht werden! " + e);
            redirectAttributes.addFlashAttribute("danger", "Fähigkeit konnte nicht gelöscht werde...");
            return "redirect:/" + employeeId;
        }
    }


}
