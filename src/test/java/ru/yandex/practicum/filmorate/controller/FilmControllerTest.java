package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.validation.BindingResult;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmControllerTest {
    FilmController filmController = new FilmController();
    HttpClient client = HttpClient.newHttpClient();
    ObjectMapper mapper = new ObjectMapper();
    URI url;
    Film film = new Film();
    @LocalServerPort
    private int port;

    @Test
    void checkingWorkValidationExceptionFilmController() {
        defaultValues();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasFieldErrors()).thenReturn(true);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film, bindingResult));

        Mockito.when(bindingResult.hasFieldErrors()).thenReturn(false);
        Film factUser = filmController.addFilm(film, bindingResult);
        assertEquals(film, factUser);
    }

    @Test
    void filmFieldValidationPositive() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void filmFieldValidationNegative() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        mapper.registerModule(new JavaTimeModule());
        String json = "";
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void filmFieldValidationNameWithoutValue() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        film.setName("");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void filmFieldValidationNameWithNull() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        film.setName(null);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void filmFieldValidationDescriptionWithoutValue() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        film.setDescription("");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void filmFieldValidationDescriptionPositiveValue1() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        film.setDescription("b");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void filmFieldValidationDescriptionPositiveValue200() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        film.setDescription("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" +
                "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" +
                "zzzzzzzzzzzzzz");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void filmFieldValidationDescriptionNegativeValue201() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        film.setDescription("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" +
                "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" +
                "zzzzzzzzzzzzzzN");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void filmFieldValidationReleaseDateWithNull() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        film.setReleaseDate(null);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void filmFieldValidationReleaseDate() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        LocalDate release = LocalDate.of(1895, DECEMBER, 28);
        film.setReleaseDate(release);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void filmFieldValidationReleaseDateNegative() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        LocalDate release = LocalDate.of(1895, DECEMBER, 27);
        film.setReleaseDate(release);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void filmFieldValidationDurationWithZero() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        film.setDuration(0);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void filmFieldValidationDurationNegative() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        film.setDuration(-5);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putFilmValidationNamePositive() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setName("Superman");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putFilmValidationNameWithoutValue() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setName("");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putFilmValidationNameWithNull() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setName(null);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putFilmValidationDescriptionPositive() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setDescription("film for me");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putFilmValidationDescriptionPositiveValue1() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setDescription("1");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putFilmValidationDescriptionPositiveValue200() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setDescription("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" +
                "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" +
                "zzzzzzzzzzzzzz");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putFilmValidationDescriptionNegativeValue201() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setDescription("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" +
                "zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz" +
                "zzzzzzzzzzzzzzM");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putFilmValidationReleaseDatePositive() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setReleaseDate(LocalDate.of(1995, MAY, 15));
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putFilmValidationReleaseDatePositiveTime() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setReleaseDate(LocalDate.of(1895, DECEMBER, 28));
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putFilmValidationReleaseDateNegativeTime() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setReleaseDate(LocalDate.of(1895, DECEMBER, 27));
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putFilmValidationReleaseDateWithNull() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setReleaseDate(null);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putFilmValidationDurationPositive() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setDuration(111);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putFilmValidationDurationWithZero() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setDuration(0);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putFilmValidationDurationNegative() throws ValidationException, IOException, InterruptedException {
        createFilmForMethodPut();
        film.setDuration(-15);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    private void defaultValues() {
        url = URI.create("http://localhost:" + port + "/films");
        film.setName("name Film");
        film.setDescription("Description film");
        film.setReleaseDate(LocalDate.of(1965, JULY, 15));
        film.setDuration(165);
    }

    private void createFilmForMethodPut() throws IOException, InterruptedException {
        film.setId(1);
        defaultValues();
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(film);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
