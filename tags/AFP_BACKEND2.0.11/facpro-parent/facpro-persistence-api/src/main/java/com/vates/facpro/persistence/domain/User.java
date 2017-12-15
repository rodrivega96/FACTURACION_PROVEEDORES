package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Entity representing a system's user.
 * 
 * @author Gaston Napoli
 * 
 */
@Entity
@Table(name = "USERS")
@XmlRootElement(name = "user")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true, exclude = { "roles" })
@NoArgsConstructor
@Getter
@Setter
public class User extends AbstractBaseEntity implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1903119016992784851L;

	public static Long LOCAL = 0L;
	public static Long DOMAIN_VATESSA = 1L;
	public static int INACTIVE = 0;
	public static int ACTIVE = 1;
	public static Long USUARIO_ADMIN = 3L;
	public static Long USUARIO_FINAL = 4L;
	
	public static Long ROOT_USER = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
	@SequenceGenerator(name = "USER_SEQ", sequenceName = "USER_SEQ", allocationSize = 1)
	private Long id;

	@Column(name = "ACTIVE")
	@NotNull
	private Integer active;

	@NotNull
	@Size(min = 1, max = 250)
	@Column(name = "NAME")
	private String name;

	@NotNull
	@Size(min = 1, max = 250)
	@Column(name = "LAST_NAME")
	private String lastName;

	@NotNull
	@Size(min = 1, max = 250)
	@Column(name = "MAIL")
	private String mail;

	@Size(min = 0, max = 250)
	@Column(name = "JOB")
	private String job;

	@NotNull
	@Size(min = 1, max = 250)
	@Column(name = "PASSWORD")
	private String password;

	@NotNull
	@Column(name = "TIPO")
	private Long tipo;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "USERS_ROLES", joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
	private Set<Role> roles;

}
