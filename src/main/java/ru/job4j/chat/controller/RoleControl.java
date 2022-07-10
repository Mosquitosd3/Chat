package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.enity.Role;
import ru.job4j.chat.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleControl {
    private final RoleService service;

    public RoleControl(RoleService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Role> allMessage() {
        return service.showALl();
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
    public ResponseEntity<Role> create(@RequestBody Role role) {
        if (role.getName().isEmpty()) {
            throw new NullPointerException("role mustn't be empty");
        }
        return new ResponseEntity<Role>(
                this.service.save(role),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Role role) {
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
