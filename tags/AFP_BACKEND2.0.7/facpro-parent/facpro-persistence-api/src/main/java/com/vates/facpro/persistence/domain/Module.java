package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.Set;

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

/**
 * @author Cabrera Manuel
 *
 */
@Entity
@Table(name = "MODULE")
@XmlRootElement(name = "module")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = {
		"resources" })
@NoArgsConstructor
@Getter
@Setter
public class Module extends AbstractBaseEntity implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1541638270930022114L;

	@Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "MODULE_SEQ")
    @SequenceGenerator(name="MODULE_SEQ",sequenceName="MODULE_SEQ",allocationSize=1)
    private Long id;
    
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    
    @NotNull
    @Size(min = 3, max = 250)
    @Column(name = "LARGE_NAME")
    private String largeName;  
    
    @Column(name = "ORD")
    private Long ord;
	
    @JoinTable(name = "MODULE_RESOURCE", 
    joinColumns = { @JoinColumn(name = "MODULE_ID", referencedColumnName = "ID") }, 
    inverseJoinColumns = { @JoinColumn(name = "RESOURCE_ID", referencedColumnName = "ID") })
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Resource> resources;
    
}
