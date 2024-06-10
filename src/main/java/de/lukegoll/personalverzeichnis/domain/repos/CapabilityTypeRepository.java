package de.lukegoll.personalverzeichnis.domain.repos;


import java.util.Optional;
import java.util.UUID;

import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import de.lukegoll.personalverzeichnis.domain.entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import de.lukegoll.personalverzeichnis.domain.entities.CapabilityType;

@Repository
public interface CapabilityTypeRepository extends JpaRepository<CapabilityType, UUID>, JpaSpecificationExecutor<CapabilityType>, PagingAndSortingRepository<CapabilityType, UUID> {


    @Query("select c from CapabilityType c" +
            " where lower(c.name) like lower(concat('%', :searchTerm, '%'))" +
            " order by c.createDate desc ")
    Page<CapabilityType> findPaginatedByKeyword(@Param("searchTerm") String keyword, Pageable pageable);


    @Query("select c from CapabilityType c" +
            " where lower(c.name) = lower(:searchTerm)")
    Optional<CapabilityType> findByName(@Param("searchTerm") String keyword);





    Page<CapabilityType> findAllByOrderByCreateDateDesc(Pageable pageable);


}
