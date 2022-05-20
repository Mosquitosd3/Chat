package ru.job4j.chat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.chat.enity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
