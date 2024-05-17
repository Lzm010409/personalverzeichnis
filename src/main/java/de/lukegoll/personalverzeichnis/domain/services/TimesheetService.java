package de.lukegoll.personalverzeichnis.domain.services;


import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import de.lukegoll.personalverzeichnis.domain.entities.Timesheet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface TimesheetService extends EntityService<Timesheet> {


    List<Timesheet> findAllByEmployeeId(UUID id);
}
