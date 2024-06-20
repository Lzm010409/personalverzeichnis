package de.lukegoll.personalverzeichnis.domain.services.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Document;
import de.lukegoll.personalverzeichnis.domain.exceptions.DocumentServiceException;
import de.lukegoll.personalverzeichnis.domain.repos.DocumentRepository;
import de.lukegoll.personalverzeichnis.domain.services.DocumentService;
import de.lukegoll.personalverzeichnis.domain.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

	
	private final DocumentRepository documentRepository;
	
	
	@Autowired
	public DocumentServiceImpl(DocumentRepository documentRepository) {
		super();
		this.documentRepository = documentRepository;
	}


	@Override
	public Document save(Document entity) {
		try {
			return documentRepository.save(entity);
		}catch (Exception e) {
			throw new DocumentServiceException("Es ist ein Fehler bei der Speicherung der Document aufgetreten..." +e.getMessage(),e);
		}
	}


	@Override
	public List<Document> findAll() {
		try {
			return documentRepository.findAll();
		}catch (Exception e) {
			throw new DocumentServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." +e.getMessage(),e);
		}
	}


	@Override
	public Page<Document> findPaged(Pageable pageable) {
		try {
			return documentRepository.findAll(pageable);
		}catch (Exception e) {
			throw new DocumentServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." +e.getMessage(),e);
		}
	}


	@Override
	public Optional<Document> findById(UUID id) {
		try {
			return documentRepository.findById(id);
		}catch (Exception e) {
			throw new DocumentServiceException("Es ist ein Fehler beim Abrufen der Document aufgetreten..." +e.getMessage(),e);
		}
	}


	@Override
	public boolean deleteById(UUID id) {
		try {
			 documentRepository.deleteById(id);
			 return true; 
		}catch (Exception e) {
			throw new DocumentServiceException("Es ist ein Fehler beim Abrufen der Document aufgetreten..." +e.getMessage(),e);
		}
		
	}


	@Override
	public boolean deleteAll() {
		try {
			 documentRepository.deleteAll();
			 return true; 
		}catch (Exception e) {
			throw new DocumentServiceException("Es ist ein Fehler beim Abrufen der Capabilities aufgetreten..." +e.getMessage(),e);
		}
	}


	
	

}
