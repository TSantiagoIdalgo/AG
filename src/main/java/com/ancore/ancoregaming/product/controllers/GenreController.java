package com.ancore.ancoregaming.product.controllers;

import com.ancore.ancoregaming.common.ApiResponse;
import com.ancore.ancoregaming.product.model.Genre;
import com.ancore.ancoregaming.product.services.genre.IGenreService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/genre")
public class GenreController {

  @Autowired
  private IGenreService genreService;

  @GetMapping("/")
  public ResponseEntity<ApiResponse<List<Genre>>> getAllGenres() {
    List<Genre> genres = this.genreService.findAllGenres();
    ApiResponse<List<Genre>> response = new ApiResponse<>(HttpStatus.OK, genres, null);
    return ResponseEntity.status(200).body(response);
  }

  @GetMapping("/{name}")
  public ResponseEntity<ApiResponse<Genre>> getGenre(@PathVariable String name) {
    Genre genre = this.genreService.findGenre(name);
    ApiResponse<Genre> response = new ApiResponse<>(HttpStatus.OK, genre, null);
    return ResponseEntity.status(200).body(response);
  }

  @PostMapping("/")
  public ResponseEntity<ApiResponse<Genre>> createGenre(@RequestBody Genre genre) {
    Genre newGenre = this.genreService.createGenre(genre.getName());
    ApiResponse<Genre> response = new ApiResponse<>(HttpStatus.OK, newGenre, null);
    return ResponseEntity.status(200).body(response);
  }

  @DeleteMapping("/{name}")
  public ResponseEntity<ApiResponse<Genre>> deleteGenre(@PathVariable String name) {
    Genre genre = this.genreService.destroyGenre(name);
    ApiResponse<Genre> response = new ApiResponse<>(HttpStatus.OK, genre, null);
    return ResponseEntity.status(200).body(response);
  }
}
