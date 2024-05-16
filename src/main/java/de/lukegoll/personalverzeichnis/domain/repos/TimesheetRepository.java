package de.lukegoll.personalverzeichnis.domain.repos;


import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import de.lukegoll.personalverzeichnis.domain.entities.Timesheet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, UUID>, PagingAndSortingRepository<Timesheet, UUID> {

	
 

@Query("""
            select t from Timesheet t where t.endTime is NULL order by t.startTime desc
        """)
    Page<Timesheet> findAllByOrderByCreateDateDesc(Pageable pageable);


}
