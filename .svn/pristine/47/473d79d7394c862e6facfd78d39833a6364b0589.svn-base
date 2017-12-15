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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

/**
 * 
 * @author Gaston Napoli
 *
 */
@Entity
@Table(name = "PERMISSIONS")
@XmlRootElement(name = "permission")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = { "representations" })
@NoArgsConstructor
@Getter
@Setter
public class Permission extends AbstractBaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8420727842962205413L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "PERMISSION_SEQ")
    @SequenceGenerator(name="PERMISSION_SEQ",sequenceName="PERMISSION_SEQ",allocationSize=1)
    private Long id;
    
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "NAME")
    private String name;

    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;

	@JoinColumn(name = "RESOURCE_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Resource resource;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ID")
    private Set<PermissionRepresentation> representations;
    
}
