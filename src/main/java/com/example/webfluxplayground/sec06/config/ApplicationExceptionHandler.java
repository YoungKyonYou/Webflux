package com.example.webfluxplayground.sec06.config;

import com.example.webfluxplayground.sec06.exceptions.CustomerNotFoundException;
import com.example.webfluxplayground.sec06.exceptions.InvalidInputException;
import java.net.URI;
import java.util.function.Consumer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public Mono<ServerResponse> handleException(CustomerNotFoundException ex, ServerRequest request){
        return handleException(HttpStatus.NOT_FOUND, ex, request, problem -> {
            problem.setType(URI.create("http://example.com/problems/invalid-input"));
            problem.setTitle("Customer Not Found");
        });

 /*       HttpStatus status = HttpStatus.NOT_FOUND;
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/customer-not-found"));
        problem.setTitle("Customer Not Found");
        problem.setInstance(URI.create(request.path()));
        return ServerResponse.status(status).bodyValue(problem);*/
    }

    @ExceptionHandler(InvalidInputException.class)
    public Mono<ServerResponse> handleException(InvalidInputException ex, ServerRequest request){
        return handleException(HttpStatus.BAD_REQUEST, ex, request, problem -> {
            problem.setType(URI.create("http://example.com/problems/invalid-input"));
            problem.setTitle("Invalid Input");
        });
/*
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/invalid-input"));
        problem.setTitle("Invalid Input");
        problem.setInstance(URI.create(request.path()));
        return ServerResponse.status(status).bodyValue(problem);*/
    }


    @ExceptionHandler(InvalidInputException.class)
    private Mono<ServerResponse> handleException(HttpStatus status, Exception ex, ServerRequest request, Consumer<ProblemDetail> consumer){
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setInstance(URI.create(request.path()));
        consumer.accept(problem);
        return ServerResponse.status(status).bodyValue(problem);
    }
}
