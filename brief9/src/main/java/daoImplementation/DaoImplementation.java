package daoImplementation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import configuration.DBconnection;
import dao.Dao;
import models.Tasks;
import models.UserLogin;
import models.Users;

public class DaoImplementation implements Dao {

	Connection connection = DBconnection.getconnect();
	PreparedStatement ps;
	Statement statement;

	@Override
	public boolean getUser(Users user) {
		ps = null;
		String query = "SELECT userid FROM users WHERE username = ? AND password = ?";
		boolean status = false;

		try {
			ps = connection.prepareStatement(query);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());

			ResultSet rs = ps.executeQuery();

			status = rs.next();
			
			UserLogin.setCurrentUserId(rs.getInt("userid"));
			System.out.println(UserLogin.getCurrentUserId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return status;
	}

	@Override
	public boolean signUpUser(Users user) {

		boolean status = false;

		PreparedStatement psInsert = null;
		PreparedStatement psExists = null;
		ResultSet resultset = null;
		String firstname = user.getFirstname();
		String lastname = user.getLastname();
		String username = user.getUsername();
		String password = user.getPassword();

		try {
			String query = "SELECT * FROM users WHERE username = ?";
			psExists = connection.prepareStatement(query);
			psExists.setString(1, user.getUsername());
			resultset = psExists.executeQuery();

			if (!resultset.next()) {
				String query1 = "INSERT INTO users(firstname , lastname , username , password) VALUES (?,?,?,?)";
				psInsert = connection.prepareStatement(query1);
				psInsert.setString(1, firstname);
				psInsert.setString(2, lastname);
				psInsert.setString(3, username);
				psInsert.setString(4, password);
				psInsert.executeUpdate();
				status = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return status;
	}


	@Override
	public List<Tasks> listTasks() {
		List<Tasks> list = new ArrayList<Tasks>();
		PreparedStatement st;
		ResultSet rs = null;
		try {
			String query = "SELECT * FROM tasks WHERE userid=?";
			st = connection.prepareStatement(query);

			st.setInt(1, UserLogin.getCurrentUserId());
			rs = st.executeQuery();

			Tasks task;
			
			while (rs.next()) {
				task = new Tasks(rs.getInt("id"), rs.getString("title"), rs.getString("description"),
						rs.getString("status"), rs.getString("deadline"), rs.getString("categorie"));
				list.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}
	
	

	@Override
	public void create(Tasks task) {
		String title = task.getTitle();
		String description = task.getDescription();
		String status = task.getStatus();
		String deadline = task.getDeadline();
		String categorie = task.getCategorie();

		String query = "INSERT INTO tasks(title,description,status,deadline,categorie,userid) VALUES ('" + title + "','"
				+ description + "','" + status + "','" + deadline + "','" + categorie + "',"
				+ UserLogin.getCurrentUserId() + ")";

		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	@Override
	public void update(Tasks task) {
		int id = task.getId();
		String title = task.getTitle();
		String description = task.getDescription();
		String status = task.getStatus();
		String deadline = task.getDeadline();
		String categorie = task.getCategorie();

		String query = "UPDATE  tasks  SET title = '" + title + "',description='" + description + "',status='" + status
				+ "',deadline='" + deadline + "',categorie='" + categorie + "' WHERE id =" + id;

		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);

		} catch (Exception ex) {
			ex.printStackTrace();

		}
	}

	@Override
	public void delete(int id) {
		String query = "DELETE FROM tasks WHERE title =" + id;

		try {
			statement = connection.createStatement();
			statement.executeUpdate(query);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public Tasks selectTask(int id) {
		Tasks task = null;
		
		try{
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT title,description,status,deadline,categorie FROM tasks WHERE id =?");
			preparedStatement.setInt(1, id);
			
			ResultSet rs = preparedStatement.executeQuery();

		
			while (rs.next()) {
				String title = rs.getString("title");
				String description = rs.getString("description");
				String status = rs.getString("status");
				String deadline = rs.getString("deadline");
				String categorie = rs.getString("categorie");
				task = new Tasks(title, description, status, deadline,categorie);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return task;
	}

}