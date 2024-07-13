package de.lukegoll.personalverzeichnis.domain.specification.filter;

import org.springframework.data.jpa.domain.Specification;

public interface JPAFilter <T,U>{

    public Specification<T> filter(U u);


}
