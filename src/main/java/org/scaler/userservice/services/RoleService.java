package org.scaler.userservice.services;

import org.scaler.userservice.Repository.RoleRepository;
import org.scaler.userservice.models.Role;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(String name) {
        Role role = new Role();
//        role.setRole(name);

        return roleRepository.save(role);
    }

}
