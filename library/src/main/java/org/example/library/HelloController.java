package org.example.library;
import javafx.scene.control.TextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField searchField;

    @FXML
    private TextArea resultText;

    @FXML
    private Button searchButton; // Thêm biến cho nút tìm kiếm

    private static final String API_KEY = "AIzaSyAoPKnrZghRA1_SemY-F4iDjKbZpkgNt7U"; // Thay thế bằng khóa API của bạn

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Nhập tên cuốn sách bạn mong muốn rồi ấn Tìm kiếm nhé!");
        searchField.setVisible(true); // Hiển thị TextField
        searchField.requestFocus(); // Đặt con trỏ vào TextField
        searchButton.setVisible(true); // Hiển thị nút Tìm kiếm
    }

    @FXML
    public void onSearchButtonClick(ActionEvent actionEvent) {
        String searchTerm = searchField.getText();
        if (!searchTerm.isEmpty()) {
            String response = searchBooks(searchTerm);
            displayResults(response);
        } else {
            resultText.setText("Vui lòng nhập từ khóa tìm kiếm.");
        }
    }

    private String searchBooks(String searchTerm) {
        StringBuilder response = new StringBuilder();
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=" +
                searchTerm.replace(" ", "+") + "&key=" + API_KEY;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                response.append("Error: ").append(responseCode);
            }
        } catch (IOException e) {
            response.append("Exception: ").append(e.getMessage());
        }

        return response.toString();
    }

    private void displayResults(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray items = jsonResponse.optJSONArray("items");

            if (items != null && items.length() > 0) {
                StringBuilder results = new StringBuilder();
                for (int i = 0; i < items.length(); i++) {
                    JSONObject book = items.getJSONObject(i);
                    JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                    String title = volumeInfo.getString("title");
                    String description = volumeInfo.optString("description", "Không có mô tả.");

                    // Thêm vào kết quả
                    results.append("Tiêu đề: ").append(title).append("\n")
                            .append("Mô tả: ").append(description).append("\n\n");
                }
                resultText.setText(results.toString());
            } else {
                resultText.setText("Không tìm thấy sách nào.");
            }
        } catch (Exception e) {
            resultText.setText("Lỗi khi phân tích dữ liệu.");
            e.printStackTrace();
        }
    }
}