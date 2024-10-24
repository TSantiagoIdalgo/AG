package com.ancore.ancoregaming.product.services.genre;

import com.ancore.ancoregaming.product.model.Genre;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface IGenreService {

  public Genre createGenre(String genreName);

  public List<Genre> bulkCreateGenres(List<Genre> genres);

  public List<Genre> findAllGenres();

  public Genre findGenre(String name);

  public Genre destroyGenre(String name);

  public Genre updateGenre(String name);
}
