package de.lukegoll.personalverzeichnis.domain.specification.filter;

public interface JPAFilter <T,U>{

    public T filter(U u);


}
