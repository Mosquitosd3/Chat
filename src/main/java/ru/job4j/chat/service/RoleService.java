package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.enity.Role;
import ru.job4j.chat.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    private final RoleRepository roleRepo;

    public RoleService(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    public Role save(Role role) {
        return roleRepo.save(role);
    }

    public List<Role> showALl() {
        List<Role> roles = new ArrayList<>();
        roleRepo.findAll().forEach(roles::add);
        return roles;
    }

    public Optional<Role> showById(int id) {
        return roleRepo.findById(id);
    }

    public void delete(int id) {
        Role role = new Role();
        role.setId(id);
        roleRepo.delete(role);
    }
}
