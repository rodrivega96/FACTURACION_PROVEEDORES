package com.vates.eng.ha.domain.impl;

import static com.google.common.base.Strings.isNullOrEmpty;

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
@XmlRootElement(name = "realmUserToken")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Getter
@Setter
public class RealmUserToken extends BasicUserToken {

    private String realm;

    /**
     * @param loginName
     * @param password
     * @param realm
     */
    public RealmUserToken(String loginName, String password, String realm) {
       
        super(loginName, password);
       
        this.realm = realm;
   
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.vates.eng.ha.domain.impl.BasicUserToken#isValid()
     */
    @Override
    public boolean isValid() {

        return super.isValid() && !isNullOrEmpty(realm);

    }

}
