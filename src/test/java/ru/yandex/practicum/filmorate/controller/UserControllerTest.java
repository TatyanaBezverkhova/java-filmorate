package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    HttpClient client = HttpClient.newHttpClient();
    ObjectMapper mapper = new ObjectMapper();
    User user = new User();
    URI url;
    @LocalServerPort
    private int port;

    @Test
    void userValidationPositive() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void userValidationNegative() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        mapper.registerModule(new JavaTimeModule());
        String json = " ";
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void userFieldValidationEmailNegative() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        user.setEmail("emailyandex.ru");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void userFieldValidationEmailWithoutValue() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        user.setEmail("");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void userFieldValidationEmailWithNull() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        user.setEmail(null);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void userFieldValidationLoginWithoutValue() throws IOException, InterruptedException {
        defaultValues();
        user.setLogin("");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void userFieldValidationLoginWithNull() throws IOException, InterruptedException {
        defaultValues();
        user.setLogin(null);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void userFieldValidationNameWithoutValue() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        user.setName("");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void userFieldValidationNameWithNull() throws ValidationException, IOException, InterruptedException {
        defaultValues();
        user.setName(null);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void userFieldValidationBirthdayPresentTime() throws IOException, InterruptedException {
        LocalDate nowData = LocalDate.now();
        defaultValues();
        user.setBirthday(nowData);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void userFieldValidationBirthdayFutureTime() throws IOException, InterruptedException {
        defaultValues();
        user.setBirthday(LocalDate.of(2023, NOVEMBER, 15));
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void userFieldValidationBirthdayWithNull() throws IOException, InterruptedException {
        defaultValues();
        user.setBirthday(null);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putUserValidationLoginPositive() throws ValidationException, IOException, InterruptedException {
        createUserForMethodPut();
        user.setLogin("login Test");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putUserValidationEmailPositive() throws ValidationException, IOException, InterruptedException {
        createUserForMethodPut();
        user.setEmail("tanya@yandex.ru");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putUserValidationEmailNegative() throws IOException, InterruptedException {
        createUserForMethodPut();
        user.setEmail("tanya");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putUserValidationEmailWithoutValue() throws ValidationException, IOException, InterruptedException {
        createUserForMethodPut();
        user.setEmail("");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putUserValidationEmailWithNull() throws ValidationException, IOException, InterruptedException {
        createUserForMethodPut();
        user.setEmail(null);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putUserValidationLoginWithoutValue() throws ValidationException, IOException, InterruptedException {
        createUserForMethodPut();
        user.setLogin("");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putUserValidationLoginWithNull() throws ValidationException, IOException, InterruptedException {
        createUserForMethodPut();
        user.setLogin(null);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    void putUserValidationName() throws ValidationException, IOException, InterruptedException {
        createUserForMethodPut();
        user.setName("tanya");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putUserValidationNameWithoutValue() throws ValidationException, IOException, InterruptedException {
        createUserForMethodPut();
        user.setName("");
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putUserValidationBirthdayPositive() throws ValidationException, IOException, InterruptedException {
        createUserForMethodPut();
        user.setBirthday(LocalDate.of(1990, JULY, 11));
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putUserValidationBirthdayPresentTime() throws ValidationException, IOException, InterruptedException {
        createUserForMethodPut();
        LocalDate presentData = LocalDate.now();
        user.setBirthday(presentData);
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    void putUserValidationBirthdayFutureTime() throws ValidationException, IOException, InterruptedException {
        createUserForMethodPut();
        user.setBirthday(LocalDate.of(2023, JULY, 11));
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).PUT(requestBody)
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    private void defaultValues() {
        url = URI.create("http://localhost:" + port + "/users");
        user.setName("UserName");
        user.setLogin("login");
        user.setEmail("email@yandex.ru");
        user.setBirthday(LocalDate.of(1961, NOVEMBER, 15));
    }

    private void createUserForMethodPut() throws IOException, InterruptedException {
        user.setId(1);
        defaultValues();
        mapper.registerModule(new JavaTimeModule());
        String json = mapper.writeValueAsString(user);
        final HttpRequest.BodyPublisher requestBody = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(requestBody)
                .header("Content-Type", "application/json")
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
