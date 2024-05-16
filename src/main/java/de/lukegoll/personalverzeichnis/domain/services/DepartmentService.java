package de.lukegoll.personalverzeichnis.domain.services;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.lukegoll.personalverzeichnis.domain.entities.Department;

@Service
public interface DepartmentService extends EntityService<Department > {


    Page<Department> findPagedByKeyword(String keyword, Pageable pageable);
}
