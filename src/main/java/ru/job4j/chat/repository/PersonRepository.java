package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.enity.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, Integer> {
    Person findByUsername(String username);
}
