package ru.job4j.chat.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.enity.Message;
import ru.job4j.chat.enity.Room;
import ru.job4j.chat.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/room")
public class RoomControl {
    private static final String API = "http://localhost:8080/message/";
    private static final String API_ID = "http://localhost:8080/message/{id}";
    private final RoomService service;
    private final RestTemplate rest;

    public RoomControl(RoomService service, RestTemplate rest) {
        this.service = service;
        this.rest = rest;
    }

    @GetMapping("/")
    public List<Room> allRoom() {
        return service.showAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> roomById(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id cannot be negative or equally 0");
        }
        var room = service.showByID(id);
        return new ResponseEntity<Room>(
                room.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Room not found"
                )),
                room.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<Message> addMessage(@PathVariable int id, @RequestBody Message message) {
        if (id <= 0) {
            throw new NullPointerException("Id cannot be negative or equally 0");
        }
        if (message.getMessage().isEmpty()) {
            throw new NullPointerException("Еhe message cannot be empty");
        }
        if (service.showByID(id).isPresent()) {
            Room room = service.showByID(id).get();
            room.addMessage(message);
            service.save(room);
            return new ResponseEntity<Message>(message, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<Message>(message, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Message message) {
        if (message.getMessage().isEmpty()) {
            throw new NullPointerException("Еhe message cannot be empty");
        }
        rest.put(API, message);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (id <= 0) {
            throw new NullPointerException("Id cannot be negative or equally 0");
        }
        rest.delete(API_ID, id);
        return ResponseEntity.ok().build();
    }
}
