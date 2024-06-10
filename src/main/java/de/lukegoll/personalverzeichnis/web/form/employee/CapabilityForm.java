package de.lukegoll.personalverzeichnis.web.form.employee;

import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import de.lukegoll.personalverzeichnis.domain.entities.Employment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CapabilityForm {


    private Capability capability;

    private UUID capabilityTypeId;

}
