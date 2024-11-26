package com.example.library.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class searchAccount {

    public static List<AccountDetail> searchAccounts(String id, String email, String username,
                                                     String role,
                                                     String phone) {
        List<AccountDetail> accounts = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT * " +
                        "FROM user_verification uv " +
                        "JOIN accounts acc " +
                        "ON uv.id = acc.id " +
                        "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();

        if (!id.isEmpty()) {
            queryBuilder.append(" AND acc.id = ?");
            params.add(id);
        }
        if (!email.isEmpty()) {
            queryBuilder.append(" AND acc.email = ?");
            params.add(email);
        }
        if (!username.isEmpty()) {
            queryBuilder.append(" AND acc.username = ?");
            params.add(username);
        }
        if (!role.isEmpty()) {
            queryBuilder.append(" AND acc.role = ?");
            params.add(role);
        }
        if (!phone.isEmpty()) {
            queryBuilder.append(" AND uv.phone_number = ?");
            params.add(phone);
        }

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     queryBuilder.toString())) {

            for (int i = 0; i < params.size(); i++) {
                preparedStatement.setObject(i + 1, params.get(i));
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int accountID = resultSet.getInt("id");
                String accountUserName = resultSet.getString("username");
                String accountPassWord = resultSet.getString("password");
                String accountEmail = resultSet.getString("email");
                String accountFullName = resultSet.getString("full_name");
                String accountAddress = resultSet.getString("address");
                String accountPhoneNumber = resultSet.getString("phone_number");
                String accountStatus = resultSet.getString("status");
                String accountRole = resultSet.getString("role");

                // Add if need

                AccountDetail accountDetail = new AccountDetail(accountID, accountUserName, accountPassWord,
                        accountEmail, accountRole, accountFullName, accountAddress, accountPhoneNumber,
                        accountStatus);
                accounts.add(accountDetail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accounts;
    }


}
