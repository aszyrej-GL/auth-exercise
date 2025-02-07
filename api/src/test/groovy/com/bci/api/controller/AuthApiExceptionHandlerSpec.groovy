package com.bci.api.controller

import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.core.MethodParameter
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.mock.http.MockHttpInputMessage
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestHeaderException
import spock.lang.Specification

import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException

import static org.springframework.http.HttpStatus.*

class AuthApiExceptionHandlerSpec extends Specification {

    AuthApiExceptionHandler handler = new AuthApiExceptionHandler()

    def "Handle API exceptions"() {
        given: "Exception is received"
        exception

        when: "The handler processes the exception"
        def response = this.handler.handle(exception)

        then: "Error response is correctly returned"
        response.getStatusCode() == expectedStatus
        noExceptionThrown()

        where:
        exception                                                           | expectedStatus
        createMethodArgumentNotValidException()                             | BAD_REQUEST
        new EntityExistsException()                                         | CONFLICT
        new MissingRequestHeaderException("error", Mock(MethodParameter))   | UNAUTHORIZED
        new SignatureException("error")                                     | UNAUTHORIZED
        new InsufficientAuthenticationException("error")                    | UNAUTHORIZED
        new MalformedJwtException("error")                                  | UNAUTHORIZED
        new EntityNotFoundException("error")                                | NOT_FOUND
    }

    def "Handle HttpMessageNotReadableException exception"() {
        given: "Exception is received"
        def ex = new HttpMessageNotReadableException("error msg", new MockHttpInputMessage())

        when: "The handler processes the exception"
        def response = this.handler.handle(ex)

        then: "Error response is correctly returned"
        response.getStatusCode() == BAD_REQUEST
        noExceptionThrown()
    }

    private MethodArgumentNotValidException createMethodArgumentNotValidException() {
        def method = this.getClass().getMethods()[0]
        def fieldError = new FieldError("field", "code", "default")
        def binding = new BeanPropertyBindingResult("1", "someProperty")
        binding.addError(fieldError)
        return new MethodArgumentNotValidException(new MethodParameter(method, -1),
                binding)
    }
}
