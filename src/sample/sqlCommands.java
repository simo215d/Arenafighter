package sample;

import java.sql.*;
import java.util.ArrayList;

public class sqlCommands {
    public ArrayList<String> getFriendNames() throws SQLException{
        ArrayList<String> names = new ArrayList<>();
        String sqlQuery = "SELECT name FROM friendslist";
        //connect
        Connection connection = null;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/lolfriendstest1?serverTimezone=UTC",
                "root", "qtj38gve");
        //create statement
        Statement statement = connection.createStatement();
        //execute statement
        ResultSet rs = statement.executeQuery(sqlQuery);
        //get getResults
        while (rs.next()){
            names.add(rs.getString("name"));
        }
        //Close the connection (always!)
        connection.close();
        return names;
    }

    public ArrayList<String> getFriendStatuses() throws SQLException{
        ArrayList<String> statuses = new ArrayList<>();
        String sqlQuery = "SELECT status FROM friendslist";
        //connect
        Connection connection = null;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/lolfriendstest1?serverTimezone=UTC",
                "root", "qtj38gve");
        //create statement
        Statement statement = connection.createStatement();
        //execute statement
        ResultSet rs = statement.executeQuery(sqlQuery);
        //get getResults
        while (rs.next()){
            statuses.add(rs.getString("status"));
        }
        //Close the connection (always!)
        connection.close();
        return statuses;
    }

    public String addFriend(String newName) throws SQLException{
        //check if name is valid (contains spaces?)
        if (newName.contains(" ")){
            System.out.println("enter a valid name :)");
            return "error";
        }
        //connect
        Connection connection = null;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/lolfriendstest1?serverTimezone=UTC",
                "root", "qtj38gve");
        //create statement
        //statement that checks if name already exists
        boolean occurred = false;
        PreparedStatement sqlQueryPreparedOccurrence = connection.prepareStatement("SELECT name FROM friendslist WHERE name=?");
        sqlQueryPreparedOccurrence.setString(1,newName);
        ResultSet rs = sqlQueryPreparedOccurrence.executeQuery();
        while (rs.next()){
            occurred=true;
        }
        if (occurred){
            System.out.println("name already exists in your friendslist");
            connection.close();
            return "error";
        }
        //statement that adds friend
        PreparedStatement sqlQueryPrepared = connection.prepareStatement("INSERT INTO friendslist (name, status)"+"VALUES(?,?)");
        sqlQueryPrepared.setString(1,newName);
        sqlQueryPrepared.setString(2,"Offline");
        //execute statement
        sqlQueryPrepared.execute();
        System.out.println(newName+" has been added to database!");
        //Close the connection (always!)
        connection.close();
        return "success";
    }

    public String removeFriend(String newName) throws SQLException{
        //connect
        Connection connection = null;
        connection = DriverManager.getConnection(
                "jdbc:mysql://localhost/lolfriendstest1?serverTimezone=UTC",
                "root", "qtj38gve");
        //create statement
        //statement that checks if friend is in database
        boolean occurred = false;
        PreparedStatement sqlQueryPreparedOccurrence = connection.prepareStatement("SELECT name FROM friendslist WHERE name=?");
        sqlQueryPreparedOccurrence.setString(1,newName);
        ResultSet rs = sqlQueryPreparedOccurrence.executeQuery();
        while (rs.next()){
            occurred=true;
        }
        if (occurred==false){
            System.out.println("friend does not exist xd");
            connection.close();
            return "error";
        }
        //statement that adds friend
        PreparedStatement sqlQueryPrepared = connection.prepareStatement("DELETE FROM friendslist WHERE name=?");
        sqlQueryPrepared.setString(1,newName);
        //execute statement
        sqlQueryPrepared.execute();
        System.out.println(newName+" has been removed from database!");
        //Close the connection (always!)
        connection.close();
        return "success";
    }

    public static void main(String[] args) {
        sqlCommands sql = new sqlCommands();
        /*try {
            ArrayList<String> names = sql.getFriendName(1);
            for (String name : names){
                System.out.println(name);
            }
        } catch (SQLException error){
            System.out.println("sql error");
        }
         */
    }
}
