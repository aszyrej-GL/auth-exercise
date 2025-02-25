package com.bci.api.controller

import com.bci.api.dto.PhoneDto
import com.bci.api.dto.requests.UserRequestDto
import com.bci.api.service.AuthService
import com.bci.api.service.JwtService
import org.springframework.http.HttpStatus
import spock.lang.Specification

class AuthControllerSpec extends Specification {

    AuthService authService = Mock()
    JwtService jwtService = Mock()
    private AuthController authController

    private UserRequestDto validUserRequestDto
    private String requestHeader
    private String uuid

    void setup() {
        this.authController = new AuthController(authService, jwtService)

        this.uuid = "123"

        PhoneDto phoneDto = new PhoneDto(123, 123, "AR")
        PhoneDto otherDto = new PhoneDto(456, 456, "US")
        List<PhoneDto> phoneList = new ArrayList<>();
        phoneList.add(phoneDto)
        phoneList.add(otherDto)

        this.validUserRequestDto = new UserRequestDto(
                "Test User",
                "testuser@email.com",
                "T3stus3r",
                phoneList
        )

        this.requestHeader = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJqdGkiOiI4NDM2OGNlNS02ZjBmLTRhZDktOWYzYi1lYjMwNTY0NzAwNzI" +
                "iLCJ1dWlkIjoiODQzNjhjZTUtNmYwZi00YWQ5LTlmM2ItZWIzMDU2NDcwMDcyIiwic3ViIjoidGVzdHVzZXJAZW1haWwuY29tIiw" +
                "iaWF0IjoxNzM4ODkxMzgxfQ.tazcUvxavwuvKwNzVWlMecr2P98AvZra51uOOctZXAcpaDGpbDJifPUl41vLogcx"

        this.uuid = "123"
    }

    def "Sign-up finishes OK"() {
        when: "Calling the service to sign up an user"
        def response = this.authController.signUp(validUserRequestDto)

        then: "User is created and JWT is returned with 200 OK"
        1 * this.authService.createUser(validUserRequestDto)
        response.getStatusCode() == HttpStatus.CREATED
    }

    def "Login finishes OK"() {
        when: "Calling the service to retrieve an user"
        def response = this.authController.login(requestHeader)

        then: "User is created and JWT is returned with 200 OK"
        1 * this.jwtService.getUserUuidFromAuthHeader(requestHeader) >> uuid
        1 * this.authService.getUser(uuid)
        response.getStatusCode() == HttpStatus.OK
    }

}
