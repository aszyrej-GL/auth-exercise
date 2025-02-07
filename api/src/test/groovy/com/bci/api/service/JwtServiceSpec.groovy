package com.bci.api.service

import com.bci.api.dto.PhoneDto
import com.bci.api.model.Phone
import com.bci.api.model.Token
import com.bci.api.model.User
import spock.lang.Specification
import org.springframework.security.authentication.InsufficientAuthenticationException

import java.time.LocalDateTime


class JwtServiceSpec extends Specification {

    private JwtService jwtService

    private String uuid
    private String token
    private String invalidSignatureToken
    private String noClaimToken
    private User validUser

    void setup() {
        final String secretKey = "2e4a460f18d4d5a6aa78f401d5422c99370e747ada3f48baa018b111409453fcec0f1760fb459c53b6b7d8374d5306aa2b05f3c64f552e32492d62a8c9aa2c5143a4c7dc5ce928a9ab8b270ebbcc9a7099553cb40fe2ce01373d2bc60a010f8a25e4897c84cc10dcab0af6c5ca3033e997c1934b0b1b315e1432dcb931af670016495c5b6b258e0f0f5cb493f84178ef647b6d0972100818fea6412a21978a385cae999702a8cc055b1f7b98b6d80cc2d41fb48bb66146f10d930047bd5fa24072753bfa0f0a7ceb6d163fc4e412b304ee1442159eb2108b2e02c51c3fcfe02156f614133db61e19ca68e3c446e04424ffa5f299d34e4fd87b5501d25502acfc9ef0b22aac4a89d5e320d672595e7883d089d40f3f4930811f87a71936245631376b380dd4317b245a1bb86c70248e3f36cfbf1272be3db375cabac878e96da13bea5cf0fb9f4e0386cef74396b31187d6abce8a1b0868e3864eb97f77338f3e03fe64d6e3ad0e6adac2b150604ca153c37292ffeeb59af7b847c62e9596f04d24182433d665c49dd41fbf8b646a13fbb8ae490b5b8260e63796b60c5776bd61f5582132509dee0bfda0f8d911a49467a49e53493119e19ed2bb5fe5d309a33102fa21c10b24e74e46ceb3fa67ee9c6a7e8b431dde1daa6ca1a42f7ceb4298b439e52244f82d92f14a9d7cf10af22f582549606f980ee941d5d10a49549e5818"
        this.jwtService = new JwtService(secretKey)

        this.uuid = "efe0dc12-2daf-4ed8-a3d7-e503a8b56332"
        this.token = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJlZmUwZGMxMi0yZGFmLTRlZDgtYTNkNy1lNTAzYThiNTYzMzIiLCJ1dWlkIjoiZWZlMGRjMTItMmRhZi00ZWQ4LWEzZDctZTUwM2E4YjU2MzMyIiwic3ViIjoidGVzdHVzZXJAZW1haWwuY29tIiwiaWF0IjoxNzM4OTQyMjE0fQ.yh0VPSzHgwNLZXdbUBmJRrzYzCxE1x72O3ZZ1CFYma_LhRyYAHirD9CtSeI8vKH_trf6laIicUd6xGFaxWDkjQ"
        this.invalidSignatureToken = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiIxN2FhYmJhNS00Yjc3LTRhM2MtYTc4MC03MTU3MWUwZmQyNzAiLCJ1dWlkIjoiMTdhYWJiYTUtNGI3Ny00YTNjLWE3ODAtNzE1NzFlMGZkMjcwIiwic3ViIjoidGVzdHVzZXJAZW1haWwuY29tIiwiaWF0IjoxNzM4OTQzNzA5fQ.KdzLEMuHnKiLkakzJIASNOSWBRborrfRZTnAbityz4kmiHi8x64CErwtmDA-r7ysJaIGp_DSICpVUNZXpHypBw"
        this.noClaimToken = "eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJlM2UwNWVkMS0wYjNiLTQxMjAtODQ3Zi03NDM3NTkyYTdlNjgiLCJzdWIiOiJ0ZXN0dXNlckBlbWFpbC5jb20iLCJpYXQiOjE3Mzg5NDM5MDN9.c52h0LOBKwDNCg5bBiseXqRnBMIiB0NsnkpxxVFR2AJKlzboRcKwgmlXvwdkVpws9w7qQ7DaBOZc_WdDOBmwFg"

        PhoneDto phoneDto = new PhoneDto(123, 123, "AR")
        PhoneDto otherDto = new PhoneDto(456, 456, "US")
        List<PhoneDto> phoneList = new ArrayList<>();
        phoneList.add(phoneDto)
        phoneList.add(otherDto)

        Phone phone = new Phone(1, validUser, 123, 123, "AR")
        Phone otherPhone = new Phone(2, validUser, 456, 456, "US")
        Set<Phone> phoneSet = new HashSet<>()
        phoneSet.add(phone);
        phoneSet.add(otherPhone);
        def token = new Token(1, validUser, "token")

        def now = LocalDateTime.now()

        this.validUser = new User(1,
                uuid,
                "Test".repeat(2),
                "testuser@email.com",
                "encryptedPass",
                now,
                now,
                phoneSet,
                token,
                true
        )
    }

    def "Retrieve UUID from token finishes OK"() {
        when: "Calling the service to retrieve UUID"
        def response = this.jwtService.getUserUuidFromAuthHeader("Bearer " + token)

        then: "UUID is retrieved successfully"
        response == uuid
        noExceptionThrown()
    }

    def "Retrieve UUID fails if auth header is not valid"() {
        when: "Calling the service to retrieve UUID"
        def response = this.jwtService.getUserUuidFromAuthHeader(token)

        then: "Exception is thrown"
        thrown(InsufficientAuthenticationException)
    }

    def "Retrieve UUID fails if token is signed with a different key"() {
        when: "Calling the service to retrieve UUID"
        def response = this.jwtService.getUserUuidFromAuthHeader(invalidSignatureToken)

        then: "Exception is thrown"
        thrown(InsufficientAuthenticationException)
    }

    def "Retrieve UUID fails if uuid claim is not present"() {
        when: "Calling the service to retrieve UUID"
        def response = this.jwtService.getUserUuidFromAuthHeader("Bearer " + noClaimToken)

        then: "Exception is thrown"
        thrown(InsufficientAuthenticationException)
    }

}
