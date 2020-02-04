package com.wizaord.moneyweb.configuration.security

import com.wizaord.moneyweb.services.JwtService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationFilter(
        @Autowired val jwtService: JwtService
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(javaClass.canonicalName)

    override fun doFilterInternal(httpServletRequest: HttpServletRequest,
                                  httpServletResponse: HttpServletResponse,
                                  filterChain: FilterChain) {


        val jwtFromRequest = getJwtFromRequest(httpServletRequest)
        if (jwtFromRequest != null && this.jwtService.isTokenValid(jwtFromRequest)) {
            val username = jwtService.getUsernameFromToken(jwtFromRequest)
            val userId = jwtService.getUserIdFromToken(jwtFromRequest)

            val userAuthenticated = UserAuthenticated(userId, username)

            val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(userAuthenticated, null, userAuthenticated.authorities)
            usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
            SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse)

    }

    fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization").orEmpty()
        return if (bearerToken.isNotEmpty() && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7, bearerToken.length)
        } else null
    }
}


data class UserAuthenticated(
        var userId: String,
        var usern: String
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority("USER"))
    override fun isEnabled(): Boolean = true
    override fun getUsername(): String = this.usern
    override fun isCredentialsNonExpired(): Boolean = true
    override fun getPassword(): String = ""
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
}