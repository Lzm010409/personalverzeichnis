package de.lukegoll.personalverzeichnis.web.controller.capabilities;

import de.lukegoll.personalverzeichnis.domain.entities.CapabilityType;
import de.lukegoll.personalverzeichnis.domain.exceptions.CapabilityTypeServiceException;
import de.lukegoll.personalverzeichnis.domain.services.CapabilityTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/capabilitytype")
@Slf4j
public class CapabilityTypeController {


    private final CapabilityTypeService capabilityTypeService;

    @Autowired
    public CapabilityTypeController(CapabilityTypeService capabilityTypeService) {
        this.capabilityTypeService = capabilityTypeService;
    }

    @GetMapping()
    private String getCapabilityTypes(Model model, @RequestParam(required = false) String keyword, @RequestParam(defaultValue = "1", name = "pageNo") int pageNo, @RequestParam(defaultValue = "10", name = "pageSize") int pageSize,
                                 RedirectAttributes redirectAttributes
    ) {
        try {
            Page<CapabilityType> page;
            if (Objects.nonNull(keyword) && !keyword.isEmpty()) {
                page = capabilityTypeService.findPagedByKeyword(keyword, PageRequest.of(pageNo - 1, pageSize));
                model.addAttribute("capabilities", page.getContent());
            } else {
                page = capabilityTypeService.findPaged(PageRequest.of(pageNo - 1, pageSize));
                model.addAttribute("capabilities", page.getContent());
            }
            model.addAttribute("currentPage", pageNo);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalElements", page.getTotalElements());
            model.addAttribute("title", "Fähigkeiten Verwaltung");
            model.addAttribute("keyword", keyword);
        } catch (Exception e) {
            log.error("Fähigkeitenseite konnte nicht geladen werden! " + e);
            redirectAttributes.addFlashAttribute("danger", "Fähigkeitenseite konnte nicht geladen werden..." + e.getMessage());
            return "redirect:/";
        }
        return "capabilitytypes/capabilityTypes";
    }


    @GetMapping("/{id}")
    public String editCapabilityType(@PathVariable("id") UUID capabilityId, Model model, RedirectAttributes redirectAttributes) {
        try {
            Optional<CapabilityType> capabilityTypeOptional = capabilityTypeService.findById(capabilityId);
            if (capabilityTypeOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("danger", "Fähigkeit wurde nicht gefunden...");
                return "redirect:/capabilitytypes";
            }
            model.addAttribute("capabilitytype", capabilityTypeOptional.get());
            return "capabilitytypes/editCapabilityType";

        } catch (CapabilityTypeServiceException e) {
            log.error("Fehler beim Abrufen der Fähigkeit..." + capabilityId);
            return "redirect:/capabilitytype";
        }
    }

    @GetMapping("/addCapabilityType")
    public String addCapabilityType(Model model) {
        try {
            CapabilityType capabilityType = new CapabilityType();
            model.addAttribute("capabilitytype", capabilityType);
            return "capabilitytypes/editCapabilityType";

        } catch (CapabilityTypeServiceException e) {
            log.error("Fehler bei der Erstellung einer neuen Fähigkeit");
            return "redirect:/";
        }
    }

    @GetMapping("/cancel")
    public String cancel(Model model) {
        return "redirect:/capabilitytype";
    }

    @PostMapping("/saveCapabilityType")
    public String saveCapabilityType(@ModelAttribute("capability") CapabilityType capabilityType, RedirectAttributes redirectAttributes) {
        try {
            capabilityTypeService.save(capabilityType);
            redirectAttributes.addFlashAttribute("success", "Fähigkeit gespeichert...");
            return "redirect:/capabilitytype";
        } catch (CapabilityTypeServiceException e) {
            log.error("Fehler beim speichern der Fähigkeit..." + e.getMessage() + e);
            redirectAttributes.addFlashAttribute("danger", "Fehler beim speichern der Fähigkeit, versuchen Sie es später nochmal.");
            return "redirect:/capabilitytype";
        }
    }

    @GetMapping("/{id}/deleteCapabilityType")
    public String deleteCapabilityType(RedirectAttributes redirectAttributes, @PathVariable("id") UUID capabilityId) {
        if (capabilityId == null) {
            redirectAttributes.addFlashAttribute("danger", "Fähigkeits-ID nicht vorhanden. Fähigkeit konnte nicht gelöscht werden.");
            return "redirect:/capabilitytype";
        }
        try {
            capabilityTypeService.deleteById(capabilityId);
            redirectAttributes.addFlashAttribute("success", "Fähigkeit gelöscht...");
            return "redirect:/capabilitytype";
        }catch (DataIntegrityViolationException e) {
            log.error("Fähigkeit konnte nicht gelöscht werden, da noch Mitarbeiter mit dieser verknüpft sind...");
            redirectAttributes.addFlashAttribute("danger", "Fähigkeit konnte nicht gelöscht werden, da noch Mitarbeiter mit dieser Fähigkeit exstieren.");
            return "redirect:/capabilitytype";
        } catch (CapabilityTypeServiceException e) {
            log.error("Fähigkeit konnte nicht gelöscht werden..." + capabilityId + e);
            redirectAttributes.addFlashAttribute("danger", "Fähigkeit konnte nicht gelöscht werden...");
            return "redirect:/capabilitytype";
        }
    }

}
