package com.vates.facpro.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Type;

/**
 * Entity representing a system's user.
 * 
 * @author Gaston Napoli
 * 
 */
@Entity
@Table(name = "NIVELES")
@XmlRootElement(name = "nivel")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = {"factura"})
@NoArgsConstructor
@Getter
@Setter
public class Nivel extends AbstractBaseEntity implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -5159579078512531778L;
	
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "NIVEL_SEQ")
    @SequenceGenerator(name="NIVEL_SEQ",sequenceName="NIVEL_SEQ",allocationSize=1)
    private Long id;
        
    @JsonIgnore
    @JoinColumn(name = "FACTURA_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Factura factura;
    
//    @ManyToOne(fetch = FetchType.EAGER)
//    private User usuario;
    
    @Column(name = "USUARIO_ID")
    private Long usuarioId;
	    
    @Column(name = "ORDEN")
    private Integer orden;
    
    @Column(name = "ESTADO")
    private Integer estado;
    
    @Column(name = "PADRE")    
    private Long padre;
    
    @Column(name = "PUBLICADO")
    @Type(type = "yes_no")
    private Boolean publicado;
    
    @Column(name = "ELIMINADO")
    @Type(type = "yes_no")
    private Boolean eliminado;
}
