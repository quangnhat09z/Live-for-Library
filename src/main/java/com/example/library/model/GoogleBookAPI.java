package com.example.library.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class GoogleBookAPI {
    private static final String API_KEY = "AIzaSyAoPKnrZghRA1_SemY-F4iDjKbZpkgNt7U"; // Thay thế bằng API key của bạn

    public static String searchBooks(String query) throws Exception {
        // Mã hóa truy vấn để đảm bảo an toàn
        String encodedQuery = URLEncoder.encode(query, "UTF-8");
        String urlString = "https://www.googleapis.com/books/v1/volumes?q=" + encodedQuery + "&key=" + API_KEY;
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        // Kiểm tra mã trạng thái HTTP
        int responseCode = conn.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed : HTTP error code : " + responseCode);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private static List<String> extractTitles(String jsonResponse) {
        List<String> titles = new ArrayList<>();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        // Kiểm tra lỗi
        if (jsonObject.has("error")) {
            String errorMessage = jsonObject.getAsJsonObject("error").get("message").getAsString();
            throw new RuntimeException("API Error: " + errorMessage);
        }

        // Lấy danh sách items
        if (jsonObject.has("items")) {
            JsonArray items = jsonObject.getAsJsonArray("items");
            for (int i = 0; i < items.size(); i++) {
                JsonObject book = items.get(i).getAsJsonObject();
                JsonObject volumeInfo = book.getAsJsonObject("volumeInfo");

                // Trích xuất tiêu đề
                if (volumeInfo.has("title")) {
                    String title = volumeInfo.get("title").getAsString();
                    titles.add(title);
                }
            }
        } else {
            System.out.println("No items found in response.");
        }
        return titles;
    }
}