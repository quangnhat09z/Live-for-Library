package com.example.library.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GetQuiz {
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper();

        try (Connection connection = db.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM quiz")) {

            while (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setId(rs.getInt("id"));
                quiz.setQuestion(rs.getString("question"));
                quiz.setRightAnswer(rs.getString("rightAnswer"));
                quiz.setWrongAnswer1(rs.getString("wrongAnswer1"));
                quiz.setWrongAnswer2(rs.getString("wrongAnswer2"));
                quiz.setWrongAnswer3(rs.getString("wrongAnswer3"));
                quiz.setImageLink(rs.getString("imageLink"));
                quizzes.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    public List<Quiz> getNumberOfQuiz() {
        List<Integer> listQuizId = RandomNumberForQuiz.getFinalList();
        List<Quiz> quizzes = new ArrayList<>();
        DatabaseHelper db = new DatabaseHelper();

        String query = "SELECT * FROM quiz WHERE id = ?";

        try (Connection connection = db.connect();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            for (int quizId : listQuizId) {
                pstmt.setInt(1, quizId); // Gán giá trị tham số "?" bằng `quizId`
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Quiz quiz = new Quiz();
                        quiz.setId(rs.getInt("id"));
                        quiz.setQuestion(rs.getString("question"));
                        quiz.setRightAnswer(rs.getString("rightAnswer"));
                        quiz.setWrongAnswer1(rs.getString("wrongAnswer1"));
                        quiz.setWrongAnswer2(rs.getString("wrongAnswer2"));
                        quiz.setWrongAnswer3(rs.getString("wrongAnswer3"));
                        quiz.setImageLink(rs.getString("imageLink"));
                        quizzes.add(quiz);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(listQuizId);
        return quizzes;
    }
}