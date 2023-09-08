package com.company.jmixpm.app;

import com.company.jmixpm.security.FullAccessRole;
import io.jmix.core.session.SessionData;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class AppAuthenticationSuccessListener {

    private final ObjectProvider<SessionData> sessionDataProvider;

    public AppAuthenticationSuccessListener(ObjectProvider<SessionData> sessionDataProvider) {
        this.sessionDataProvider = sessionDataProvider;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent event) {
        SessionData sessionData = sessionDataProvider.getIfAvailable();
        if (sessionData == null) {
            return;
        }

        Authentication authentication = event.getAuthentication();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals(FullAccessRole.CODE)) {
                sessionData.setAttribute("isManager", true);
                return;
            }
        }
        sessionData.setAttribute("isManager", false);
    }
}
