package de.lukegoll.personalverzeichnis.domain.services;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;

@Service
public interface EmployeeService extends EntityService<Employee> {


    Page<Employee> findPagedByKeyword(String keyword, Pageable pageable);
}
