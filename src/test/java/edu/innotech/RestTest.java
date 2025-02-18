package edu.innotech;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Arrays;
import java.util.List;


public class RestTest {
    String uri = "http://localhost:8080/";
    ObjectMapper mapper = new ObjectMapper();


    @Test
    @SneakyThrows
    @DisplayName("test1 - метод get и пустой массив оценок")
    public void test1() {
        Stud st = new Stud(1, "Vasya", List.of());
        String json = mapper.writeValueAsString(st);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json)
                .when().post().then()
                .statusCode(201);

        RestAssured.given()
                .baseUri(uri + "student/1")
                .contentType(ContentType.JSON)
                .when().get().then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", Matchers.equalTo(1))
                .body("name", Matchers.notNullValue())
                .body("marks", Matchers.empty());

        RestAssured.given()
                .baseUri(uri + "student/1")
                .when().delete().then()
                .statusCode(200);
    }

    //метод get и массив оценок
    @Test
    @SneakyThrows
    @DisplayName("test2 - метод get и массив оценок")
    public void test1_1() {
        Stud st = new Stud(1, "Vasya", List.of(5, 3, 4, 2));
        String json = mapper.writeValueAsString(st);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json)
                .when().post().then()
                .statusCode(201);

        RestAssured.given()
                .baseUri(uri + "student/1")
                .contentType(ContentType.JSON)
                .when().get().then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", Matchers.equalTo(1))
                .body("name", Matchers.notNullValue())
                .body("marks", Matchers.notNullValue());

        RestAssured.given()
                .baseUri(uri + "student/1")
                .when().delete().then()
                .statusCode(200);
    }

    //метод get и id студента не существует
    @Test
    @DisplayName("test3 - метод get и id студента не существует")
    public void test2() {
        RestAssured.given()
                .baseUri(uri + "student/1")
                .when().get().then()
                .statusCode(404);
    }

    @Test
    @SneakyThrows
    @DisplayName("test4 - метод post и new id студента")
    public void test3() {
        Stud st = new Stud(1, "Vasya", List.of(5, 3, 4, 2));
        String json = mapper.writeValueAsString(st);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json)
                .when().post().then()
                .statusCode(201);

        RestAssured.given()
                .baseUri(uri + "student/1")
                .when().delete().then()
                .statusCode(200);
    }

    @Test
    @SneakyThrows
    @DisplayName("test5 - метод post и  id студента ранее был")
    public void test4() {
        Stud st = new Stud(1, "Vasya", List.of());
        String json = mapper.writeValueAsString(st);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json)
                .when().post().then()
                .statusCode(201);

        Stud st1 = new Stud(1, "Vasya", List.of(5, 3, 4, 2));
        String json1 = mapper.writeValueAsString(st1);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json1)
                .when().post().then()
                .statusCode(201);

        RestAssured.given()
                .baseUri(uri + "student/1")
                .when().delete().then()
                .statusCode(200);
    }

    @Test
    @SneakyThrows
    @DisplayName("test6 - метод post и id null")
    public void test5() {
        Stud st = new Stud(null, "Vasya", List.of(5, 3, 4, 2));
        String json = mapper.writeValueAsString(st);
        int id = RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json)
                .when().post().then()
                .statusCode(201)
                .extract().as(int.class);

        RestAssured.given()
                .baseUri(uri + "student/" + id)
                .when().delete().then()
                .statusCode(200);

    }

    @Test
    @SneakyThrows
    @DisplayName("test7 - метод post и имя не заполнено")
    public void test6() {
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"Id\": 2,\n" +
                        "  \"marks\": []\n" +
                        "}"
                )
                .when().post().then()
                .statusCode(400);
    }

    @Test
    @SneakyThrows
    @DisplayName("test8 - метод delete и cтудент существует")
    public void test7() {
        Stud st = new Stud(1, "Vasya", List.of(5, 3, 4, 2));
        String json = mapper.writeValueAsString(st);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json)
                .when().post().then()
                .statusCode(201);

        RestAssured.given()
                .baseUri(uri + "student/1")
                .when().delete().then()
                .statusCode(200);
    }

    @Test
    @DisplayName("test9 - метод delete и cтудент НЕ существует")
    public void test8() {
        RestAssured.given()
                .baseUri(uri + "student/-1")
                .when().delete().then()
                .statusCode(404);
    }

    @Test
    @DisplayName("test10 - /topStudent и пустое тело, студентов нет")
    public void test9() {
        RestAssured.given()
                .baseUri(uri + "topStudent")
                .when().get().then()
                .statusCode(200)
                .body(Matchers.anything());
    }

    @Test
    @SneakyThrows
    @DisplayName("test11 - /topStudent и пустое тело, оценок нет")
    public void test10() {
        Stud st1 = new Stud(1, "Vasya", List.of());
        Stud st2 = new Stud(2, "Kate", List.of());
        String json1 = mapper.writeValueAsString(st1);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json1)
                .when().post().then()
                .statusCode(201);
        String json2 = mapper.writeValueAsString(st2);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json2)
                .when().post().then()
                .statusCode(201);

        RestAssured.given()
                .baseUri(uri + "topStudent")
                .when().get().then()
                .statusCode(200)
                .body(Matchers.anything());

        RestAssured.given()
                .baseUri(uri + "student/1")
                .when().delete().then()
                .statusCode(200);
        RestAssured.given()
                .baseUri(uri + "student/2")
                .when().delete().then()
                .statusCode(200);
    }

    @Test
    @SneakyThrows
    @DisplayName("test12 - /topStudent и один студент")
    public void test11() {
        Stud st1 = new Stud(1, "Vasya", List.of(4, 5));
        Stud st2 = new Stud(2, "Kate", List.of(4, 5, 3));
        String json1 = mapper.writeValueAsString(st1);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json1)
                .when().post().then()
                .statusCode(201);
        String json2 = mapper.writeValueAsString(st2);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json2)
                .when().post().then()
                .statusCode(201);

        Stud[] top = RestAssured.given()
                .baseUri(uri + "topStudent")
                .when().get().then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", Matchers.hasItem(st1.getId()))
                .extract().as(Stud[].class);

        System.out.println(Arrays.toString(top));
        Stud st3 = new Stud(3, "Pete", List.of(4, 4, 5, 5));
        String json3 = mapper.writeValueAsString(st3);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json3)
                .when().post().then()
                .statusCode(201);

        top = RestAssured.given()
                .baseUri(uri + "topStudent")
                .when().get().then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", Matchers.hasItem(st3.getId()))
                .extract().as(Stud[].class);

        System.out.println(Arrays.toString(top));
        RestAssured.given()
                .baseUri(uri + "student/1")
                .when().delete().then()
                .statusCode(200);
        RestAssured.given()
                .baseUri(uri + "student/2")
                .when().delete().then()
                .statusCode(200);
        RestAssured.given()
                .baseUri(uri + "student/3")
                .when().delete().then()
                .statusCode(200);
    }

    @Test
    @SneakyThrows
    @DisplayName("test13 - /topStudent и один студент")
    public void test12() {
        Stud st1 = new Stud(1, "Vasya", List.of(4, 4, 5, 5));
        Stud st2 = new Stud(2, "Kate", List.of(4, 5, 3));
        String json1 = mapper.writeValueAsString(st1);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json1)
                .when().post().then()
                .statusCode(201);
        String json2 = mapper.writeValueAsString(st2);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json2)
                .when().post().then()
                .statusCode(201);

        Stud[] top = RestAssured.given()
                .baseUri(uri + "topStudent")
                .when().get().then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", Matchers.hasItem(st1.getId()))
                .extract().as(Stud[].class);

        System.out.println(Arrays.toString(top));
        Stud st3 = new Stud(3, "Pete", List.of(4, 4, 5, 5));
        String json3 = mapper.writeValueAsString(st3);
        RestAssured.given()
                .baseUri(uri + "student")
                .contentType(ContentType.JSON)
                .body(json3)
                .when().post().then()
                .statusCode(201);

        top = RestAssured.given()
                .baseUri(uri + "topStudent")
                .when().get().then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", Matchers.containsInAnyOrder(st1.getId(),st3.getId()))
                .extract().as(Stud[].class);

        System.out.println(Arrays.toString(top));
        RestAssured.given()
                .baseUri(uri + "student/1")
                .when().delete().then()
                .statusCode(200);
        RestAssured.given()
                .baseUri(uri + "student/2")
                .when().delete().then()
                .statusCode(200);
        RestAssured.given()
                .baseUri(uri + "student/3")
                .when().delete().then()
                .statusCode(200);
    }
}