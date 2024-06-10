package de.lukegoll.personalverzeichnis.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "capability_types")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CapabilityType extends AbstractEntity {

    @Column(name = "capability_type")
    private String name;


    @OneToMany(mappedBy = "capabilityType", fetch = FetchType.EAGER, targetEntity = Capability.class)
    private List<Capability> capabilities = new ArrayList<>();

    public void removeCapability(Capability capability) {
        this.capabilities.remove(capability);
    }

    public void attachCapability(Capability capability) {
        this.capabilities.add(capability);
        capability.setCapabilityType(this);
    }

}
