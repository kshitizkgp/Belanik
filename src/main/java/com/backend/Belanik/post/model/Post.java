package com.backend.Belanik.post.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/** The persistent class for the database table: Post **/
@Entity
@NoArgsConstructor
@Getter
@Setter
public class Post {
    @Id
    @Column
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String postId;

    @Column(nullable = false)
    private String authorId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTimestamp;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedTimestamp;

    @Column
    private String categories;

    @Column
    private String customCategories;

    @Column
    private String media;

    @Column
    private String description;

    @Column
    private String likes;
}