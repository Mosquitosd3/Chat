package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.enity.Message;
import ru.job4j.chat.service.MessageService;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageControl {
    private final MessageService service;

    public MessageControl(MessageService service) {
        this.service = service;
    }

    @GetMapping("/")
    public List<Message> allMessage() {
        return service.showAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> messageById(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id cannot be negative or equally 0");
        }
        var message = service.showById(id);
        return new ResponseEntity<Message>(
                message.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Message not found"
                )),
                message.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        if (message.getMessage().isEmpty()) {
            throw new NullPointerException("Еhe message cannot be empty");
        }
        return new ResponseEntity<Message>(
                this.service.save(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        if (message.getMessage().isEmpty()) {
            throw new NullPointerException("Еhe message cannot be empty");
        }
        this.service.save(message);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/")
    public Message patch(@RequestBody Message message)
            throws InvocationTargetException, IllegalAccessException {
        var current = service.showById(message.getId())
                .orElseThrow(
                        () -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "massage not found"
                        )
                );
        return service.patch(current, message);
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
