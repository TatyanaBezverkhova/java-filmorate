package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import ru.yandex.practicum.filmorate.dao.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestExecutionListeners({DirtiesContextBeforeModesTestExecutionListener.class})
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final GenreDbStorage genreStorage;
    private final FilmDbStorage filmStorage;
    private final MpaDbStorage mpaStorage;
    private final FriendDbStorage friendStorage;
    private final LikeDbStorage likeStorage;
    private final FilmGenreDbStorage filmGenreStorage;

    private final FilmMpaDbStorage filmMpaStorage;

    @Test
    public void testCreateFilm() {
        addFilm();
        Optional<Film> getFilm = Optional.ofNullable(filmStorage.getFilm(4L));

        assertThat(getFilm)
                .isPresent()
                .hasValueSatisfying(filmTest -> {
                            assertThat(filmTest).hasFieldOrPropertyWithValue("id", 4L);
                            assertThat(filmTest).hasFieldOrPropertyWithValue("name", "Test");
                        }
                );
    }

    @Test
    public void testUpdateFilm() {
        Film filmTest = new Film(1, "Test", "Test Film", LocalDate.of(1995, 12, 28),
                145);
        filmStorage.update(filmTest);
        Optional<Film> getFilm = Optional.ofNullable(filmStorage.getFilm(1L));
        assertThat(getFilm)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "Test");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "Test Film");
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                    LocalDate.of(1995, 12, 28));
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 145);
                        }
                );
    }

    @Test
    public void testGetAllFilms() {
        List<Film> films = new ArrayList<>(filmStorage.getFilms());
        Film film = films.get(0);
        assertThat(films).isNotEmpty();
        assertThat(films.size()).isEqualTo(3);
        assertThat(film.getId()).isEqualTo(1);
        assertThat(film.getName()).isEqualTo("film One");
        film = films.get(2);
        assertThat(film.getId()).isEqualTo(3);
        assertThat(film.getName()).isEqualTo("film Three");
    }

    @Test
    public void testAddLike() {
        addFilm();
        likeStorage.addLike(4L, 2L);
        likeStorage.addLike(4L, 1L);
        likeStorage.addLike(4L, 3L);
        filmStorage.getFilm(4L);
        Set<Long> likes = filmStorage.getFilm(4L).getLikes();
        assertThat(likes.size()).isEqualTo(3);
    }

    @Test
    public void testDeleteLike() {
        Set<Long> likes = filmStorage.getFilm(2L).getLikes();
        assertThat(likes.size()).isEqualTo(3);
        likeStorage.deleteLike(2L, 2L);
        likes = filmStorage.getFilm(2L).getLikes();
        assertThat(likes.size()).isEqualTo(2);
    }

    @Test
    public void getPopularFilms() {
        List<Film> popularFilms = filmStorage.getPopularFilms(null);
        Film film = popularFilms.get(0);
        assertThat(film.getName()).isEqualTo("film Two");
        film = popularFilms.get(2);
        assertThat(film.getName()).isEqualTo("film Three");
        assertThat(popularFilms.size()).isEqualTo(3);
        popularFilms = filmStorage.getPopularFilms(10);
        assertThat(popularFilms.size()).isEqualTo(3);
        popularFilms = filmStorage.getPopularFilms(2);
        assertThat(popularFilms.size()).isEqualTo(2);
    }

    @Test
    public void testGetFilmById() {
        Optional<Film> getFilm = Optional.ofNullable(filmStorage.getFilm(3L));
        assertThat(getFilm)
                .isPresent()
                .hasValueSatisfying(film -> {
                            assertThat(film).hasFieldOrPropertyWithValue("id", 3L);
                            assertThat(film).hasFieldOrPropertyWithValue("name", "film Three");
                            assertThat(film).hasFieldOrPropertyWithValue("description", "description Three");
                            assertThat(film).hasFieldOrPropertyWithValue("releaseDate",
                                    LocalDate.of(2005, 11, 8));
                            assertThat(film).hasFieldOrPropertyWithValue("duration", 130);
                        }
                );
    }

    @Test
    public void testCreateGenreInFilm() {
        addFilm();
        Set<Genre> genres = new HashSet<>();
        genres.add(genreStorage.getGenreById((3)));
        genres.add(genreStorage.getGenreById(5));
        filmGenreStorage.addGenreInFilm(4L, genres);
        Film getFilm = filmStorage.getFilm(4L);
        List<Genre> genreList = getFilm.getGenres();
        assertThat(genreList).isNotEmpty();
        assertThat(genreList.size()).isEqualTo(2);
    }

    @Test
    public void testUpdateGenreInFilm() {
        addFilm();
        Set<Genre> genres = new HashSet<>();
        genres.add(genreStorage.getGenreById((3)));
        genres.add(genreStorage.getGenreById(5));
        filmGenreStorage.addGenreInFilm(4L, genres);
        List<Genre> genresTest = new ArrayList<>();
        genresTest.add(genreStorage.getGenreById(1));
        filmGenreStorage.updateGenreInFilm(4L, genresTest);
        Film getFilm = filmStorage.getFilm(4L);
        List<Genre> genreList = getFilm.getGenres();
        assertThat(genreList).isNotEmpty();
        assertThat(genreList.size()).isEqualTo(1);
    }

    @Test
    public void testGetMpaById() {
        Optional<Mpa> mpaOptional = Optional.ofNullable(mpaStorage.getMpaById(1));

        assertThat(mpaOptional)
                .isPresent()
                .hasValueSatisfying(mpa -> {
                            assertThat(mpa).hasFieldOrPropertyWithValue("id", 1);
                            assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
                        }
                );
    }

    @Test
    public void testGetAllMpa() {
        List<Mpa> mpaS = new ArrayList<>(mpaStorage.getAllMpa());
        Mpa mpa = mpaS.get(0);
        assertThat(mpaS).isNotEmpty();
        assertThat(mpaS.size()).isEqualTo(5);
        assertThat(mpa.getId()).isEqualTo(1);
        assertThat(mpa.getName()).isEqualTo("G");
        mpa = mpaS.get(4);
        assertThat(mpa.getId()).isEqualTo(5);
        assertThat(mpa.getName()).isEqualTo("NC-17");
    }

    @Test
    public void testGetGenreById() {
        Optional<Genre> genreOptional = Optional.ofNullable(genreStorage.getGenreById(1));

        assertThat(genreOptional)
                .isPresent()
                .hasValueSatisfying(genre -> {
                            assertThat(genre).hasFieldOrPropertyWithValue("id", 1);
                            assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
                        }
                );
    }

    @Test
    public void testCreateMpaInFilm() {
        addFilm();
        filmMpaStorage.addMpaInFilm(4L, 3);
        Film getFilm = filmStorage.getFilm(4L);
        assertThat(getFilm.getMpa().getId()).isEqualTo(3);
    }

    @Test
    public void testUpdateMpaInFilm() {
        assertThat(filmStorage.getFilm(1L).getMpa().getId()).isEqualTo(2);
        filmMpaStorage.updateMpaInFilm(1L, 4);
        Film getFilm = filmStorage.getFilm(1L);
        assertThat(getFilm.getMpa().getId()).isEqualTo(4);

    }

    @Test
    public void testGetAllGenre() {
        List<Genre> genres = new ArrayList<>(genreStorage.getAllGenre());
        Genre genre = genres.get(0);
        assertThat(genres).isNotEmpty();
        assertThat(genres.size()).isEqualTo(6);
        assertThat(genre.getId()).isEqualTo(1);
        assertThat(genre.getName()).isEqualTo("Комедия");
        genre = genres.get(5);
        assertThat(genre.getId()).isEqualTo(6);
        assertThat(genre.getName()).isEqualTo("Боевик");
    }

    @Test
    public void testCreateUser() {
        createUser();
        Optional<User> getUser = Optional.ofNullable(userStorage.getUser(4L));

        assertThat(getUser)
                .isPresent()
                .hasValueSatisfying(userTest -> {
                            assertThat(userTest).hasFieldOrPropertyWithValue("id", 4L);
                            assertThat(userTest).hasFieldOrPropertyWithValue("name", "TestUser");
                        }
                );
    }

    @Test
    public void testUpdateUser() {
        User userTest = new User(1L, "TestUser", "Test User", "hah@hoh.com", LocalDate.of(1995,
                12, 28));
        userStorage.update(userTest);
        Optional<User> getUser = Optional.ofNullable(userStorage.getUser(1L));
        assertThat(getUser)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 1L);
                            assertThat(user).hasFieldOrPropertyWithValue("name", "TestUser");
                            assertThat(user).hasFieldOrPropertyWithValue("login", "Test User");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "hah@hoh.com");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(1995, 12, 28));
                        }
                );
    }

    @Test
    public void testGetUserById() {
        Optional<User> getUser = Optional.ofNullable(userStorage.getUser(3L));
        assertThat(getUser)
                .isPresent()
                .hasValueSatisfying(user -> {
                            assertThat(user).hasFieldOrPropertyWithValue("id", 3L);
                            assertThat(user).hasFieldOrPropertyWithValue("name", "Donald");
                            assertThat(user).hasFieldOrPropertyWithValue("login", "Duck");
                            assertThat(user).hasFieldOrPropertyWithValue("email", "Donald@duck.com");
                            assertThat(user).hasFieldOrPropertyWithValue("birthday",
                                    LocalDate.of(1988, 1, 1));
                        }
                );
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>(userStorage.getUsers());
        User user = users.get(0);
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(3);
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getName()).isEqualTo("Tanya");
        user = users.get(2);
        assertThat(user.getId()).isEqualTo(3);
        assertThat(user.getName()).isEqualTo("Donald");
    }

    @Test
    public void testAddFriend() {
        createUser();
        friendStorage.addFriend(4L, 2L);
        friendStorage.addFriend(4L, 1L);
        friendStorage.addFriend(4L, 3L);
        userStorage.getUser(4L);
        Set<Long> friends = userStorage.getUser(4L).getFriends();
        assertThat(friends.size()).isEqualTo(3);
        friends = userStorage.getUser(2L).getFriends();
        assertThat(friends.size()).isEqualTo(1);
        friendStorage.addFriend(2L, 4L);
        friends = userStorage.getUser(2L).getFriends();
        assertThat(friends.size()).isEqualTo(2);
    }


    @Test
    public void testDeleteFriend() {
        Set<Long> friends = userStorage.getUser(1L).getFriends();
        assertThat(friends.size()).isEqualTo(2);
        friendStorage.deleteFriend(1L, 3L);
        friends = userStorage.getUser(1L).getFriends();
        assertThat(friends.size()).isEqualTo(1);
    }

    @Test
    public void testGetFriends() {
        List<User> users = friendStorage.getFriends(1L);
        assertThat(users.size()).isEqualTo(2);
        User user = users.get(0);
        assertThat(user.getName()).isEqualTo("Mikel");

    }

    @Test
    public void testGetGeneralFriends() {
        List<User> generalFriends = friendStorage.getGeneralFriends(1L, 3L);
        assertThat(generalFriends.size()).isEqualTo(1);
        User user = generalFriends.get(0);
        assertThat(user.getId()).isEqualTo(2);
        createUser();
        friendStorage.addFriend(1L, 4L);
        friendStorage.addFriend(3L, 4L);
        generalFriends = friendStorage.getGeneralFriends(1L, 3L);
        assertThat(generalFriends.size()).isEqualTo(2);

    }

    private void addFilm() {
        Film film = new Film("Test", "Test Film", LocalDate.of(1995, 12, 28),
                145);
        filmStorage.addFilm(film);
    }

    private void createUser() {
        User user = new User("TestUser", "Test User",
                "hah@hoh.com", LocalDate.of(1995, 12, 28));
        userStorage.createUser(user);
    }
}
