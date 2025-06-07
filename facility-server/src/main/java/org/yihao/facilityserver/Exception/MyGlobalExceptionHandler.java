package org.yihao.facilityserver.Exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class MyGlobalExceptionHandler {

/*    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> response = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String message = err.getDefaultMessage();
            response.put(fieldName, message);
        });
        return new ResponseEntity<Map<String, String>>(response,
                HttpStatus.BAD_REQUEST);
    }*/

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e) {
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<APIResponse> myAPIException(APIException e) {
        String message = e.getMessage();
        APIResponse apiResponse = new APIResponse(message, false);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
//
//    @ExceptionHandler(FeignException.class)
//    public ResponseEntity<APIResponse> handleFeignException(FeignException e) {
//        try {
//            // Extract the raw response body from the FeignException as a UTF-8 string
//            String body = e.contentUTF8();
//
//            // Use Jackson ObjectMapper to convert the JSON string into an APIResponse object
//            ObjectMapper mapper = new ObjectMapper();
//            APIResponse response = mapper.readValue(body, APIResponse.class);
//
//            // Return the deserialized APIResponse and set the HTTP status to match the remote service's response
//            return new ResponseEntity<>(response, HttpStatus.valueOf(e.status()));
//        } catch (Exception ex) {
//            // If something goes wrong while parsing (e.g. invalid JSON), return a generic fallback response
//            return new ResponseEntity<>(
//                    new APIResponse("Remote service error", false), // Custom error message
//                    HttpStatus.BAD_GATEWAY // 502: indicates problem with upstream service
//            );
//        }
//    }
}
