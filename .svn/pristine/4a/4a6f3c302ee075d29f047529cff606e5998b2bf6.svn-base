package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * Entity representing the different roles in the system.
 * 
 * @author Gaston Napoli
 * 
 */
@Entity
@Table(name = "ROLES")
@XmlRootElement(name = "role")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = {
		"users", "permissions" })
@NoArgsConstructor
@Getter
@Setter
public class Role extends AbstractBaseEntity implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 595388574123319664L;
	public static final long ADMIN_ID = 1L;
	public static final long STUDENT_ID = 2L;
	public static final long USER_FINAL = 4L;
	public static final long PAGO_PROVEEDOR = 5L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ")
	@SequenceGenerator(name = "ROLE_SEQ", sequenceName = "ROLE_SEQ", allocationSize = 1)
	private Long id;

	@NotNull
	@Size(min = 1, max = 25)
	@Column(name = "NAME")
	private String name;

	@Size(max = 255)
	@Column(name = "DESCRIPTION")
	private String description;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
	private Set<User> users;

	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
	private Set<Permission> permissions;

}
