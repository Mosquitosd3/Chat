package ru.job4j.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.enity.Message;
import ru.job4j.chat.repository.MessageRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepo;

    public MessageService(MessageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    public Message save(Message message) {
        return messageRepo.save(message);
    }

    public Message patch(Message current, Message message)
            throws InvocationTargetException, IllegalAccessException {
        var methods = message.getClass().getDeclaredMethods();
        var nameMassageMethod = new HashMap<String, Method>();
        for (var method : methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                nameMassageMethod.put(name, method);
            }
        }
        for (var name : nameMassageMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = nameMassageMethod.get(name);
                var setMethod = nameMassageMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Impossible invoke set method from object : "
                                    + message
                                    + ", Check set and get pairs."
                    );
                }
                var newValue = getMethod.invoke(message);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        return messageRepo.save(current);
    }

    public List<Message> showAll() {
        List<Message> messages = new ArrayList<>();
        messageRepo.findAll().forEach(messages::add);
        return messages;
    }

    public Optional<Message> showById(int id) {
        return messageRepo.findById(id);
    }

    public void delete(int id) {
        Message message = new Message();
        message.setId(id);
        messageRepo.delete(message);
    }
}
