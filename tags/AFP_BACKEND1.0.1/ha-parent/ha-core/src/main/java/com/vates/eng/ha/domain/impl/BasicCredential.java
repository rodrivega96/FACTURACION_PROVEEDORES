package com.vates.eng.ha.domain.impl;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.vates.eng.ha.domain.Credential;

/**
 * Basic implementation of Credential. It just includes the 'sessionId' property, which is the ID of the session associated with the authenticated
 * user.
 * 
 * @author Gaston Napoli
 * 
 */
@XmlRootElement(name = "basicCredential")
@ToString(callSuper = true, doNotUseGetters = true)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@Getter
@Setter
public class BasicCredential implements Credential {

    private String sessionId;

}
