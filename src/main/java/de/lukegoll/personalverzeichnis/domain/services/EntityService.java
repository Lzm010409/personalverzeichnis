package de.lukegoll.personalverzeichnis.domain.services;



import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Generic Entity Service für den Zugriff auf eine Datenbank
 *
 * @param <T>
 */
public interface EntityService<T> {

    public T save(T entity);

    public List<T> findAll();
    
    public Page<T> findPaged(Pageable pageable);

    public Optional<T> findById(UUID id);

    public boolean deleteById(UUID id);

    public boolean deleteAll();


}

