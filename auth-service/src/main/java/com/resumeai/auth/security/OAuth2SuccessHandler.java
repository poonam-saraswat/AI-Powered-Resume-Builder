package com.resumeai.auth.security;

import com.resumeai.auth.dto.AuthDtos.TokenResponse;
import com.resumeai.auth.entity.AuthProvider;
import com.resumeai.auth.entity.User;
import com.resumeai.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component @RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AuthService authService;

    @Value("${app.frontend.redirect-uri:http://localhost:4200/oauth/callback}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) auth;
        String regId = token.getAuthorizedClientRegistrationId(); // "google" or "github"
        OAuth2User principal = token.getPrincipal();

        String email; String name; String avatar; String providerId;
        if ("google".equals(regId)) {
            email = principal.getAttribute("email");
            name = principal.getAttribute("name");
            avatar = principal.getAttribute("picture");
            providerId = principal.getAttribute("sub");
        } else { // github
            email = principal.getAttribute("email");
            if (email == null) email = principal.getAttribute("login") + "@users.noreply.github.com";
            name = principal.getAttribute("name");
            if (name == null) name = principal.getAttribute("login");
            avatar = principal.getAttribute("avatar_url");
            Object id = principal.getAttribute("id");
            providerId = id == null ? null : id.toString();
        }

        AuthProvider provider = "google".equals(regId) ? AuthProvider.GOOGLE : AuthProvider.GITHUB;
        User u = authService.upsertOAuthUser(provider, providerId, email, name, avatar);
        TokenResponse tokens = authService.issueTokens(u);

        String url = redirectUri
                + "?accessToken=" + URLEncoder.encode(tokens.accessToken(), StandardCharsets.UTF_8)
                + "&refreshToken=" + URLEncoder.encode(tokens.refreshToken(), StandardCharsets.UTF_8);
        getRedirectStrategy().sendRedirect(req, res, url);
    }
}
