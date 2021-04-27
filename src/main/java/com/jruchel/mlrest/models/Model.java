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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    private String predictedAttribute;
    private double lastTrainedAccuracy;
    private String algorithm;
    @Lob
    private String savedModel;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private User owner;

    @PreRemove
    protected void preRemove() {
        owner.getModels().remove(this);
        this.owner = null;
    }
}
