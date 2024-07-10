package de.lukegoll.personalverzeichnis.domain.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.lukegoll.personalverzeichnis.domain.entities.CapabilityType;
import de.lukegoll.personalverzeichnis.domain.exceptions.CapabilityTypeServiceException;
import de.lukegoll.personalverzeichnis.domain.repos.CapabilityTypeRepository;
import de.lukegoll.personalverzeichnis.domain.services.CapabilityTypeService;
import org.springframework.stereotype.Service;

@Service
public class CapabilityTypeServiceImpl implements CapabilityTypeService {


    private final CapabilityTypeRepository capabilityTypeRepository;


    @Autowired
    public CapabilityTypeServiceImpl(CapabilityTypeRepository capabilityTypeRepository) {
        this.capabilityTypeRepository = capabilityTypeRepository;
    }


    @Override
    public CapabilityType save(CapabilityType entity) {
        try {
            return capabilityTypeRepository.save(entity);
        } catch (Exception e) {
            throw new CapabilityTypeServiceException("Es ist ein Fehler bei der Speicherung der CapabilityType aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public List<CapabilityType> findAll() {
        try {
            return capabilityTypeRepository.findAll();
        } catch (Exception e) {
            throw new CapabilityTypeServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public Page<CapabilityType> findPaged(Pageable pageable) {
        try {
            return capabilityTypeRepository.findAllByOrderByCreateDateDesc(pageable);
        } catch (Exception e) {
            throw new CapabilityTypeServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public Optional<CapabilityType> findById(UUID id) {
        try {
            return capabilityTypeRepository.findById(id);
        } catch (Exception e) {
            throw new CapabilityTypeServiceException("Es ist ein Fehler beim Abrufen der CapabilityType aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public boolean deleteById(UUID id) {
        try {
            capabilityTypeRepository.deleteById(id);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw e;
        } catch (Exception e) {
            throw new CapabilityTypeServiceException("Es ist ein Fehler beim Abrufen der CapabilityType aufgetreten..." + e.getMessage(), e);
        }

    }


    @Override
    public boolean deleteAll() {
        try {
            capabilityTypeRepository.deleteAll();
            return true;
        } catch (Exception e) {
            throw new CapabilityTypeServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public Page<CapabilityType> findPagedByKeyword(String keyword, Pageable pageable) {
        try {
            return capabilityTypeRepository.findPaginatedByKeyword(keyword, pageable);
        } catch (Exception e) {
            throw new CapabilityTypeServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }

    @Override
    public CapabilityType findByName(String name) {
        try {
            Optional<CapabilityType> capabilityTypeOptional = capabilityTypeRepository.findByName(name);
            if (capabilityTypeOptional.isEmpty()) {
                throw new CapabilityTypeServiceException("Capability wurde nicht gefunden...");
            }
            return capabilityTypeOptional.get();
        } catch (Exception e) {
            throw new CapabilityTypeServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }
}
