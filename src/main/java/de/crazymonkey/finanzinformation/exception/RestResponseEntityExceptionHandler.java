package de.crazymonkey.finanzinformation.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);
	//
	// @ExceptionHandler(value = {IllegalArgumentException.class})
	// public ResponseEntity<Object>
	// handleNotFoundException(IllegalArgumentException e, HttpServletResponse
	// response) {
	// return ResponseEntity.badRequest().body(e.getMessage());
	// }

	@ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<String> constraintViolationException(MethodArgumentTypeMismatchException ex) {
		LOG.error(ex.getMessage());
		return ResponseEntity.badRequest().body(ex.getCause().getMessage());
	}

	// @ExceptionHandler(value = { NoHandlerFoundException.class })
	// @ResponseStatus(HttpStatus.NOT_FOUND)
	// public ResponseEntity<String> noHandlerFoundException(Exception ex) {
	// LOG.error(ex.getCause().toString());
	// return ResponseEntity.badRequest().body(ex.getMessage());
	// }

	@ExceptionHandler(value = {Exception.class})
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<String> unknownException(Exception ex) {
		LOG.error(ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
	}

}