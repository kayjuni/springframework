package com.mycompany.webapp.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mycompany.webapp.dto.Ch14Member;
import com.mycompany.webapp.service.Ch14MemberService;
import com.mycompany.webapp.service.Ch14MemberService.JoinResult;

@Controller
@RequestMapping("/ch14")
public class Ch14Controller {
	private static final Logger logger = LoggerFactory.getLogger(Ch14Controller.class);
	
	@Resource
	private DataSource dataSource;
	
	
	@RequestMapping("/content")
	public String content() {
		return "ch14/content";
	}
	
	@GetMapping("/testConnectToDB")
	public String testConnectToDB() throws Exception {
		//커넥션 풀에서 연결 객체 하나를 가져오기
		Connection conn = dataSource.getConnection();
		logger.info("연결성공");
		
		// 커넥션 풀로 연결 객체를 반납하기
		conn.close();
		
		return "redirect:/ch14/content";
	}
	
	/*
	 * @GetMapping("/testInsert") public String testInsert() throws Exception {
	 * //커넥션 풀에서 연결 객체 하나를 가져오기 Connection conn = dataSource.getConnection();
	 * 
	 * //작업 처리 String sql =
	 * "INSERT INTO board VALUES(SEQ_BNO.NEXTVAL, ?, ?, SYSDATE, ?)";
	 * PreparedStatement pstmt = conn.prepareStatement(sql); pstmt.setString(1,
	 * "오늘은 월요일"); pstmt.setString(2,"스트레스가 이빠이 올라갔어요."); pstmt.setString(3,
	 * "user"); pstmt.executeUpdate();
	 * 
	 * 
	 * //연결 객체를 반납하기 conn.close();
	 * 
	 * return "redirect:/ch14/content"; }
	 */
	
	@GetMapping("/testInsert")
	public String testInsert() throws Exception {
		//커넥션 풀에서 연결 객체 하나를 가져오기
		Connection conn = dataSource.getConnection();
		try {
				//작업 처리
				String sql = "INSERT INTO board VALUES(SEQ_BNO.NEXTVAL, ?, ?, SYSDATE, ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "오늘은 월요일");
				pstmt.setString(2,"스트레스가 이빠이 올라갔어요.");
				pstmt.setString(3, "user");
				pstmt.executeUpdate();
				pstmt.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		
		//연결 객체를 반납하기
		conn.close(); // 예외가 생기든 안 생기든 커넥션 누수가 생기지 않도록  반납을 해줘야 함.
		
		return "redirect:/ch14/content";
	}
	
	@GetMapping("/testSelect")
	public String testSelect() throws Exception {
		//커넥션 풀에서 연결 객체 하나를 가져오기
		Connection conn = dataSource.getConnection();
		try {
				//작업 처리
				String sql = "SELECT bno, btitle, bcontent, bdate, mid FROM board";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery();
				while(rs.next()) {
					int bno = rs.getInt("bno");
					String btitle = rs.getString("btitle");
					String bcontent = rs.getString("bcontent");
					Date bdate = rs.getDate("bdate");
					String mid = rs.getString("mid");
					logger.info(bno + "\t" + btitle + "\t" + bcontent + "\t" + bdate + "\t" + mid);
				}
				rs.close();
				pstmt.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		
		//연결 객체를 반납하기
		conn.close(); // 예외가 생기든 안 생기든 커넥션 누수가 생기지 않도록  반납을 해줘야 함.
		
		return "redirect:/ch14/content";
	}
	
	@GetMapping("/testUpdate")
	public String testUpdate() throws Exception {
		
		//커넥션 풀에서 연결 객체 하나를 가져오기
		Connection conn = dataSource.getConnection();
		
		try {
				//작업 처리
				String sql = "UPDATE board SET btitle=?, bcontent=? WHERE bno=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, "배고파요");
				pstmt.setString(2,"점심 먹으러 언제 가요?");
				pstmt.setInt(3, 1);
				pstmt.executeUpdate();
				pstmt.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		
		//연결 객체를 반납하기
		conn.close(); // 예외가 생기든 안 생기든 커넥션 누수가 생기지 않도록  반납을 해줘야 함.
		
		return "redirect:/ch14/content";
	}
	
	@GetMapping("/testDelete")
	public String testDelte() throws Exception {
		
		//커넥션 풀에서 연결 객체 하나를 가져오기
		Connection conn = dataSource.getConnection();
		
		try {
				//작업 처리
				String sql = "DELETE FROM board WHERE bno=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, 0);
				pstmt.executeUpdate();
				pstmt.close();
		} catch (Exception e) {
			e.printStackTrace(); 
		} 
		
		//연결 객체를 반납하기
		conn.close(); // 예외가 생기든 안 생기든 커넥션 누수가 생기지 않도록  반납을 해줘야 함.
		
		return "redirect:/ch14/content";
	}
	
	@Resource
	private Ch14MemberService memberService;
	
	@GetMapping("/join")
	public String joinForm() {
		return "ch14/joinForm";
	}
	
	@PostMapping("/join")
	public String join(Ch14Member member, Model model) {
		member.setMenabled(1);
		member.setMrole("ROLE_USER");
		JoinResult jr = memberService.join(member);
		if(jr == JoinResult.SUCCESS) {
			return "redirect:/ch14/content";
		} else if(jr == JoinResult.DUPLICATED) {
			model.addAttribute("error", "중복된 아이디가 있습니다.");
			return "ch14/joinForm";
		} else {
			model.addAttribute("error", "회원 가입이 실패되었습니다. 다시 시도해 주세요.");
			return "ch14/joinForm";
		}
		
	}
	
	@GetMapping("/login")
	public String loginForm() {
		return "ch14/loginForm";
	}
	
	@PostMapping("/login")
	public String login(Ch14Member member, Model model) {
		return "redirect:/ch14/content";
	}
}