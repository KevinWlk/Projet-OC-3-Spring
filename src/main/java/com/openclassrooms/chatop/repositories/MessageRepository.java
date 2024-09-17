package com.openclassrooms.chatop.repositories;

import com.openclassrooms.chatop.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;



public interface MessageRepository extends JpaRepository<Message, Integer> {
}