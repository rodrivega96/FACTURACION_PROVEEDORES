package com.vates.facpro.persistence.domain;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * It defines an base entity with basic information. The basic data encompasses a name, a description and the data given by AbstractBaseEntity.
 * 
 * @author 
 * 
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true, exclude = { "deleted" , "description","name"})
@Getter
@Setter
public class AbstractBasicInfoEntity extends AbstractBaseEntity {

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "NAME")
    private String name;

    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;
    
    @NotNull
    @Column(name="DELETED")
    private boolean deleted;
    
}
