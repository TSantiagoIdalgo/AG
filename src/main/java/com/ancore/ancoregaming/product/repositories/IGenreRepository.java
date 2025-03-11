package com.ancore.ancoregaming.product.repositories;

import com.ancore.ancoregaming.product.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IGenreRepository extends JpaRepository<Genre, String> {
    @Query("SELECT g FROM Genre g WHERE LOWER(g.name) = LOWER(:genreName) ")
    Optional<Genre> findGenreByName(String genreName);
}
