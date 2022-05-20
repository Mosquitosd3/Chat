package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.enity.Message;
import ru.job4j.chat.repository.MessageRepository;

import java.util.ArrayList;
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
