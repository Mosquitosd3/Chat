package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.enity.Operation;
import ru.job4j.chat.enity.Role;
import ru.job4j.chat.service.RoleService;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
@RequestMapping("/role")
public class RoleControl {
    private final RoleService service;

    public RoleControl(RoleService service) {
        this.service = service;
    }

    @GetMapping("/")
    public ResponseEntity<List<Role>> allMessage() {
        var body = service.showALl();
        var entity = ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);
        return entity;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> messageById(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id cannot be negative or equally 0");
        }
        var message = service.showById(id);
        return new ResponseEntity<Role>(
                message.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Role not found"
                )),
                message.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Role> create(@Valid @RequestBody Role role) {
        if (role.getName().isEmpty()) {
            throw new NullPointerException("role mustn't be empty");
        }
        return new ResponseEntity<Role>(
                this.service.save(role),
                HttpStatus.CREATED
        );
    }

    @PatchMapping("/")
    @Validated(Operation.OnUpdate.class)
    public Role patch(@Valid @RequestBody Role role)
            throws InvocationTargetException, IllegalAccessException {
        var current = service.showById(role.getId())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Role not found"
                        )
                );
        return service.patch(current, role);
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Role role) {
        if (role.getName().isEmpty()) {
            throw new NullPointerException("role mustn't be empty");
        }
        this.service.save(role);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id cannot be negative or equally 0");
        }
        this.service.delete(id);
        return ResponseEntity.ok().build();
    }
}
