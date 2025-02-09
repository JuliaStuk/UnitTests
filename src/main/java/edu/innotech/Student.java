package edu.innotech;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
public class Student {
    @Setter
    private StudentRepo repo;

    @Getter    @Setter
    private String name;
    private List grades = new ArrayList<>();

    public Student(String name) {
        this.name = name;
    }

    public List getGrades() {
        return new ArrayList<>(grades);
    }

    public void setRepo(StudentRepo repo) {
        this.repo = repo;
    }

    @SneakyThrows
    public void addGrade(int grade) {
        if (!repo.checkGrade(grade))
            throw new IllegalArgumentException(grade + " is wrong grade");
        grades.add(grade);
    }

    @SneakyThrows
    public int raiting() {
       return repo.getRaiting(
               grades.stream()
                       .mapToInt(x-> (int) x)
                       .sum()
       );
    }
}