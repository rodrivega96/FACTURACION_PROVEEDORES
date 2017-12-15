package com.vates.facpro.security.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.vates.facpro.persistence.comparators.PermissionRepresentationComparator;
import com.vates.facpro.persistence.comparators.ResourceComparator;
import com.vates.facpro.persistence.domain.Permission;
import com.vates.facpro.persistence.domain.PermissionRepresentation;
import com.vates.facpro.persistence.domain.Resource;
import com.vates.facpro.persistence.domain.Role;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.security.dto.MenuDTO;
import com.vates.facpro.security.dto.SubMenuDTO;

@Service
public class FacProMenuLoader {

    public List<MenuDTO> generateInitialMenu() {
        List<MenuDTO> menu = new ArrayList<MenuDTO>();
        MenuDTO main = new MenuDTO();
        SubMenuDTO sub = new SubMenuDTO();
        List<SubMenuDTO> opt = new ArrayList<SubMenuDTO>();
        sub.setTitle("Iniciar Session");
        sub.setPage("login-form");
        main.setTitle("Login");
        opt.add(sub);
        main.setSubOptions(opt);
        menu.add(main);
        return menu;
    }
    
    public List<MenuDTO> generateMenu(User user){
        List<MenuDTO> menu = new ArrayList<MenuDTO>();
        generateMain(menu, getPermisionsRep(user));
        return menu;
    }
    
    private Map<Resource, Set<PermissionRepresentation>> getPermisionsRep(User user) {
        Map<Resource, Set<PermissionRepresentation>> permissionsRep = new HashMap<Resource, Set<PermissionRepresentation>>();
        for (Role role : user.getRoles()) {
            for (Permission permission : role.getPermissions()) {
                Set<PermissionRepresentation> perReps = permission.getRepresentations();
                if (permission.getResource().getRepresentation() != null && perReps != null && !perReps.isEmpty()) {
                    for(PermissionRepresentation perRep:permission.getRepresentations()){
                        if(perRep.getRepresentationPage()!=null && !"".equals(perRep.getRepresentationPage())){
                            Set<PermissionRepresentation> mappedPermissionsRep = permissionsRep.get(permission.getResource());
                            if (mappedPermissionsRep == null) {
                                mappedPermissionsRep = new HashSet<PermissionRepresentation>();
                                permissionsRep.put(permission.getResource(), mappedPermissionsRep);
                            }
                            mappedPermissionsRep.add(perRep);
                            
                        }
                    }
                }
            }
        }
        return permissionsRep;
    }
    
    private void generateMain(List<MenuDTO> menu, Map<Resource,Set<PermissionRepresentation>> permissionsRep){
        MenuDTO mainMenu = null;
        List<Resource> orderedResources = new ArrayList<Resource>(permissionsRep.keySet());
        Collections.sort(orderedResources, new ResourceComparator());
        for(Resource resource:orderedResources){
            mainMenu = new MenuDTO();
            mainMenu.setTitle(resource.getRepresentation());
            mainMenu.setSubOptions(new ArrayList<SubMenuDTO>());
            generateSub(mainMenu,permissionsRep.get(resource));
            menu.add(mainMenu);
        }
        
    }
    
    private void generateSub(MenuDTO main, Set<PermissionRepresentation> permissionsRep) {
        List<PermissionRepresentation> orderedPeRep = new ArrayList<PermissionRepresentation>(permissionsRep);
        Collections.sort(orderedPeRep, new PermissionRepresentationComparator());
        for (PermissionRepresentation perRep : orderedPeRep) {
            if (perRep.getRepresentationPage() != null) {
                SubMenuDTO subMenu = new SubMenuDTO();
                subMenu.setPage(perRep.getRepresentationPage());
                subMenu.setTitle(perRep.getRepresentation());
                main.getSubOptions().add(subMenu);
            }
        }
    }
}
