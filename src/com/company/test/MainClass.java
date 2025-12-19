package com.company.test;

import com.company.jsonlib.JsonTool;

import java.util.ArrayList;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {

        // ===== Création des cours =====
        CoursDTO c1 = new CoursDTO("INF1035", "Concepts", "POO avancée", 30);
        CoursDTO c2 = new CoursDTO("INF1040", "Algo", null, 25);

        List<CoursDTO> cours = new ArrayList<>();
        cours.add(c1);
        cours.add(c2);

        // ===== Création de l'étudiant =====
        StudentDTO s = new StudentDTO();
        s.setId(1);
        s.setFirsName("Daryl");
        s.setLastName("Tafonkem");
        s.setAge(22);
        s.setGender("M");
        s.setInscriptions(cours);

        // ===== JSON Tool =====
        JsonTool jt = new JsonTool();

        // ===== Test sérialisation =====
        String json = jt.toJson(s);
        System.out.println("JSON généré :");
        System.out.println(json);

        // ===== Test désérialisation (simple) =====
        StudentDTO s2 = jt.toDTO(json, StudentDTO.class);
        System.out.println("\nObjet reconstruit :");
        System.out.println("id = " + s2.getId());
        System.out.println("firsName = " + s2.getFirsName());
        System.out.println("age = " + s2.getAge());
        System.out.println("gender = " + s2.getGender());
    }
}
