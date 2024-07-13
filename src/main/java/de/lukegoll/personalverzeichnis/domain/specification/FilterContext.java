package de.lukegoll.personalverzeichnis.domain.specification;

import org.springframework.data.jpa.domain.Specification;

public interface FilterContext <T, U>{

    public Specification<T> fillContext(U filterObject);


}
