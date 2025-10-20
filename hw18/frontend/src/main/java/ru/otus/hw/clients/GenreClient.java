package ru.otus.hw.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.dto.genre.GenreDto;

import java.util.List;

@FeignClient(name = "user-client", url = "http://localhost:8080/api")
public interface GenreClient {
    @GetMapping("/genres")
    List<GenreDto> getGenres();

}
