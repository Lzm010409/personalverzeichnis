package de.lukegoll.personalverzeichnis.utils.mapper;

public interface EntityMapper<T, U> {


    T mapTo(U u);


    U mapFrom(T t);


}
