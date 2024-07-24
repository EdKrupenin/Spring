package com.example.demo.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.springframework.http.HttpStatus
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.security.core.Authentication

@Component
class CustomAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        handle(request, response, authentication)
        clearAuthenticationAttributes(request)
    }

    protected fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        response.status = HttpStatus.OK.value()
        response.writer.flush()
        response.writer.close()
    }

    protected fun clearAuthenticationAttributes(request: HttpServletRequest) {
        val session: HttpSession? = request.getSession(false)
        session?.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)
    }
}
