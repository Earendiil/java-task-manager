package taskmanager.exceptions;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice //is the specialized annotation of @ControllerAdvice for 
//REST-APIs.Better for REST-APIs. for hybrid API & HTML use @ControllerAdvice
//Methods automatically return JSON or XML responses instead of view names
public class MyGlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
		
		Map<String, String> response = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach(err -> {
		String	fieldName = ((FieldError) err).getField();
		String message= err.getDefaultMessage();
		response.put(fieldName, message);
		});
		return new  ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
	}
	 @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
	       
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }

//	    @ExceptionHandler(Exception.class)
//	    public ResponseEntity<String> handleGenericException(Exception ex) {
//	        // Return a generic error message with 500 status code
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Not found");
//	    }

	 @ExceptionHandler(RuntimeException.class)
	    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
	        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
   
	 
	 
	 
}