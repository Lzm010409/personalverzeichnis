package de.lukegoll.personalverzeichnis.domain.services.impl;

import de.lukegoll.personalverzeichnis.domain.entities.Document;
import de.lukegoll.personalverzeichnis.domain.entities.Employee;
import de.lukegoll.personalverzeichnis.domain.entities.Employment;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DocumentServiceImplTest {

    private Document document;
    private DocumentServiceImpl documentService;
    private EmployeeServiceImpl employeeService;

    @Autowired
    public DocumentServiceImplTest(DocumentServiceImpl documentService, EmployeeServiceImpl employeeService) {
        this.documentService = documentService;
        this.employeeService = employeeService;
    }

    @PostConstruct
    public void beforeAll() throws IOException {
        byte[] docBytes = Files.readAllBytes(new File("src/test/resources/Test.pdf").toPath());
        document = new Document();
        document.setFileName("test.pdf");
        document.setFileEnding(".pdf");
        document.setBlobData(docBytes);
        document.setSize(Long.valueOf(String.valueOf(docBytes.length)));
    }

    @Test
    @Order(1)
    void save() {
        Employee employee = employeeService.save(new Employee());
        document.setEmployee(employee);
        documentService.save(document);
        assertTrue(documentService.findAll().size() > 0);
    }

    @Test
    @Order(2)
    void findAll() {
        assertEquals(1, documentService.findAll().size());
    }

    @Test
    @Order(3)
    void findPaged() {
        Page<Document> page = documentService.findPaged(Pageable.ofSize(1));
        assertEquals(1, page.getTotalElements());
        assertEquals(1, page.getNumberOfElements());
    }

    @Test
    @Order(4)
    void deleteAll() {
        documentService.deleteAll();
        assertTrue(documentService.findAll().size() == 0);
    }
}