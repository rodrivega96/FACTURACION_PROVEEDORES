package com.vates.eng.ha.persistence.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = { "permission" })
@NoArgsConstructor
@Getter
@Setter
public class PermissionRepresentation extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 1997028958851859783L;

    @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Permission permission;

    @Size(max = 255)
    @Column(name = "REPRESENTATION")
    private String representation;

}
