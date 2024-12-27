package com.ancore.ancoregaming.product.services.genre;

import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.repositories.IGenreRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreService implements IGenreService {

  private final IGenreRepository genreRepository;
  
  @Autowired
  public GenreService(IGenreRepository genreRepository) {
    this.genreRepository = genreRepository;
  }
  
  @Override
  public Genre createGenre(String genreName) {
    Optional<Genre> genreFound = this.genreRepository.findById(genreName);
    if (genreFound.isPresent()) {
      return genreFound.get();
    }
    Genre genre = new Genre(genreName);
    this.genreRepository.save(genre);
    return genre;
  }

  @Override
  public List<Genre> bulkCreateGenres(List<Genre> genres) {
    return genres
            .stream()
            .map((genre) -> this.createGenre(genre.getName()))
            .collect(Collectors.toList());
  }

  @Override
  public List<Genre> findAllGenres() {
    return this.genreRepository.findAll();
  }

  @Override
  public Genre findGenre(String name) {
    return this.genreRepository.findById(name).orElseThrow(() -> new EntityNotFoundException("Genre not found"));
  }

  @Override
  public Genre destroyGenre(String name) {
    Genre genre = this.findGenre(name);
    this.genreRepository.delete(genre);
    return genre;
  }

  @Override
  public Genre updateGenre(String name) {
    Genre genre = this.findGenre(name);
    genre.setName(name != null ? name : genre.getName());
    this.genreRepository.save(genre);

    return genre;
  }

}
