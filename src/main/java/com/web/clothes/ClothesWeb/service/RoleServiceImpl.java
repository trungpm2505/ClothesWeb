package com.web.clothes.ClothesWeb.service;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import com.web.clothes.ClothesWeb.entity.Role;
import com.web.clothes.ClothesWeb.repository.RoleRespository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{
	private final RoleRespository roleRespository;
	
	@Override
    public Optional<Role> getRoleByName(String roleName) {
        Optional<Role> role = roleRespository.findRoleByRoleName(roleName);
        return role;
    }

	@Override
	public List<Role> getAll() {
		List<Role> roles = roleRespository.findAll();
		return roles;
	}

	@Override
	public Optional<Role> getRole(Integer roleId) {
		Optional<Role> role = roleRespository.getRoleById(roleId);
		return role;
	}

}
