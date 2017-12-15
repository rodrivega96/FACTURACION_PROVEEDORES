package com.vates.eng.ha.persistence.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "USERS", uniqueConstraints = @UniqueConstraint(columnNames = { "LOGIN", "REALM_ID" }))
@XmlRootElement(name = "user")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true, exclude = { "roles", "realm" })
@NoArgsConstructor
@Getter
@Setter
public class User extends AbstractBaseEntity implements Serializable {

    private static final long serialVersionUID = 3269025252740491947L;

    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "FIRST_NAME")
    private String firstName;

    @Size(max = 25)
    @Column(name = "MIDDLE_NAME")
    private String middleName;

    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "LAST_NAME")
    private String lastName;

    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "LOGIN")
    private String login;

    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "PASSWORD")
    private String password;

    @NotNull
    @Size(min = 1, max = 35)
    @Column(name = "MAIN_EMAIL")
    private String mainEmail;

    @Size(max = 35)
    @Column(name = "SECONDARY_EMAIL")
    private String secondaryEmail;

    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Size(max = 6)
    @Column(name = "PHONE_EXTENSION")
    private String phoneExtension;

    @Size(max = 25)
    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;

    @NotNull
    @Column(name = "ACTIVE")
    private boolean active;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private List<Role> roles;

    @JoinColumn(name = "REALM_ID", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Realm realm;

}
