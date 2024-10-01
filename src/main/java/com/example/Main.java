package com.example;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
     // Assurez-vous de remplacer ces valeurs par vos propres informations de connexion
    private static final String URL = "jdbc:mysql://localhost:3306/schoolman";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            // Exemple d'étudiant à enregistrer
            Etudiant E2 = new Etudiant("00002", "Irumva", "Brice Berry", "Kigobe", 22);


            Etudiant E3 = new Etudiant();

            E3.setId("07");
            E3.setNom("mnm");
            E3.setPrenom("m");
            E3.setAdress("Kigombe");
            E3.setAge(202);
            // // Méthode 1: Requête simple
            enregistrerEtudiantSimple(conn, E3);

            // // Méthode 2: PreparedStatement
            enregistrerEtudiantPrepared(conn, E3);

            // // Méthode 3: CallableStatement
            enregistrerEtudiantCallable(conn, E3);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode 1: Requête simple
    private static void enregistrerEtudiantSimple(Connection conn, Etudiant E1) throws SQLException {

        String sql = "INSERT INTO etudiants (id,nom, prenom, adresse,age) VALUES ('" + E1.getId() + "', '" + E1.getNom() + "', '" + E1.getPrenom() + "', '" + E1.getAdress() + "', " + E1.getAge() + ")";
        try (Statement stmt = conn.createStatement()) {
            int rowsAffected = stmt.executeUpdate(sql);
            System.out.println(rowsAffected + " étudiant(s) enregistré(s) avec la requête simple.");
        }
    }

    // Méthode 2: PreparedStatement
    private static void enregistrerEtudiantPrepared(Connection conn,Etudiant E1) throws SQLException {
        String sql = "INSERT INTO etudiants (id,nom, prenom, adresse,age) VALUES (?, ?,?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, E1.getId());
            pstmt.setString(2, E1.getNom());
            pstmt.setString(3, E1.getPrenom());
            pstmt.setString(4, E1.getAdress());
            pstmt.setInt(5, E1.getAge());
            int rowsAffected = pstmt.executeUpdate();
            System.out.println(rowsAffected + " étudiant(s) enregistré(s) avec PreparedStatement.");
        }
    }

    // Méthode 3: CallableStatement
    private static void enregistrerEtudiantCallable(Connection conn,Etudiant E1) throws SQLException {
        String sql = "{call enregistrer_etudiant(?, ?, ?, ?, ?)}";
        try (CallableStatement cstmt = conn.prepareCall(sql)) {
            cstmt.setString(1, E1.getId());
            cstmt.setString(2, E1.getNom());
            cstmt.setString(3, E1.getPrenom());
            cstmt.setString(4, E1.getAdress());
            cstmt.setInt(5, E1.getAge());
            cstmt.execute();
            System.out.println("Étudiant enregistré avec CallableStatement.");
        }
    }
}