package de.lukegoll.personalverzeichnis.web.controller.capabilities;

import de.lukegoll.personalverzeichnis.domain.entities.CapabilityType;
import de.lukegoll.personalverzeichnis.domain.exceptions.CapabilityServiceException;
import de.lukegoll.personalverzeichnis.domain.exceptions.CapabilityTypeServiceException;
import de.lukegoll.personalverzeichnis.domain.services.CapabilityTypeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CapabilityTypeController.class)
class CapabilityTypeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CapabilityTypeService capabilityTypeService;

    @Autowired
    public CapabilityTypeControllerTest(CapabilityTypeService capabilityTypeService) {
        this.capabilityTypeService = capabilityTypeService;
    }

    @Test
    void getCapabilityTypes() throws Exception {
        Page<CapabilityType> capabilityTypePage = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 1);
        when(capabilityTypeService.findPaged( PageRequest.of(0,10))).thenReturn(capabilityTypePage);
        mockMvc.perform(MockMvcRequestBuilders.get("/capabilitytype"))
                .andExpect(status().isOk());
    }


    @Test
    void getCapabilityTypesWithKeyword() throws Exception {
        Page<CapabilityType> capabilityTypePage = new PageImpl<>(new ArrayList<>(), PageRequest.of(0, 10), 1);
        when(capabilityTypeService.findPagedByKeyword("test", PageRequest.of(0,10))).thenReturn(capabilityTypePage);
        mockMvc.perform(MockMvcRequestBuilders.get("/capabilitytype").param("keyword","test"))
                .andExpect(status().isOk());
    }

    @Test
    void getCapabilityTypesWithException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/capabilitytype").param("keyword","test"))
                .andExpect(status().is(302));
    }

    @Test
    void editCapabilityType() throws Exception {
        UUID testID= UUID.randomUUID();
        when(capabilityTypeService.findById(testID)).thenReturn(Optional.of(new CapabilityType()));
        mockMvc.perform(MockMvcRequestBuilders.get("/capabilitytype/{id}", testID)
                ).andExpect(status().isOk());
    }

    @Test
    void editCapabilityTypeWithEmptyCapability() throws Exception {
        UUID testID= UUID.randomUUID();
        when(capabilityTypeService.findById(testID)).thenReturn(Optional.empty());
        mockMvc.perform(MockMvcRequestBuilders.get("/capabilitytype/{id}", testID)
        ).andExpect(status().is(302));
    }

    @Test
    void editCapabilityTypeWithException() throws Exception {
        UUID testID= UUID.randomUUID();
        when(capabilityTypeService.findById(testID)).thenThrow(CapabilityTypeServiceException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/capabilitytype/{id}", testID)
        ) .andExpect(status().isFound()) // 302 status code for redirect
                .andExpect(redirectedUrl("/capabilitytype"));
    }

    @Test
    void addCapabilityType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/capabilitytype/addCapabilityType")
        ).andExpect(status().is(200));
    }


    @Test
    void saveCapabilityType() throws Exception {
        CapabilityType capabilityType  = new CapabilityType();
        when(capabilityTypeService.save(capabilityType)).thenReturn(capabilityType);
        mockMvc.perform(post("/capabilitytype/saveCapabilityType")
                .flashAttr("capability",capabilityType)
        ).andExpect(status().is(302));
    }

    @Test
    void saveCapabilityTypeWithException() throws Exception {
        CapabilityType capabilityType  = new CapabilityType();
        when(capabilityTypeService.save(capabilityType)).thenThrow(CapabilityTypeServiceException.class);

        mockMvc.perform(post("/capabilitytype/saveCapabilityType")
                        .flashAttr("capability", capabilityType))
                .andExpect(status().isFound()) // 302 status code for redirect
                .andExpect(redirectedUrl("/capabilitytype"))
                .andExpect(flash().attribute("danger", "Fehler beim speichern der Fähigkeit, versuchen Sie es später nochmal."));
    }

    @Test
    void deleteCapabilityType() throws Exception {
        UUID testID= UUID.randomUUID();
        when(capabilityTypeService.deleteById(testID)).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.get("/capabilitytype/{id}/deleteCapabilityType",testID)
        ).andExpect(status().is(302));
    }

    @Test
    void deleteCapabilityTypeWithException() throws Exception {
        UUID testID= UUID.randomUUID();
        when(capabilityTypeService.deleteById(testID)).thenThrow(CapabilityTypeServiceException.class);
        mockMvc.perform(get("/capabilitytype/{id}/deleteCapabilityType",testID)
                )
                .andExpect(status().isFound()) // 302 status code for redirect
                .andExpect(redirectedUrl("/capabilitytype"))
                .andExpect(flash().attribute("danger", "Fähigkeit konnte nicht gelöscht werden..."));
    }

}