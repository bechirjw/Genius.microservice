package tn.esprit.projet4arcticback.user_service.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tn.esprit.projet4arcticback.user_service.service.JwtService;

import java.io.IOException;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService tokenProvider;

    private String redirectUri = "http://localhost:4200"; // Default redirect URI


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        var oauthUser = (UserDetails) authentication.getPrincipal();
        var claims = new HashMap<String, Object>();
        claims.put("imageVerified", true);

        // Generate JWT token
        String token = tokenProvider.generateTokenOauth2(oauthUser, claims);

        // Redirect to frontend with token
        String targetUrl = "http://localhost:4200/#/login?token=" + token;

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}