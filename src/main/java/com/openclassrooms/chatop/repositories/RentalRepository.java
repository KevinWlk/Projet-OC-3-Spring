package com.openclassrooms.chatop.repositories;

import com.openclassrooms.chatop.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
    Optional<Rental> findByName(String name);
}