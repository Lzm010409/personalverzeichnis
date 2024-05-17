package de.lukegoll.personalverzeichnis.domain.repos;


import java.util.UUID;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.lukegoll.personalverzeichnis.domain.entities.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID>, PagingAndSortingRepository<Department, UUID> {


    @Query("select c from Department c" +
            " where lower(c.name) like lower(concat('%', :searchTerm, '%'))" +
            " order by c.createDate desc ")
    Page<Department> findPaginatedByKeyword(@Param("searchTerm") String keyword, Pageable pageable);


    Page<Department> findAllByOrderByCreateDateDesc(Pageable pageable);


}
