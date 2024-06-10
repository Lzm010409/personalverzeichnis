package de.lukegoll.personalverzeichnis.domain.repos;


import java.util.UUID;

import de.lukegoll.personalverzeichnis.domain.entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.lukegoll.personalverzeichnis.domain.entities.Capability;

@Repository
public interface CapabilityRepository extends JpaRepository<Capability, UUID>,
        JpaSpecificationExecutor<Capability>, PagingAndSortingRepository<Capability, UUID> {





    Page<Capability> findAllByOrderByCreateDateDesc(Pageable pageable);


}
