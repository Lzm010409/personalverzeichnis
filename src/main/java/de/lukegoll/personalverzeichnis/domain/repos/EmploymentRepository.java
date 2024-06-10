package de.lukegoll.personalverzeichnis.domain.repos;


import java.util.UUID;

import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.lukegoll.personalverzeichnis.domain.entities.Employment;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, UUID>, JpaSpecificationExecutor<Employment>, PagingAndSortingRepository<Employment, UUID> {

	



    Page<Employment> findAllByOrderByCreateDateDesc(Pageable pageable);


}
