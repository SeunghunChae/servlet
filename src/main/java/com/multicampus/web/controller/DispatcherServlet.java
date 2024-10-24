package com.multicampus.web.controller;

import java.io.IOException;
import java.util.List;

import com.multicampus.biz.board.BoardDAO;
import com.multicampus.biz.board.BoardVO;
import com.multicampus.biz.user.UserDAO;
import com.multicampus.biz.user.UserVO;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "action", urlPatterns = { "*.do" })
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;	
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 1. 사용자 요청 path를 추출한다.
		String uri = request.getRequestURI();
		String path = uri.substring(uri.lastIndexOf("/"));
		
		// 2. 추출된 path에 따라 요청을 분기처리한다.
		if(path.equals("/loginView.do")) {
			
			System.out.println("로그인 화면으로 이동");
			RequestDispatcher rd = 
				request.getRequestDispatcher("/WEB-INF/board/login.jsp");
			rd.forward(request, response);			
			
		} else if(path.equals("/login.do")) {
			System.out.println("로그인 처리");
			
			// 1. 사용자 입력정보 추출
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			// 2. DB 연동 처리
			UserVO vo = new UserVO();
			vo.setId(id);
			vo.setPassword(password);
			
			UserDAO dao = new UserDAO();
			UserVO user = dao.getUser(vo);
			
			// 3. 화면 이동    	
			if(user != null) {
				// 로그인 성공 시, 상태 정보를 세션에 등록한다. 
				HttpSession session = request.getSession();
				session.setAttribute("userId", user.getId());
				session.setAttribute("userName", user.getName());
				session.setAttribute("userRole", user.getRole());
				
				RequestDispatcher rd = request.getRequestDispatcher("getBoardList.do");
				rd.forward(request, response);
			} else {
				RequestDispatcher rd = request.getRequestDispatcher("loginView.do");
				rd.forward(request, response);
			}
			
		} else if(path.equals("/logout.do")) {
			System.out.println("로그아웃 처리");
			
			// 로그아웃 요청한 브라우저와 매핑된 세션을 강제 종료한다.
			HttpSession session = request.getSession();
			session.invalidate();
			
			response.sendRedirect("index.jsp");			
			
		} else if(path.equals("/insertUserView.do")) {
			
			System.out.println("회원 등록 화면으로 이동");
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/board/insertUser.jsp");
			rd.forward(request, response);
			
		} else if(path.equals("/insertUser.do")) {
			System.out.println("회원 등록 처리");
			
			// 1. 사용자 입력정보 추출
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String role = request.getParameter("role");
			
			// 2. DB 연동 처리
			UserVO vo = new UserVO();
			vo.setId(id);
			vo.setPassword(password);
			vo.setName(name);
			vo.setRole(role);
			
			UserDAO dao = new UserDAO();
			dao.insertUser(vo);
			
			// 3. 화면이동
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);

		} else if(path.equals("/insertBoardView.do")) {
			
			System.out.println("글 등록 화면으로 이동");
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/board/insertBoard.jsp");
			rd.forward(request, response);			
			
		}  else if(path.equals("/insertBoard.do")) {
			System.out.println("글 등록 처리");
			
			// 1. 사용자 입력정보 추출
			String title = request.getParameter("title");
			String writer = request.getParameter("writer");
			String content = request.getParameter("content");    	
			
			// 2. DB 연동 처리
			BoardVO vo = new BoardVO();
			vo.setTitle(title);
			vo.setWriter(writer);
			vo.setContent(content);
			
			BoardDAO dao = new BoardDAO();
			dao.insertBoard(vo); 
			
			// 3. 화면 이동
			response.sendRedirect("getBoardList.do");
			
		} else if(path.equals("/updateBoard.do")) {
			System.out.println("글 수정 처리");
			
			// 1. 사용자 입력정보 추출
			String title = request.getParameter("title");
			String seq = request.getParameter("seq");
			String content = request.getParameter("content");    	
			
			// 2. DB 연동 처리
			BoardVO vo = new BoardVO();
			vo.setTitle(title);
			vo.setSeq(Integer.parseInt(seq));
			vo.setContent(content);
			
			BoardDAO dao = new BoardDAO();
			dao.updateBoard(vo); 
			
			// 3. 화면 이동
			RequestDispatcher rd = request.getRequestDispatcher("getBoardList.do");
			rd.forward(request, response);
			
		} else if(path.equals("/deleteBoard.do")) {
			System.out.println("글 삭제 처리");
			
			// 1. 사용자 입력정보 추출
			String seq = request.getParameter("seq");   	
			
			// 2. DB 연동 처리
			BoardVO vo = new BoardVO();
			vo.setSeq(Integer.parseInt(seq));
			
			BoardDAO dao = new BoardDAO();
			dao.deleteBoard(vo); 
			
			// 3. 화면 이동
			RequestDispatcher rd = request.getRequestDispatcher("getBoardList.do");
			rd.forward(request, response);
			
		} else if(path.equals("/getBoard.do")) {
			System.out.println("글 상세 조회 처리");
			
			// 1. 사용자 입력정보 추출
			String seq = request.getParameter("seq");
			
			// 2. DB 연동 처리
			BoardVO vo = new BoardVO();
			vo.setSeq(Integer.parseInt(seq));
			
			BoardDAO dao = new BoardDAO();
			BoardVO board = dao.getBoard(vo);
			
			// 3. 검색 결과를 request에 등록하고 글상세 화면으로 이동한다. 
			request.setAttribute("board", board);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/board/getBoard.jsp");
			rd.forward(request, response);
			
		} else if(path.equals("/getBoardList.do")) {
			System.out.println("글 목록 검색 처리");
			
			// 1. 사용자 입력정보 추출
			String searchCondition = request.getParameter("searchCondition");
			String searchKeyword = request.getParameter("searchKeyword");
			
			// Null Check
			if(searchCondition == null) searchCondition = "title";
			if(searchKeyword == null) searchKeyword = "";
			
			// 세션에 검색 관련 정보를 저장한다.
			HttpSession session = request.getSession();
			session.setAttribute("condition", searchCondition);
			session.setAttribute("keyword", searchKeyword);
			
			// 2. DB 연동 처리
			BoardVO vo = new BoardVO();
			vo.setSearchCondition(searchCondition);
			vo.setSearchKeyword(searchKeyword);
			
			BoardDAO dao = new BoardDAO();
			List<BoardVO> boardList = dao.getBoardList(vo);
			
			// 3. 검색 결과를 request에 등록하고 글목록 화면으로 이동한다. 
			request.setAttribute("boardList", boardList);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/board/getBoardList.jsp");
			rd.forward(request, response);
			
		} else {
			System.out.println("요청 URL을 확인하세요.");
		}
	}

}
