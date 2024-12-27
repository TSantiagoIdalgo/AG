package com.ancore.ancoregaming.product.repositories;

import com.ancore.ancoregaming.product.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGenreRepository extends JpaRepository<Genre, String> {

}
