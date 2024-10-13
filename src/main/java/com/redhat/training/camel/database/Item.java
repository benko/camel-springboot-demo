package com.redhat.training.camel.database;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "randomstuff")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "randomstuff_id_seq")
    @SequenceGenerator(name = "randomstuff_id_seq",
                    sequenceName = "randomstuff_id_seq",
                    allocationSize = 1)
    private int id;

    @Column(name = "item")
    private String name;

    public Item() {
        // NOP
    }

    public Item(String name) {
        this.setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "Item:[id=" + id + ",name=" + name + "]";
    }
}
