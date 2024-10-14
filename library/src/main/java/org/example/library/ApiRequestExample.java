package org.example.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiRequestExample {

    public static void main(String[] args) {
        String apiKey = "AIzaSyAoPKnrZghRA1_SemY-F4iDjKbZpkgNt7U"; // Thay YOUR_API_KEY bằng khóa API thực
        String searchTerm = "Harry Potter"; // Từ khóa tìm kiếm
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=" +
                searchTerm.replace(" ", "+") + "&key=" + apiKey; // URL API

        try {
            // Tạo đối tượng URL
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Thiết lập phương thức
            conn.setRequestMethod("GET");

            // Kiểm tra mã trạng thái
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Đọc dữ liệu từ phản hồi
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // In ra dữ liệu
                System.out.println("Response: " + response.toString());
            } else {
                System.out.println("Error: " + responseCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}