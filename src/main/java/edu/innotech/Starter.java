package edu.innotech;

public class Starter {
    public static void main(String[] args) {
        System.out.println("HW");
        Student st  = new Student("Vasya");
        st.addGrade(3);
        st.addGrade(5);
        System.out.println(st);
        st.getGrades().add(5);
        System.out.println(st.getGrades());
    }
}
