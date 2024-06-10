package de.lukegoll.personalverzeichnis.domain.repos;


import java.util.List;
import java.util.UUID;

import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.lukegoll.personalverzeichnis.domain.entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID>, JpaSpecificationExecutor<Employee>, PagingAndSortingRepository<Employee, UUID> {


    @Query("select c from Employee c" +
            " where lower(c.firstName) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(c.name) like lower(concat('%', :searchTerm, '%'))" +
            "or lower(cast(c.address as string )) like lower(concat('%', :searchTerm, '%'))" +
            " order by c.createDate desc ")
    Page<Employee> findPaginatedByKeyword(@Param("searchTerm") String keyword, Pageable pageable);

        @Query("""
                      SELECT e
                      FROM Employee e
                      WHERE NOT EXISTS (
                          SELECT t
                          FROM Timesheet t
                          WHERE t.employee = e
                          AND t.endTime is NULL 
                      )
            
                """)
    List<Employee> findAllWhichArentStampedIn();

    Page<Employee> findAllByOrderByCreateDateDesc(Pageable pageable);


}
