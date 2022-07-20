package ru.job4j.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.enity.Room;
import ru.job4j.chat.repository.RoomRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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

    public Room patch(Room current, Room room)
            throws InvocationTargetException, IllegalAccessException {
        var methods = room.getClass().getDeclaredMethods();
        var nameRoomMethod = new HashMap<String, Method>();
        for (var method : methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                nameRoomMethod.put(name, method);
            }
        }
        for (var name : nameRoomMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = nameRoomMethod.get(name);
                var setMethod = nameRoomMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Impossible invoke set method from object : "
                                    + room
                                    + ", Check set and get pairs."
                    );
                }
                var newValue = getMethod.invoke(room);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        return roomRepo.save(current);
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
