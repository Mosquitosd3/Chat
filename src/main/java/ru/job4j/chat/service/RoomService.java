package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.enity.Room;
import ru.job4j.chat.repository.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepo;

    public RoomService(RoomRepository roomRepo) {
        this.roomRepo = roomRepo;
    }

    public Room save(Room room) {
        return roomRepo.save(room);
    }

    public List<Room> showAll() {
        List<Room> rooms = new ArrayList<>();
        roomRepo.findAll().forEach(rooms::add);
        return rooms;
    }

    public Optional<Room> showByID(int id) {
        return roomRepo.findById(id);
    }

    public void delete(int id) {
        Room room = new Room();
        room.setId(id);
        roomRepo.delete(room);
    }
}
