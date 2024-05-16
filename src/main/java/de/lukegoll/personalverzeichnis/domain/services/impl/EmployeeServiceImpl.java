package de.lukegoll.personalverzeichnis.domain.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.exceptions.EmployeeServiceException;
import de.lukegoll.personalverzeichnis.domain.repos.EmployeeRepository;
import de.lukegoll.personalverzeichnis.domain.services.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private final EmployeeRepository employeeRepository;


    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Override
    public Employee save(Employee entity) {
        try {
            return employeeRepository.save(entity);
        } catch (Exception e) {
            throw new EmployeeServiceException("Es ist ein Fehler bei der Speicherung der Employee aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public List<Employee> findAll() {
        try {
            return employeeRepository.findAll();
        } catch (Exception e) {
            throw new EmployeeServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public Page<Employee> findPaged(Pageable pageable) {
        try {
            return employeeRepository.findAllByOrderByCreateDateDesc(pageable);
        } catch (Exception e) {
            throw new EmployeeServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }

    @Override
    public Page<Employee> findPagedByKeyword(String keyword, Pageable pageable) {
        try {
            return employeeRepository.findPaginatedByKeyword(keyword, pageable);
        } catch (Exception e) {
            throw new EmployeeServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public Optional<Employee> findById(UUID id) {
        try {
            return employeeRepository.findById(id);
        } catch (Exception e) {
            throw new EmployeeServiceException("Es ist ein Fehler beim Abrufen der Employee aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public boolean deleteById(UUID id) {
        try {
            employeeRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new EmployeeServiceException("Es ist ein Fehler beim Abrufen der Employee aufgetreten..." + e.getMessage(), e);
        }

    }


    @Override
    public boolean deleteAll() {
        try {
            employeeRepository.deleteAll();
            return true;
        } catch (Exception e) {
            throw new EmployeeServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }


}
