package ru.job4j.chat.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.chat.enity.Person;
import ru.job4j.chat.repository.PersonRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
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

    public Person patch(Person current, Person person)
            throws InvocationTargetException, IllegalAccessException {
        var methods = person.getClass().getDeclaredMethods();
        var namePerMethod = new HashMap<String, Method>();
        for (var method: methods) {
            var name = method.getName();
            if (name.startsWith("get") || name.startsWith("set")) {
                namePerMethod.put(name, method);
            }
        }
        for (var name : namePerMethod.keySet()) {
            if (name.startsWith("get")) {
                var getMethod = namePerMethod.get(name);
                var setMethod = namePerMethod.get(name.replace("get", "set"));
                if (setMethod == null) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Impossible invoke set method from object : "
                                    + person
                                    + ", Check set and get pairs.");
                }
                var newValue = getMethod.invoke(person);
                if (newValue != null) {
                    setMethod.invoke(current, newValue);
                }
            }
        }
        return personRepo.save(current);
    }

    public List<Person> showAll() {
        List<Person> persons = new ArrayList<>();
        personRepo.findAll().forEach(persons::add);
        return persons;
    }

    public Person findByName(String username) {
        return personRepo.findByUsername(username);
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
