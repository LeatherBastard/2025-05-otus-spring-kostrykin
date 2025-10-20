package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.clients.GenreClient;
import org.springframework.stereotype.Service;
import ru.otus.hw.dto.genre.GenreDto;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreClient genreClient;

    @Override
    public List<GenreDto> findAll() {
        return genreClient.getGenres();
    }
}
