package com.example.library.model;

import java.util.*;

public class RandomNumberForQuiz {
    private static final int minQuizType1 = 1,    maxQuizType1 = 1540;
    private static final int minQuizType2 = 1541, maxQuizType2 = 3293;
    private static final int minQuizType3 = 3294, maxQuizType3 = 4398;
    private static final int minQuizType4 = 4399, maxQuizType4 = 4435;

    public static List<Integer> getRandomUniqueNumbers(int n, int a, int b) {
        if (n > (b - a + 1)) {
            throw new IllegalArgumentException("Không thể chọn nhiều hơn số lượng số trong khoảng!");
        }
        Set<Integer> uniqueNumbers = new HashSet<>();
        Random random = new Random();

        while (uniqueNumbers.size() < n) {
            int num = random.nextInt(b - a + 1) + a; // Random số trong khoảng [a, b]
            uniqueNumbers.add(num);
        }
        return new ArrayList<>(uniqueNumbers);
    }

    public static List<Integer> get10First() {
        List<Integer> finalList = new ArrayList<>();
        List<Integer> list1 = RandomNumberForQuiz.getRandomUniqueNumbers(3, minQuizType1, maxQuizType1);
        List<Integer> list2 = RandomNumberForQuiz.getRandomUniqueNumbers(3, minQuizType2, maxQuizType2);
        List<Integer> list3 = RandomNumberForQuiz.getRandomUniqueNumbers(3, minQuizType3, maxQuizType3);
        List<Integer> list4 = RandomNumberForQuiz.getRandomUniqueNumbers(1, minQuizType4, maxQuizType4);

        finalList.addAll(list1);
        finalList.addAll(list2);
        finalList.addAll(list3);
        finalList.addAll(list4);
        Collections.shuffle(finalList);
        return finalList;
    }

    public static List<Integer> generateRandomNumbers(int n, int a, int b, List<Integer>... excludeLists) {
        // Tập hợp tất cả các số cần loại trừ
        Set<Integer> excludedNumbers = new HashSet<>();
        for (List<Integer> list : excludeLists) {
            excludedNumbers.addAll(list);
        }

        // Kiểm tra khả năng sinh đủ số lượng số
        int totalPossibleNumbers = (b - a + 1) - excludedNumbers.size();
        if (n > totalPossibleNumbers) {
            throw new IllegalArgumentException("Không thể chọn đủ số không trùng lặp!");
        }

        // Sinh số ngẫu nhiên
        Set<Integer> resultSet = new HashSet<>();
        Random random = new Random();
        while (resultSet.size() < n) {
            int num = random.nextInt(b - a + 1) + a; // Random số trong khoảng [a, b]
            if (!excludedNumbers.contains(num)) {
                resultSet.add(num);
            }
        }
        return new ArrayList<>(resultSet);
    }

    public static List<Integer> getFinalList() {
        // Lấy 10 số đầu tiên
        List<Integer> finalList = get10First();
        // Sinh thêm 10 số không trùng
        List<Integer> secondPart = generateRandomNumbers(50, minQuizType1, maxQuizType4, finalList);
        finalList.addAll(secondPart);
        return finalList;
    }
}
