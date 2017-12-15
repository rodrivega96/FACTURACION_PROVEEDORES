package com.vates.facpro.security.dto;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vates.facpro.persistence.domain.Permission;
import com.vates.facpro.persistence.domain.PermissionRepresentation;
import com.vates.facpro.persistence.domain.Role;
import com.vates.facpro.persistence.domain.User;
import com.vates.eng.ha.util.impl.Utils;

/**
 * @author Gaston Napoli
 * 
 */
@Data
@Slf4j
public class UserDTO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1226527270870982687L;

    private Long id; 
    
    private String loginName;

    private Set<String> grants;
    
    private Set<String> grantsAll;
    
    private static final String ALL = "ALL";
    
    private static final String ALL_FAC = "_FACTURAS";

    /**
     * @param user
     * @return
     */
    public static UserDTO of(User user) {

        checkNotNull(user);

        UserDTO dto = new UserDTO();
        
        dto.setId(user.getId());

        dto.setLoginName(user.getMail());

        dto.setGrants(Sets.<String> newHashSet());

        List<String> grants = Lists.newArrayList();
        
        List<String> grantsAll = Lists.newArrayList();

        for (Role role : user.getRoles()) {

            for (Permission permission : role.getPermissions()) {

                String resourceRepresentation = permission.getResource().getRepresentation();

                for (PermissionRepresentation permissionRepresentation : permission.getRepresentations()) {
                    
                	if(ALL.equals(permissionRepresentation.getRepresentation())){
                		String grant = Utils.basicGrantFormat(permissionRepresentation.getRepresentation(), resourceRepresentation);
                		
                		log.debug(grant);
                		
                		grantsAll.add(grant);
                	} else {
                		String grant = Utils.basicGrantFormat(permissionRepresentation.getRepresentation(), resourceRepresentation);
                		
                		log.debug(grant);
                		
                		grants.add(grant);                		
                	}
                	

                }

            }

        }

        dto.setGrantsAll(ImmutableSet.copyOf(grantsAll));
        dto.setGrants(ImmutableSet.copyOf(grants));

        return dto;

    }

	public boolean isGrantAll() {
		for (String st : this.getGrantsAll()) {
			if ((UserDTO.ALL + ALL_FAC).toUpperCase().equals(st.toUpperCase())) {
				return true;
			}
		}
		return false;
	}

}
