package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.enity.Person;
import ru.job4j.chat.service.PersonService;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonControl {
    private final PersonService service;

    public PersonControl(PersonService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Person> allMessage() {
        return service.showAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> messageById(@PathVariable int id) {
        var message = service.showById(id);
        return new ResponseEntity<Person>(
                message.orElse(new Person()),
                message.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        return new ResponseEntity<Person>(
                this.service.save(person),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        this.service.save(person);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        this.service.delete(id);
        return ResponseEntity.ok().build();
    }
}
