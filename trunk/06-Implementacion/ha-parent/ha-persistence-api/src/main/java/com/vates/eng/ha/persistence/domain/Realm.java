package com.vates.eng.ha.persistence.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * It defines the entity for realms. Each user must belong to a realm.
 */
@Entity
@Table(name = "REALMS")
@XmlRootElement(name = "realm")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = { "users" })
@NoArgsConstructor
@Getter
@Setter
public class Realm extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = -7506516575970414145L;

    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "NAME")
    private String name;

    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "HANDLED_BY")
    private String handledBy;

    @Column(name = "CHANGING_PASSWORD_ENABLED")
    private Boolean changingPasswordEnabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "realm", fetch = FetchType.EAGER)
    private Set<User> users;

}
