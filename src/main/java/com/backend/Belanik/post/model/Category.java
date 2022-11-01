package com.backend.Belanik.post.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/** The persistent class for the database table: Category **/
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Category {
    @Id
    @Column
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String categoryId;

    @Column(nullable = false)
    private String name;

    @Column
    private String parent;
}
