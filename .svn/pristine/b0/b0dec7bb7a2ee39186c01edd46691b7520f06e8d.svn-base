package com.vates.eng.ha.persistence.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
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
@Table(name = "RESOURCES", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@XmlRootElement(name = "resource")
@NamedQueries({
        @NamedQuery(name = "Resource.findAll", query = "SELECT r FROM Resource r"),
        @NamedQuery(name = "Resource.findById", query = "SELECT r FROM Resource r WHERE r.id = :id"),
        @NamedQuery(name = "Resource.findByName", query = "SELECT r FROM Resource r WHERE r.name = :name"),
        @NamedQuery(name = "Resource.findByDescription", query = "SELECT r FROM Resource r WHERE r.description = :description"),
        @NamedQuery(name = "Resource.findByVersion", query = "SELECT r FROM Resource r WHERE r.version = :version") })
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = { "permissions" })
@NoArgsConstructor
@Getter
@Setter
public class Resource extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 2548638095396834833L;

    @NotNull
    @Size(min = 3, max = 25)
    @Column(name = "NAME")
    private String name;

    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "resource", fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    @NotNull
    @Size(min = 3, max = 25)
    @Column(name = "REPRESENTATION")
    private String representation;

}
