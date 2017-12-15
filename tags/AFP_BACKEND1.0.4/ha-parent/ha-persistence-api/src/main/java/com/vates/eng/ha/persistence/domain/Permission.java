package com.vates.eng.ha.persistence.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
 * 
 * @author Gaston Napoli
 *
 */
@Entity
@Table(name = "PERMISSIONS")
@XmlRootElement(name = "permission")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = { "roles", "resource", "representations" })
@NoArgsConstructor
@Getter
@Setter
public class Permission extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 8106348959936554428L;

    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "NAME")
    private String name;

    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "APP_PERMISSION")
    private Boolean appPermission;

    @JoinTable(name = "ROLES_PERMISSIONS", 
            joinColumns = { @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ID") }, 
            inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @JoinColumn(name = "RESOURCE_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Resource resource;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "permission")
    private Set<PermissionRepresentation> representations;

}
