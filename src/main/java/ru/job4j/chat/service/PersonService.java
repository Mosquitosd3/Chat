package ru.job4j.chat.service;

import org.springframework.stereotype.Service;
import ru.job4j.chat.enity.Person;
import ru.job4j.chat.repository.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    private final PersonRepository personRepo;

    public PersonService(PersonRepository personRepo) {
        this.personRepo = personRepo;
    }

    public Person save(Person person) {
        return personRepo.save(person);
    }

    public List<Person> showAll() {
        List<Person> persons = new ArrayList<>();
        personRepo.findAll().forEach(persons::add);
        return persons;
    }

    public Optional<Person> showById(int id) {
        return personRepo.findById(id);
    }

    public void delete(int id) {
        Person person = new Person();
        person.setId(id);
        personRepo.delete(person);
    }
}
