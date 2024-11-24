package com.ancore.ancoregaming.product.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ancore.ancoregaming.common.ApiEntityResponse;
import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.services.genre.IGenreService;

@RestController
@RequestMapping("/genre")
public class GenreController {

  @Autowired
  private IGenreService genreService;

  @Secured("ROLE_ADMIN")
  @GetMapping("/")
  public ApiEntityResponse<List<Genre>> getAllGenres() {
    List<Genre> genres = this.genreService.findAllGenres();
    ApiResponse<List<Genre>> response = new ApiResponse<>(genres, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @GetMapping("/{name}")
  public ApiEntityResponse<Genre> getGenre(@PathVariable String name) {
    Genre genre = this.genreService.findGenre(name);
    ApiResponse<Genre> response = new ApiResponse<>(genre, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }

  @PostMapping("/")
  public ApiEntityResponse<Genre> createGenre(@RequestBody Genre genre) {
    Genre newGenre = this.genreService.createGenre(genre.getName());
    ApiResponse<Genre> response = new ApiResponse<>(newGenre, null);
    return ApiEntityResponse.of(HttpStatus.CREATED, response);
  }

  @DeleteMapping("/{name}")
  public ApiEntityResponse<Genre> deleteGenre(@PathVariable String name) {
    Genre genre = this.genreService.destroyGenre(name);
    ApiResponse<Genre> response = new ApiResponse<>(genre, null);
    return ApiEntityResponse.of(HttpStatus.OK, response);
  }
}
