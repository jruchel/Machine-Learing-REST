package com.jruchel.mlrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @Lob
    @Type(type = "org.hibernate.type.BlobType")
    private byte[] savedModel;

    @JsonIgnore
    @JoinTable(name = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;
}
