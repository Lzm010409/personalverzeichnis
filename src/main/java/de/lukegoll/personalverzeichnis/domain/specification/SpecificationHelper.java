package de.lukegoll.personalverzeichnis.domain.specification;

import org.springframework.data.jpa.domain.Specification;

/**
 * Helper Klasse für einen Specifiaction Generator
 */
public interface SpecificationHelper<T, U> {

    Specification<T> createSpecifiaction(U u)
            ;

}
