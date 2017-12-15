package com.vates.facpro.security.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.vates.facpro.persistence.comparators.ModuleComparator;
import com.vates.facpro.persistence.comparators.PermissionRepresentationComparator;
import com.vates.facpro.persistence.comparators.ResourceComparator;
import com.vates.facpro.persistence.domain.Module;
import com.vates.facpro.persistence.domain.Permission;
import com.vates.facpro.persistence.domain.PermissionRepresentation;
import com.vates.facpro.persistence.domain.Resource;
import com.vates.facpro.persistence.domain.Role;
import com.vates.facpro.persistence.domain.User;
import com.vates.facpro.security.dto.MenuDTO;
import com.vates.facpro.security.dto.ModuloMenuDTO;
import com.vates.facpro.security.dto.SubMenuDTO;

@Service
public class FacProMenuLoader {

	public List<MenuDTO> generateInitialMenu() {
		List<MenuDTO> menu = new ArrayList<MenuDTO>();
		MenuDTO main = new MenuDTO();
		List<ModuloMenuDTO> mods = new ArrayList<ModuloMenuDTO>();
		ModuloMenuDTO mod = new ModuloMenuDTO();
		mod.setTitle("Login");
		mods.add(mod);
		SubMenuDTO sub = new SubMenuDTO();
		List<SubMenuDTO> opt = new ArrayList<SubMenuDTO>();
		sub.setTitle("Iniciar Session");
		sub.setPage("login-form");
		main.setTitle("Login");
		opt.add(sub);
		mod.setSubOptions(opt);
		main.setSubOptions(mods);
		menu.add(main);
		return menu;
	}
    
    public List<MenuDTO> generateMenu(User user){
        List<MenuDTO> menu = new ArrayList<MenuDTO>();
        generateMain(menu, getPermisionsRep(user));
        return menu;
    }
    
    private Map<Module,  Map<Resource, Set<PermissionRepresentation>>> getPermisionsRep(User user) {
        Map<Module,  Map<Resource, Set<PermissionRepresentation>>> modulos = new HashMap<Module,  Map<Resource, Set<PermissionRepresentation>>>();
        for (Role role : user.getRoles()) {
            for (Permission permission : role.getPermissions()) {
                Set<PermissionRepresentation> perReps = permission.getRepresentations();
                if (permission.getResource().getRepresentation() != null && perReps != null && !perReps.isEmpty()) {
                    for(PermissionRepresentation perRep:permission.getRepresentations()){
                        if(perRep.getRepresentationPage()!=null && !"".equals(perRep.getRepresentationPage())){
                            for(Module mod: permission.getResource().getModules()){
                            	Map<Resource, Set<PermissionRepresentation>> permissionsRep = modulos.get(mod);
                            	if(permissionsRep==null){
                            		permissionsRep = new HashMap<Resource, Set<PermissionRepresentation>>();
                            		modulos.put(mod, permissionsRep);
                            	}
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
        }
        return modulos;
    }
    
    private void generateMain(List<MenuDTO> menu, Map<Module,  Map<Resource, Set<PermissionRepresentation>>> modules){
    	MenuDTO mainMenu = null;
        List<Module> orderedModules = new ArrayList<Module>(modules.keySet());
        Collections.sort(orderedModules, new ModuleComparator()); 
        for(Module mod: orderedModules){
        	mainMenu = new MenuDTO();
            mainMenu.setTitle(mod.getLargeName());
            mainMenu.setSubOptions(new ArrayList<ModuloMenuDTO>());
            menu.add(mainMenu);
            generateMod(mainMenu,modules.get(mod));
        	
        }    
    }
    
    private void generateMod(MenuDTO main, Map<Resource, Set<PermissionRepresentation>> resPer) {
        List<Resource> orderedResources = new ArrayList<Resource>(resPer.keySet());
        Collections.sort(orderedResources, new ResourceComparator());
        for(Resource resource:orderedResources){
        	ModuloMenuDTO modMen = new ModuloMenuDTO();
        	modMen.setTitle(resource.getRepresentation());
        	modMen.setSubOptions(new ArrayList<SubMenuDTO>());
        	main.getSubOptions().add(modMen);
        	generateSub(modMen,resPer.get(resource));
            
        }
    }
    
    private void generateSub(ModuloMenuDTO main, Set<PermissionRepresentation> permissionsRep) {
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
