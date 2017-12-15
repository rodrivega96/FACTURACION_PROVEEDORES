package com.vates.eng.ha.dto;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vates.eng.ha.persistence.domain.Permission;
import com.vates.eng.ha.persistence.domain.PermissionRepresentation;
import com.vates.eng.ha.persistence.domain.Role;
import com.vates.eng.ha.persistence.domain.User;

/**
 * @author Gaston Napoli
 * 
 */
@Data
@Slf4j
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -4012396734987509234L;

    private String loginName;
    
    private String realmName;
    
    private Set<String> grants;

    /**
     * @param user
     * @return
     */
    public static UserDTO of(User user) {

        checkNotNull(user);

        UserDTO dto = new UserDTO();
        
        dto.setLoginName(user.getLogin());
        
        dto.setRealmName(user.getRealm().getName());
        
        dto.setGrants(Sets.<String> newHashSet());

        List<String> grants = Lists.newArrayList();

        for (Role role : user.getRoles()) {
            
            for (Permission permission : role.getPermissions()) {
                
                String resourceRepresentation = permission.getResource().getRepresentation();

                for (PermissionRepresentation permissionRepresentation : permission.getRepresentations()) {

                    String grant = MessageFormat.format("{0}_{1}", permissionRepresentation.getRepresentation(), resourceRepresentation);
                    
                    log.debug(grant);
                    
                    grants.add(grant);

                }
                
            }
            
        }

        dto.setGrants(ImmutableSet.copyOf(grants));

        return dto;

    }

}
