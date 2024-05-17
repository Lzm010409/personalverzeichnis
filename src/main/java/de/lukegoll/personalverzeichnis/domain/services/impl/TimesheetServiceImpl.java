package de.lukegoll.personalverzeichnis.domain.services.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Timesheet;
import de.lukegoll.personalverzeichnis.domain.exceptions.TimesheetServiceException;
import de.lukegoll.personalverzeichnis.domain.repos.TimesheetRepository;
import de.lukegoll.personalverzeichnis.domain.services.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TimesheetServiceImpl implements TimesheetService {


    private final TimesheetRepository timesheetRepository;


    @Autowired
    public TimesheetServiceImpl(TimesheetRepository timesheetRepository) {
        super();
        this.timesheetRepository = timesheetRepository;
    }


    @Override
    public Timesheet save(Timesheet entity) {
        try {
            return timesheetRepository.save(entity);
        } catch (Exception e) {
            throw new TimesheetServiceException("Es ist ein Fehler bei der Speicherung der Timesheet aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public List<Timesheet> findAll() {
        try {
            return timesheetRepository.findAll();
        } catch (Exception e) {
            throw new TimesheetServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public List<Timesheet> findAllByEmployeeId(UUID id) {
        try {
            return timesheetRepository.findAllByEmployeeId(id);
        } catch (Exception e) {
            throw new TimesheetServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }

    @Override
    public Page<Timesheet> findPaged(Pageable pageable) {
        try {
            return timesheetRepository.findAllByOrderByCreateDateDesc(pageable);
        } catch (Exception e) {
            throw new TimesheetServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public Optional<Timesheet> findById(UUID id) {
        try {
            return timesheetRepository.findById(id);
        } catch (Exception e) {
            throw new TimesheetServiceException("Es ist ein Fehler beim Abrufen der Timesheet aufgetreten..." + e.getMessage(), e);
        }
    }


    @Override
    public boolean deleteById(UUID id) {
        try {
            timesheetRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new TimesheetServiceException("Es ist ein Fehler beim Abrufen der Timesheet aufgetreten..." + e.getMessage(), e);
        }

    }


    @Override
    public boolean deleteAll() {
        try {
            timesheetRepository.deleteAll();
            return true;
        } catch (Exception e) {
            throw new TimesheetServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." + e.getMessage(), e);
        }
    }


}
