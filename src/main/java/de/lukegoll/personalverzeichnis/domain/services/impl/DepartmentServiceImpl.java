package de.lukegoll.personalverzeichnis.domain.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.lukegoll.personalverzeichnis.domain.exceptions.EmployeeServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.lukegoll.personalverzeichnis.domain.entities.Department;
import de.lukegoll.personalverzeichnis.domain.exceptions.DepartmentServiceException;
import de.lukegoll.personalverzeichnis.domain.repos.DepartmentRepository;
import de.lukegoll.personalverzeichnis.domain.services.DepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService{

	
	private final DepartmentRepository departmentRepository;
	
	
	@Autowired
	public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}


	@Override
	public Department save(Department entity) {
		try {
			return departmentRepository.save(entity);
		}catch (Exception e) {
			throw new DepartmentServiceException("Es ist ein Fehler bei der Speicherung der Department aufgetreten..." +e.getMessage(),e);
		}
	}


	@Override
	public List<Department> findAll() {
		try {
			return departmentRepository.findAll();
		}catch (Exception e) {
			throw new DepartmentServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." +e.getMessage(),e);
		}
	}


	@Override
	public Page<Department> findPaged(Pageable pageable) {
		try {
			return departmentRepository.findAllByOrderByCreateDateDesc(pageable);
		}catch (Exception e) {
			throw new DepartmentServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." +e.getMessage(),e);
		}
	}

	@Override
	public Page<Department> findPagedByKeyword(String keyword, Pageable pageable) {
		try {
			return departmentRepository.findPaginatedByKeyword(keyword, pageable);
		} catch (Exception e) {
			throw new EmployeeServiceException("Es ist ein Fehler beim Abrufen des Departments aufgetreten..." + e.getMessage(), e);
		}
	}


	@Override
	public Optional<Department> findById(UUID id) {
		try {
			return departmentRepository.findById(id);
		}catch (Exception e) {
			throw new DepartmentServiceException("Es ist ein Fehler beim Abrufen der Department aufgetreten..." +e.getMessage(),e);
		}
	}


	@Override
	public boolean deleteById(UUID id) {
		try {
			departmentRepository.deleteById(id);
			 return true; 
		}catch (Exception e) {
			throw new DepartmentServiceException("Es ist ein Fehler beim Abrufen der Department aufgetreten..." +e.getMessage(),e);
		}
		
	}


	@Override
	public boolean deleteAll() {
		try {
			departmentRepository.deleteAll();
			 return true; 
		}catch (Exception e) {
			throw new DepartmentServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." +e.getMessage(),e);
		}
	}


	
	

}
