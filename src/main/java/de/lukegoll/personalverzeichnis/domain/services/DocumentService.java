package de.lukegoll.personalverzeichnis.domain.services;


import de.lukegoll.personalverzeichnis.domain.entities.Document;
import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.web.dto.EmployeeFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface DocumentService extends EntityService<Document> {


}
