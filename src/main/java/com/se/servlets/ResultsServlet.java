package com.se.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.se.data.SearchResult;
import com.se.query.QueryRunner;

public class ResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ResultsServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ")
				.append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		String name = request.getParameter("search");
		QueryRunner queryRunner = new QueryRunner();
		List<SearchResult> searchResults = queryRunner.search(name);

		request.setAttribute("searchResults", searchResults);
		request.getRequestDispatcher("search.jsp").forward(request, response);

		out.close();
	}

}
