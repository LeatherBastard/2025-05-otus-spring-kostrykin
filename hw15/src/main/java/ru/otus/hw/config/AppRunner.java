package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.generator.ItemGeneratorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {
    final ItemGeneratorService itemGeneratorService;

    @Override
    public void run(String... args) throws Exception {
        itemGeneratorService.startGenerateItems();
    }
}
