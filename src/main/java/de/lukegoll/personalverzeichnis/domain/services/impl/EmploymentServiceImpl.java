package de.lukegoll.personalverzeichnis.domain.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.lukegoll.personalverzeichnis.domain.entities.Employment;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmploymentServiceException;
import de.lukegoll.personalverzeichnis.domain.repos.EmploymentRepository;
import de.lukegoll.personalverzeichnis.domain.services.EmploymentService;
import org.springframework.stereotype.Service;

@Service
public class EmploymentServiceImpl implements EmploymentService {


    private final EmploymentRepository employmentRepository;


    @Autowired
    public EmploymentServiceImpl(EmploymentRepository employmentRepository) {
        this.employmentRepository = employmentRepository;
    }


    @Override
    public Employment save(Employment entity) {
        try {
            return employmentRepository.save(entity);
        } catch (Exception e) {
            throw new EmploymentServiceException("Es ist ein Fehler bei der Speicherung der Employment aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public List<Employment> findAll() {
        try {
            return employmentRepository.findAll();
        } catch (Exception e) {
            throw new EmploymentServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public Page<Employment> findPaged(Pageable pageable) {
        try {
            return employmentRepository.findAllByOrderByCreateDateDesc(pageable);
        } catch (Exception e) {
            throw new EmploymentServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public Optional<Employment> findById(UUID id) {
        try {
            return employmentRepository.findById(id);
        } catch (Exception e) {
            throw new EmploymentServiceException("Es ist ein Fehler beim Abrufen der Employment aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public boolean deleteById(UUID id) {
        try {
            employmentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new EmploymentServiceException("Es ist ein Fehler beim Abrufen der Employment aufgetreten..." + e.getMessage(), e);
        }

    }


    @Override
    public boolean deleteAll() {
        try {
            employmentRepository.deleteAll();
            return true;
        } catch (Exception e) {
            throw new EmploymentServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }


}
