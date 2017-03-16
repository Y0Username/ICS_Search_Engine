<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="java.util.*, java.io.*, com.se.data.SearchResult, com.se.data.Document"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="stylesheet.css"> 
<title>Search Engine</title>
</head>
<body>
		<form action="results" method="post" autocomplete="on">

			<input type="text" name="search" class="search" autocomplete="on" placeholder="Enter your search term"> 
			<input type="submit" value="search" class="button">

		</form>

	<table>

		<%
			List<SearchResult> searchResults = (List<SearchResult>) request.getAttribute("searchResults");
			//out.print("Results for search query: ");
			//out.print("\n");

			for (SearchResult result : searchResults) {
		%>

		<tr>

			<td><a href="<%="//" + result.getDocument().getUrl()%>"> <%=result.getDocument().getUrl()%>
			</a> <br> <%=result.getSnippet()%></td>
			<td>SCORE: <%=result.getScore().toString()%>
			</td>
			</div>



		</tr>

		<%
			}
		%>
	</table>
</body>
</html>