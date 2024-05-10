package de.lukegoll.personalverzeichnis.domain.repos;


import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID>, PagingAndSortingRepository<Employee, UUID> {

	
    Page<Employee> findPaginatedByKeyword(@Param("searchTerm") String keyword, Pageable pageable);




    Page<Employee> findAllByOrderByCreateDateDesc(Pageable pageable);


}
