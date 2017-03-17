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

/**
 * Servlet implementation class secondservlet
 */
public class ResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ResultsServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		String name = request.getParameter("search");
		QueryRunner queryRunner = new QueryRunner();
		List<SearchResult> searchResults = queryRunner.search(name);

		request.setAttribute("searchResults", searchResults);
		request.getRequestDispatcher("search.jsp").forward(request, response);

//		out.print("Results for search query: " + name);
//		out.print("\n");
//		for (SearchResult searchResult : searchResults) {
//			out.println(searchResult.getDocument().getUrl() + "  || SCORE: " + searchResult.getScore().toString());
//		}
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		String name = request.getParameter("search");
		QueryRunner queryRunner = new QueryRunner();
		List<SearchResult> searchResults = queryRunner.search(name);

		request.setAttribute("searchResults", searchResults);
		request.getRequestDispatcher("search.jsp").forward(request, response);
		
//		out.print("Results for search query: " + name);
//		out.print("\n");
//		for (SearchResult searchResult : searchResults) {
//			out.println(searchResult.getDocument().getUrl() + "  || SCORE: " + searchResult.getScore().toString());
//		}
		out.close();
	}

}
