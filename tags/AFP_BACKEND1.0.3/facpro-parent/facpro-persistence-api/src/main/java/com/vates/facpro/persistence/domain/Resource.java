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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
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

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Gaston Napoli
 *
 */
@Entity
@Table(name = "RESOURCES", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@XmlRootElement(name = "resource")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = { "permissions","modules" })
@NoArgsConstructor
@Getter
@Setter
public class Resource extends AbstractBaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -3379754155283651794L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "RESOURCE_SEQ")
    @SequenceGenerator(name="RESOURCE_SEQ",sequenceName="RESOURCE_SEQ",allocationSize=1)
    private Long id;
    
    @NotNull
    @Size(min = 3, max = 120)
    @Column(name = "NAME")
    private String name;

    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;

    @OneToMany(mappedBy = "resource", fetch = FetchType.EAGER)
    private Set<Permission> permissions;

    @Size(min = 3, max = 120)
    @Column(name = "REPRESENTATION")
    private String representation;
    

    @Column(name = "ORD")
    private Long ord;
    
	@JsonIgnore
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "resources")
	private Set<Module> modules;
    
}
