package de.lukegoll.personalverzeichnis.domain.repos;


import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import de.lukegoll.personalverzeichnis.domain.entities.Timesheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.List;
import java.util.UUID;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, UUID>, JpaSpecificationExecutor<Timesheet>, PagingAndSortingRepository<Timesheet, UUID> {

	
 

@Query("""
            select t from Timesheet t join fetch t.employee where t.endTime is NULL order by t.startTime desc
        """)
    Page<Timesheet> findAllByOrderByCreateDateDesc(Pageable pageable);

@Query("""
            select t from Timesheet t left join t.employee e 
            where e.id = :employeeId
            order by t.startTime desc 
        """)
List<Timesheet> findAllByEmployeeId(@Param("employeeId")UUID id);

}
