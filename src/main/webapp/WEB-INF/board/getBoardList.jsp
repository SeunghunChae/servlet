<%@page import="com.multicampus.biz.board.BoardDAO"%>
<%@page import="java.util.List"%>
<%@page import="com.multicampus.biz.board.BoardVO"%>
<%@page contentType="text/html; charset=UTF-8"%>

<%
	// 1. request에 저장된 검색 결과를 꺼낸다.
	List<BoardVO> boardList = (List) request.getAttribute("boardList");
	
	// 2. 응답 화면 구성
%>

<%@include file="../layout/header.jsp" %>

<center>

<form action="getBoardList.jsp" method="get">
<table border="1" cellpadding="0" cellspacing="0" width="800">
	<tr>
		<td align="right">
			<select name="searchCondition">
				<option value="title">제목
				<option value="content">내용
			</select>
			<input name="searchKeyword" type="text"/>
			<input type="submit" value="검색"/>
		</td>
	</tr>
</table>
</form>

<table border="1" cellpadding="0" cellspacing="0" width="800">
	<tr>
		<th width="100" bgcolor="orange">번호</th>
		<th width="300" bgcolor="orange">제목</th>
		<th width="150" bgcolor="orange">작성자</th>
		<th width="150" bgcolor="orange">등록일</th>
		<th width="100" bgcolor="orange">조회수</th>
	</tr>
	<% for(BoardVO board : boardList) { %>
	<tr>
		<td><%= board.getSeq() %></td>
		<td><a href="getBoard.do?seq=<%= board.getSeq() %>"><%= board.getTitle() %></a></td>
		<td><%= board.getWriter() %></td>
		<td><%= board.getRegDate() %></td>
		<td><%= board.getCnt() %></td>
	</tr>
	<% } %>
</table>

</center>

<%@include file="../layout/footer.jsp" %>
