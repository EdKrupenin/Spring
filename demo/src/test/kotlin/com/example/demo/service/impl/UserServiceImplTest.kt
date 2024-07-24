package com.example.demo.service.impl

import com.example.demo.model.User
import com.example.demo.repository.UserRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.core.userdetails.User as SecurityUser

@ExtendWith(MockitoExtension::class)
class UserServiceImplTest {
    @InjectMocks
    lateinit var userService: UserServiceImpl

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun testCreate() = runTest {
        val user = User(login = "testUser", password = "Qwerty123!")
        val encodedPassword = "jf2w0jisdlfnweijdf0qjiedsolkfmwepofdjmsd"

        Mockito.`when`(passwordEncoder.encode(user.password)).thenReturn(encodedPassword)

        userService.create(user)

        val argumentCaptor = ArgumentCaptor.forClass(User::class.java)
        Mockito.verify(userRepository).save(argumentCaptor.capture())
        val savedUser = argumentCaptor.value

        Assertions.assertEquals(encodedPassword, savedUser.password)
    }

    @Test
    fun `test getCurrentUser`() = runTest {
        val login = "testUser"
        val password = "password"
        val user = User(login = login, password = password)
        val principal = SecurityUser(login, password, listOf())

        val authentication = Mockito.mock(Authentication::class.java)
        Mockito.`when`(authentication.principal).thenReturn(principal)
        Mockito.`when`(userRepository.findByLogin(login)).thenReturn(user)

        val securityContext = SecurityContextHolder.createEmptyContext()
        securityContext.authentication = authentication
        SecurityContextHolder.setContext(securityContext)

        val result = userService.getCurrentUser()

        Assertions.assertEquals(user, result)
    }

    @Test
    fun `test getCurrentUser not found`() = runTest {
        val login = "testUser"
        val password = "password"
        val principal = SecurityUser(login, password, listOf())

        val authentication = Mockito.mock(Authentication::class.java)
        Mockito.`when`(authentication.principal).thenReturn(principal)
        Mockito.`when`(userRepository.findByLogin(login)).thenReturn(null)

        val securityContext = SecurityContextHolder.createEmptyContext()
        securityContext.authentication = authentication
        SecurityContextHolder.setContext(securityContext)

        val exception = Assertions.assertThrows(UsernameNotFoundException::class.java) {
            runBlocking {
                userService.getCurrentUser()
            }
        }

        Assertions.assertEquals("user not found", exception.message)
    }
}