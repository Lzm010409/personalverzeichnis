package de.lukegoll.personalverzeichnis.domain.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import de.lukegoll.personalverzeichnis.domain.entities.Capability;
import de.lukegoll.personalverzeichnis.domain.exceptions.CapabilityServiceException;
import de.lukegoll.personalverzeichnis.domain.repos.CapabilityRepository;
import de.lukegoll.personalverzeichnis.domain.services.CapabilityService;
import org.springframework.stereotype.Service;

@Service
public class CapabilityServiceImpl implements CapabilityService{

	
	private final CapabilityRepository capabilityRepository;
	
	
	@Autowired
	public CapabilityServiceImpl(CapabilityRepository capabilityRepository) {
		super();
		this.capabilityRepository = capabilityRepository;
	}


	@Override
	public Capability save(Capability entity) {
		try {
			return capabilityRepository.save(entity);
		}catch (Exception e) {
			throw new CapabilityServiceException("Es ist ein Fehler bei der Speicherung der Capability aufgetreten..." +e.getMessage(),e);
		}
	}


	@Override
	public List<Capability> findAll() {
		try {
			return capabilityRepository.findAll();
		}catch (Exception e) {
			throw new CapabilityServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." +e.getMessage(),e);
		}
	}


	@Override
	public Page<Capability> findPaged(Pageable pageable) {
		try {
			return capabilityRepository.findAllByOrderByCreateDateDesc(pageable);
		}catch (Exception e) {
			throw new CapabilityServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." +e.getMessage(),e);
		}
	}


	@Override
	public Optional<Capability> findById(UUID id) {
		try {
			return capabilityRepository.findById(id);
		}catch (Exception e) {
			throw new CapabilityServiceException("Es ist ein Fehler beim Abrufen der Capability aufgetreten..." +e.getMessage(),e);
		}
	}


	@Override
	public boolean deleteById(UUID id) {
		try {
			 capabilityRepository.deleteById(id);
			 return true; 
		}catch (Exception e) {
			throw new CapabilityServiceException("Es ist ein Fehler beim Abrufen der Capability aufgetreten..." +e.getMessage(),e);
		}
		
	}


	@Override
	public boolean deleteAll() {
		try {
			 capabilityRepository.deleteAll();
			 return true; 
		}catch (Exception e) {
			throw new CapabilityServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." +e.getMessage(),e);
		}
	}


	
	

}
