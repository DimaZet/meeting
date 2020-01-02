package ru.party.meeting.model;

import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
@Data
class RelationDatabaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CreatedDate
    @Column(name = "createdAt")
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt")
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;
}