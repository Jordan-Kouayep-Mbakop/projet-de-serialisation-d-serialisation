package com.company.test;

import java.util.List;

public class StudentDTO {
    private int id;
    private String firsName; // on garde exactement ce nom
    private String lastName;
    private int age;
    private String gender;
    private List<CoursDTO> inscriptions;

    public StudentDTO() {}

    public StudentDTO(int id, String firsName, String lastName, int age, String gender, List<CoursDTO> inscriptions) {
        this.id = id;
        this.firsName = firsName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.inscriptions = inscriptions;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirsName() { return firsName; }
    public void setFirsName(String firsName) { this.firsName = firsName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public List<CoursDTO> getInscriptions() { return inscriptions; }
    public void setInscriptions(List<CoursDTO> inscriptions) { this.inscriptions = inscriptions; }
}
