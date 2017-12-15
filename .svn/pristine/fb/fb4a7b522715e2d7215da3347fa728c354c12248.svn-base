package com.vates.facpro.persistence.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

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
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Type;

/**
 * Entity representing a system's user.
 * 
 * @author 
 * 
 */
@Entity
@Table(name = "NIVELES_PUBLICADOS")
@XmlRootElement(name = "nivelPublicado")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = false, exclude = {"factura", "niveles"})
@NoArgsConstructor
@Getter
@Setter
public class NivelPublicado extends AbstractBaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8336847398666318390L;

	
	
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "NIVEL_PUBLICADO_SEQ")
    @SequenceGenerator(name="NIVEL_PUBLICADO_SEQ",sequenceName="NIVEL_PUBLICADO_SEQ",allocationSize=1)
    private Long id;
        
    @Column(name = "LAST")
    @Type(type = "yes_no")
    private Boolean last;
    
    @JoinColumn(name = "NIVEL_PUBLICADO_ID", referencedColumnName = "ID")
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Nivel> niveles;
    
    @JoinColumn(name = "FACTURA_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Factura factura;    
    
    @Column(name = "CREATED_USER")
    private Long createdUser;
   
}
