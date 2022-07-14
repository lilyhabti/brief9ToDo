package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Tasks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import daoImplementation.DaoImplementation;


public class Space extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    DaoImplementation dao = new DaoImplementation();
	
    public Space() {
        super();
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getServletPath();

		try {
			switch (action) {
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
				insertUser(request, response);
				break;
			case "/delete":
				deleteUser(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/update":
				updateUser(request, response);
				break;
			default:
				listUser(request, response);
				break;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
		
	}
	
	private void listUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
		List<Tasks> listTasks = dao.listTasks();
		request.setAttribute("listTasks", listTasks);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsps/space.jsp");
		dispatcher.forward(request, response);
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsps/actions.jsp");
		dispatcher.forward(request, response);
	}
	 
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("ID"));
		Tasks existingTask = dao.selectTask(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/jsps/actions.jsp");
		request.setAttribute("task", existingTask);
		dispatcher.forward(request, response);

	}
	
	private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String status = request.getParameter("status");
		String deadline = request.getParameter("deadline");
		String categorie = request.getParameter("categorie");
		
		Tasks newTask = new Tasks(title, description, status,deadline,categorie);
		dao.create(newTask);
		response.sendRedirect("list");
	}
	
	private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String status = request.getParameter("status");
		String deadline = request.getParameter("deadline");
		String categorie = request.getParameter("categorie");;

		Tasks oldTask = new Tasks(title, description, status,deadline,categorie);
		dao.update(oldTask);
		response.sendRedirect("list");
	}
	
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("ID"));
		dao.delete(id);
		response.sendRedirect("list");

	}

}