package ru.job4j.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.enity.Operation;
import ru.job4j.chat.enity.Person;
import ru.job4j.chat.service.PersonService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonControl {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(PersonControl.class.getSimpleName());

    private final PersonService service;
    private final BCryptPasswordEncoder encoder;
    private final ObjectMapper objectMapper;

    public PersonControl(PersonService service,
                         BCryptPasswordEncoder encoder,
                         ObjectMapper objectMapper) {
        this.service = service;
        this.encoder = encoder;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public List<Person> allMessage() {
        return service.showAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> messageById(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id cannot be negative or equally 0");
        }
        var message = service.showById(id);
        return new ResponseEntity<Person>(
                message.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Person not found"
                )),
                message.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/sign-up")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Person> create(@Valid @RequestBody Person person) {
        if (person.getUsername().isEmpty() || person.getPassword().isEmpty()) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException(
                    "Invalid password. Password length must be more than 5 characters."
            );
        }
        person.setPassword(encoder.encode(person.getPassword()));
        return new ResponseEntity<Person>(
                this.service.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    @Validated(Operation.OnUpdate.class)
    public ResponseEntity<Void> update(@Valid @RequestBody Person person) {
        if (person.getUsername().isEmpty() || person.getPassword().isEmpty()) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException(
                    "Invalid password. Password length must be more than 5 characters."
            );
        }
        this.service.save(person);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    @Validated(Operation.OnUpdate.class)
    public Person patch(@Valid @RequestBody Person person)
            throws InvocationTargetException, IllegalAccessException {
        var current = service.showById(person.getId())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Person not found")
                );
        if (person.getPassword().length() < 6) {
            throw new IllegalArgumentException(
                    "Invalid password. Password length must be more than 5 characters."
            );
        }
        person.setPassword(encoder.encode(person.getPassword()));
        if (person.getUsername().isEmpty() || person.getPassword().isEmpty()) {
            throw new NullPointerException("Username and password mustn't be empty");
        }
       return service.patch(current, person);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id cannot be negative or equally 0");
        }
        this.service.delete(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public void exceptionHandler(Exception e, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() {{
            put("massage", e.getMessage());
            put("type", e.getClass());
        }}));
        LOGGER.error(e.getLocalizedMessage());
    }

}
