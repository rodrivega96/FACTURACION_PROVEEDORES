package com.vates.eng.ha.domain.impl;

import static com.google.common.base.Strings.isNullOrEmpty;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.vates.eng.ha.domain.UserToken;

/**
 * @author Gaston Napoli
 * 
 */
@XmlRootElement(name = "basicUserToken")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Getter
@Setter
public class BasicUserToken implements UserToken {

    private String loginName;

    private String password;

    /**
     * @param loginName
     * @param password
     */
    public BasicUserToken(String loginName, String password) {

        this.loginName = loginName;

        this.password = password;

    }

    /**
     * @return
     */
    public boolean isValid() {

        return !isNullOrEmpty(loginName) && !isNullOrEmpty(password);

    }

}
