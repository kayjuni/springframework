package com.mycompany.webapp.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 객체로 생성해서 관리하도록 설정
@Component
//모든 컨트롤러와 관련이 있다
@ControllerAdvice 
public class Ch10ExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(Ch10ExceptionHandler.class);
	
	//객체 생성되면 항상 생성자가 출력됨. logger로 넣어보면 알 수 있음.
	
	public Ch10ExceptionHandler() {
		logger.info("실행");
	}
	
	//예외 처리자 설정
	@ExceptionHandler
	public String handleNullPointerException(NullPointerException e) {
		logger.info("실행");
		return "error/500_null";
	}
	
	@ExceptionHandler
	public String handleClassCastException(ClassCastException e) {
		logger.info("실행");
		return "error/500_cast";
	}
	
	@ExceptionHandler
	public String handleClassCastException(Ch10SoldOutException e) {
		logger.info("실행");
		return "error/soldout";
	}
	
	@ExceptionHandler
	public String handleException(Exception e) {
		logger.info("실행");
		return "error/500";
	}
}

