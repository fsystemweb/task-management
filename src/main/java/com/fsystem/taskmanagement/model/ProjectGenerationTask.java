package com.fsystem.taskmanagement.model;

import com.fsystem.taskmanagement.model.validation.CountTaskValidation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@CountTaskValidation
public class ProjectGenerationTask {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private Date creationDate;

    private EnumTypeTask type;

    private int start;

    private int end;

    private EnumStatusTask status;

    @JsonIgnore
    private String storageLocation;

}
