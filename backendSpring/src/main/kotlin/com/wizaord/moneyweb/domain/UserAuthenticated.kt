package com.wizaord.moneyweb.domain

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

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

