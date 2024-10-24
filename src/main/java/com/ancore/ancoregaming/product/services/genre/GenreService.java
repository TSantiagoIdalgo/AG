package com.ancore.ancoregaming.product.services.genre;

import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.repositories.IGenreRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenreService implements IGenreService {

  @Autowired
  private IGenreRepository genreRepository;

  @Override
  public Genre createGenre(String genreName) {
    Genre genre = new Genre(genreName);
    this.genreRepository.save(genre);
    return genre;
  }

  @Override
  public List<Genre> bulkCreateGenres(List<Genre> genres) {
    genres.forEach(genre -> this.createGenre(genre.getName()));
    return genres;
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
