package com.vates.eng.ha.persistence.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Gaston Napoli
 *
 */
@Entity
@Table(name = "ROLES")
@XmlRootElement(name = "role")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = { "users", "permissions" })
@NoArgsConstructor
@Getter
@Setter
public class Role extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = -6452953726164787347L;

    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "NAME")
    private String name;

    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USERS_ROLES", 
        joinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") }, 
        inverseJoinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "ID") })
    private Set<User> users;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    private Set<Permission> permissions;

}
