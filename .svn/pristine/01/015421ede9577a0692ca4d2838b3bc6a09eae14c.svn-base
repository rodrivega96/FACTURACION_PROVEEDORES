package com.vates.facpro.security.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.vates.facpro.persistence.comparators.OptionMenuViewComparator;
import com.vates.facpro.persistence.domain.OptionMenuView;
import com.vates.facpro.persistence.domain.RoleView;
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
    
    
    private Map<OptionMenuView,  Map<OptionMenuView, Map<OptionMenuView, Set<OptionMenuView>>>> getPermisionsRep(List<RoleView> roles) {
        //Map<Modulo,  Map<Recurso, Map<Pagina, Set<representacion>>>>
    	Map<OptionMenuView,  Map<OptionMenuView, Map<OptionMenuView, Set<OptionMenuView>>>> modulos = new  HashMap<OptionMenuView,  Map<OptionMenuView, Map<OptionMenuView, Set<OptionMenuView>>>>();
        for(RoleView rv: roles){
        	OptionMenuView modOpt = new OptionMenuView();
        	modOpt.setName(rv.getModulo());
        	modOpt.setOrder(rv.getModOrd());
        	Map<OptionMenuView, Map<OptionMenuView, Set<OptionMenuView>>> res = modulos.get(modOpt);
        	if(res==null){
        		res = new HashMap<OptionMenuView, Map<OptionMenuView, Set<OptionMenuView>>>();
        		modulos.put(modOpt, res);
        	}
        	OptionMenuView resOpt = new OptionMenuView();
        	resOpt.setName(rv.getResourceRepresentation());
        	resOpt.setOrder(rv.getResOrd());
        	Map<OptionMenuView, Set<OptionMenuView>> perRepPage =  res.get(resOpt);
        	if(perRepPage == null){
        		perRepPage = new HashMap<OptionMenuView, Set<OptionMenuView>>();
        		res.put(resOpt, perRepPage);
        	}
        	OptionMenuView repOpt = new OptionMenuView();
        	repOpt.setName(rv.getPermissionRepresentationPage());
        	repOpt.setOrder(rv.getRepOrd());
        	Set<OptionMenuView> perRep = perRepPage.get(repOpt);
        	if(perRep == null){
        		perRep = new HashSet<OptionMenuView>();
        		perRepPage.put(repOpt, perRep);
        	}
        	OptionMenuView repSetOpt = new OptionMenuView();
        	repSetOpt.setName(rv.getPremisionReprensetation());
        	repSetOpt.setOrder(rv.getRepOrd());
        	perRep.add(repSetOpt);
        }
        return modulos;
    }
    
    
    
    public List<MenuDTO> generateMenuView(List<RoleView> roles){
        List<MenuDTO> menu = new ArrayList<MenuDTO>();
        generateMainView(menu, getPermisionsRep(roles));
        return menu;
    }
    
    private void generateMainView(List<MenuDTO> menu, Map<OptionMenuView,  Map<OptionMenuView, Map<OptionMenuView, Set<OptionMenuView>>>> modules){
    	MenuDTO mainMenu = null;
        for(OptionMenuView mod:  getOrderedMenuOptions(modules.keySet())){
        	mainMenu = new MenuDTO();
            mainMenu.setTitle(mod.getName());
            mainMenu.setSubOptions(new ArrayList<ModuloMenuDTO>());
            menu.add(mainMenu);
            generateModView(mainMenu,modules.get(mod));
        	
        }    
    }
    
    private void generateModView(MenuDTO main, Map<OptionMenuView, Map<OptionMenuView, Set<OptionMenuView>>> resPer) {
        for(OptionMenuView resource: getOrderedMenuOptions(resPer.keySet())){
        	ModuloMenuDTO modMen = new ModuloMenuDTO();
        	modMen.setTitle(resource.getName());
        	modMen.setSubOptions(new ArrayList<SubMenuDTO>());
        	main.getSubOptions().add(modMen);
        	generateSubView(modMen,resPer.get(resource));
            
        }
    }
    
    private void generateSubView(ModuloMenuDTO main, Map<OptionMenuView, Set<OptionMenuView>> permissionsRep) {
        for (OptionMenuView perRepPage : getOrderedMenuOptions(permissionsRep.keySet())) {
        	for(OptionMenuView perRep : getOrderedMenuOptions(permissionsRep.get(perRepPage))){
                if (perRep != null) {
                    SubMenuDTO subMenu = new SubMenuDTO();
                    subMenu.setPage(perRepPage.getName());
                    subMenu.setTitle(perRep.getName());
                    main.getSubOptions().add(subMenu);
                }
        		
        	}
        }
    }
    
	private List<OptionMenuView> getOrderedMenuOptions(
			Set<OptionMenuView> options) {
		List<OptionMenuView> lista = new ArrayList<OptionMenuView>(options);
		Collections.sort(lista, new OptionMenuViewComparator());
		return lista;
	}
    
}
