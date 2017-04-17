package hu.bvillei.todolist.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import hu.bvillei.todolist.model.RoleType;

@Service
public class AuthorizationService {

	public String getLoggedInUsername() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	public boolean hasRole(RoleType role) {
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		for (GrantedAuthority auth : authorities) {
			if (auth.getAuthority().equals(role.name())) {
				return true;
			}
		}
		return false;
	}
}
