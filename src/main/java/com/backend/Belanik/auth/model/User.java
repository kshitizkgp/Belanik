package com.backend.Belanik.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 65981149772133526L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private Long id;

	@Column(name = "PROVIDER_USER_ID")
	private String providerUserId;

	private String email;

	private String profilePictureUrl;

	@Column(name = "enabled", columnDefinition = "BIT", length = 1)
	private boolean enabled;

	@Column(name = "DISPLAY_NAME")
	private String displayName;

	@Column(name = "created_timestamp", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createdTimestamp;

	@Temporal(TemporalType.TIMESTAMP)
	protected Date modifiedTimestamp;

	private String password;

	private String provider;

	private String contactNumber;

	private String bio;

	private Date dateOfBirth;

	private String gender;

	private String likedPosts;

	// bi-directional many-to-many association to Role
	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "user_role", joinColumns = { @JoinColumn(name = "USER_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	private Set<Role> roles;
}