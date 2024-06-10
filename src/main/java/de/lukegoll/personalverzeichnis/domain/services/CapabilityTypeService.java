package de.lukegoll.personalverzeichnis.domain.services;


import de.lukegoll.personalverzeichnis.domain.entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.lukegoll.personalverzeichnis.domain.entities.CapabilityType;

@Service
public interface CapabilityTypeService extends EntityService<CapabilityType> {
    Page<CapabilityType> findPagedByKeyword(String keyword, Pageable pageable);

    CapabilityType findByName(String name);

}
