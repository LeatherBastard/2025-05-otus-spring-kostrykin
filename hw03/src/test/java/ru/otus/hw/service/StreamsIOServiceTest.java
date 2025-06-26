package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StreamsIOServiceTest {
    private final PrintStream printStream = Mockito.mock(PrintStream.class);
    private final InputStream inputStream = new ByteArrayInputStream("-1\n-1\n-1\n-1\n-1\n-1\n-1\n-1\n-1\n-1"
            .getBytes(StandardCharsets.UTF_8));

    private IOService ioService;

    @BeforeEach
    void initialize() {
        ioService = new StreamsIOService(printStream, inputStream);
    }

    @Test
    void whenReadIntForRange_thenThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> ioService.readIntForRange(1, 2, "Error"));
    }
}
