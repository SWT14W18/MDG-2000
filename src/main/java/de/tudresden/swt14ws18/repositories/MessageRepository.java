package de.tudresden.swt14ws18.repositories;

import org.springframework.data.repository.CrudRepository;

import de.tudresden.swt14ws18.useraccountmanager.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
    public Message findById(long id);
}
