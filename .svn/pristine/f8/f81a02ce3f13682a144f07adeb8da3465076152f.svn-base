package com.vates.facpro.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
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
@Table(name = "PERMISSION_REPRESENTATIONS")
@XmlRootElement(name = "permission_representation")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
@NoArgsConstructor
@Getter
@Setter
public class PermissionRepresentation extends AbstractBaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4795796565681493972L;

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator= "PERREP_SEQ")
    @SequenceGenerator(name="PERREP_SEQ",sequenceName="PERREP_SEQ",allocationSize=1)
    private Long id;

    @Size(max = 255)
    @Column(name = "REPRESENTATION")
    private String representation;
    
    @Size(max = 255)
    @Column(name = "REPRESENTATIONPAGE")
    private String representationPage;
    
    @Column(name = "ORD")
    private Long ord;

}
