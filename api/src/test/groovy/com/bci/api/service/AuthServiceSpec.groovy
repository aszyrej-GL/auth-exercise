package com.bci.api.service

import com.bci.api.dto.PhoneDto
import com.bci.api.dto.requests.UserRequestDto
import com.bci.api.mappers.UserMapper
import com.bci.api.model.Phone
import com.bci.api.model.Token
import com.bci.api.model.User
import com.bci.api.repository.UsersRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.security.crypto.password.PasswordEncoder
import spock.lang.Specification

import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException
import java.time.LocalDateTime


class AuthServiceSpec extends Specification {

    UsersRepository usersRepository = Mock(UsersRepository)
    UserMapper userMapper = Mock(UserMapper)
    PasswordEncoder passwordEncoder = Mock(PasswordEncoder)
    JwtService jwtService = Mock(JwtService)
    private AuthService authService

    private UserRequestDto validUserRequestDto
    private User validUser
    private String uuid

    void setup() {
        this.authService = new AuthService(usersRepository, userMapper, passwordEncoder, jwtService)

        this.uuid = "123"

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
                "uuid",
                "Test User",
                "testuser@email.com",
                "encryptedPass",
                now,
                now,
                phoneSet,
                token,
                true
        )


        this.validUserRequestDto = new UserRequestDto(
                "Test User",
                "testuser@email.com",
                "T3stus3r",
                phoneList
        )
    }

    def "Sign-up finishes OK"() {
        when: "Calling the service to sign up an user"
        def response = this.authService.createUser(validUserRequestDto)

        then: "User is created and saved with new JWT token"
        1 * this.userMapper.toEntity(validUserRequestDto, passwordEncoder) >> validUser
        1 * this.jwtService.generateToken(validUser) >> "token"
        1 * this.usersRepository.save(_)
        validUser.getToken().getValue() == "token"
        1 * this.userMapper.toSignInDto(_)
        noExceptionThrown()
    }

    def "Sign-up returns EntityExistsException for duplicates"() {
        when: "Calling the service to sign up a duplicated user"
        def response = this.authService.createUser(validUserRequestDto)

        then: "Entity exists exception is thrown"
        1 * this.userMapper.toEntity(validUserRequestDto, passwordEncoder) >> validUser
        1 * this.jwtService.generateToken(validUser) >> "token"
        1 * this.usersRepository.save(_) >> {throw new DataIntegrityViolationException("error")}
        thrown(EntityExistsException)
    }

    def "Login finishes OK"() {
        when: "Calling the service to get an user"
        def response = this.authService.getUser(uuid)

        then: "User is retrieved and new JWT is generated"
        1 * this.usersRepository.findByUuid(uuid) >> Optional.of(validUser)
        1 * this.jwtService.generateToken(validUser) >> "newToken"
        1 * this.usersRepository.save(_)
        validUser.getToken().getValue() == "newToken"
        1 * this.userMapper.toLogInDto(_)
        noExceptionThrown()
    }

    def "Login throws exception when user does not exist"() {
        when: "Calling the service to sign up an user"
        def response = this.authService.getUser(uuid)

        then: "User is created and JWT is returned with 200 OK"
        1 * this.usersRepository.findByUuid(uuid) >> Optional.empty()
        thrown(EntityNotFoundException)
    }

}
