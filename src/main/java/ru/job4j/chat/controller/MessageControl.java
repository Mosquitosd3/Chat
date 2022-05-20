package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.chat.enity.Message;
import ru.job4j.chat.service.MessageService;

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
        var message = service.showById(id);
        return new ResponseEntity<Message>(
                message.orElse(new Message()),
                message.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Message> create(@RequestBody Message message) {
        return new ResponseEntity<Message>(
                this.service.save(message),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        this.service.save(message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        this.service.delete(id);
        return ResponseEntity.ok().build();
    }
}
