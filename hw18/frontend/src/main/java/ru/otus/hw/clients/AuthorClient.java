package ru.otus.hw.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.dto.author.AuthorDto;

import java.util.List;

@FeignClient(name = "genre-client", url = "http://localhost:8080/api")
public interface AuthorClient {
    @GetMapping("/authors")
    List<AuthorDto> getAuthors();
}
