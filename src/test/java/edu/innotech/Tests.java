package edu.innotech;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {
    private Student student;


    @BeforeEach
    void setUp() {
        student = new Student("Vasya");
    }

    @RepeatedTest(value = 4)
    public void gradesInRange(RepetitionInfo repetitionInfo) {
        int num = repetitionInfo.getCurrentRepetition() + 1;
        student.addGrade(num);
        Assertions.assertEquals(student.getGrades().get(0), num);
    }

    @ParameterizedTest
    @MethodSource("edu.innotech.GradesGenerator#ints")
    public void gradesNotIsRange(int x) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> student.addGrade(x));
    }

    @Test
    public void gradesEncapsulation() {
        student.addGrade(5);
        List<Integer> grades = student.getGrades();
        Assertions.assertThrows(UnsupportedOperationException.class, () -> grades.add(2));
    }

    @Test
    void testToString() {
        student.addGrade(4);
        String expected ="Student(name=Vasya, grades=[4])";
        assertEquals(expected, student.toString());
    }
}