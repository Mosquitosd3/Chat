package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.enity.Message;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
}
