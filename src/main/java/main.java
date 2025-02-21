import java.sql.*;

public class main {
    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection("jdbc:h2:.\\Office")) {

            Statement stm = con.createStatement();
  //             delete(con);
            stm.executeUpdate("INSERT INTO Employee VALUES (6, 'kate', 1)");
            stm.executeUpdate("INSERT INTO Employee VALUES (7, 'mike',2)");
            stm.executeUpdate("INSERT INTO Employee VALUES (8, 'max', 3)");
            selectAll(con);
            updateDepartmentForAnn(con);
            correctEmployeeNames(con);
            countIt(con);
            System.out.println("--------------------------------------");
            selectAll(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateDepartmentForAnn(Connection con) throws SQLException {
        int i = 0;
        String txt = "Select * from Employee where Name like ?";
        PreparedStatement pstm = con.prepareStatement(txt, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        pstm.setString(1, "Ann");
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            i++;
        }
        if (i == 1) {
            rs.first();
            rs.updateString("DepartmentID", "3");
            rs.updateRow();
        } else {
            System.out.println("Сотрудник с именем Ann больше одного.");
        }
    }

    private static int correctEmployeeNames(Connection con) throws SQLException {
        int count = 0;
        String txt = "SELECT id, name FROM employee";
        try (PreparedStatement selectStmt = con.prepareStatement(txt);
             ResultSet resultSet = selectStmt.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                if (Character.isLowerCase(name.charAt(0))) {
                    String correctedName = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                    String updateSql = "UPDATE employee SET name = ? WHERE id = ?";
                    try (PreparedStatement updateStmt = con.prepareStatement(updateSql)) {
                        updateStmt.setString(1, correctedName);
                        updateStmt.setInt(2, id);
                        updateStmt.executeUpdate();
                        count++;
                    }
                }
            }
            System.out.println("--------------------------------------");
            System.out.println("Записей исправлено: " + count);
        }
        return count;
    }

    private static int countIt(Connection con) throws SQLException {
        int count = 0;
        String txt = "Select * from Employee where DepartmentID like ?";
        PreparedStatement pstm = con.prepareStatement(txt, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        pstm.setString(1, "2");
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            count++;
        }
        if (count >0) {
            System.out.println("--------------------------------------");
            System.out.println("Всего в IT департаменте: " + count);
        } else {
            System.out.println("В IT департаменте никто не работает");
        }
        return count;
    }

    private static void delete(Connection con) throws SQLException {
        Statement stm = con.createStatement();
        stm.executeUpdate("DROP TABLE Employee IF EXISTS");
        stm.executeUpdate("CREATE TABLE Employee(ID INT PRIMARY KEY, NAME VARCHAR(255), DepartmentID INT)");
        stm.executeUpdate("INSERT INTO Employee VALUES(1,'Pete',1)");
        stm.executeUpdate("INSERT INTO Employee VALUES(2,'Ann',1)");
        stm.executeUpdate("INSERT INTO Employee VALUES(3,'Liz',2)");
        stm.executeUpdate("INSERT INTO Employee VALUES(4,'Tom',2)");
        stm.executeUpdate("INSERT INTO Employee VALUES(5,'Todd',3)");
    }

    private static void selectAll(Connection con) throws SQLException {
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery("Select Employee.ID, Employee.Name,Department.Name as DepName from Employee join Department on Employee.DepartmentID=Department.ID");
        while (rs.next()) {
            System.out.println(rs.getInt("ID") + "\t" + rs.getString("NAME") + "\t" + rs.getString("DepName"));
        }
    }
}