package ru.job4j.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.enity.Role;
import ru.job4j.chat.repository.RoleRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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

    public Role patch(Role current, Role role)
            throws InvocationTargetException, IllegalAccessException {
        var methods = role.getClass().getDeclaredMethods();
        var nameRoleMethod = new HashMap<String, Method>();
        for (var method : methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                nameRoleMethod.put(name, method);
            }
        }
        for (var name : nameRoleMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = nameRoleMethod.get(name);
                var setMethod = nameRoleMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(
                      HttpStatus.NOT_FOUND,
                            "Impossible invoke set method from object : "
                                    + role
                                    + ", Check set and get pairs."
                    );
                }
                var newValue = getMethod.invoke(role);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        return roleRepo.save(current);
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
