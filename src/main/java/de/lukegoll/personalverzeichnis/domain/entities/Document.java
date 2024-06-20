package de.lukegoll.personalverzeichnis.domain.entities;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "documents")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document extends AbstractEntity {


    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "size", nullable = false)
    private Long size;


    @Column(name = "file_ending", nullable = false)
    private String fileEnding;

    @Column(name = "document_data", nullable = false)
    @Lob
    private byte[] blobData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee", nullable = false)
    private Employee employee;
}
