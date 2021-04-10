package com.jruchel.mlrest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Model {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String predictedAttribute;
    private double lastTrainedAccuracy;
    @Lob
    private byte[] savedModel;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;
}
