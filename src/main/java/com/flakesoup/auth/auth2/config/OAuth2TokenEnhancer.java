package com.flakesoup.auth.auth2.config;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class OAuth2TokenEnhancer extends JwtAccessTokenConverter implements Serializable {
    private static int authenticateCodeExpiresTime = 10 * 60;

    private static final String TOKEN_SEG_USER_ID = "X-Flake-UserId";
    private static final String TOKEN_SEG_USER_MOBILE = "X-Flake-Mobile";
    private static final String TOKEN_SEG_USER_NAME = "X-Flake-UserName";
    private static final String TOKEN_SEG_USER_EXT = "X-Flake-UserExt";
    private static final String TOKEN_SEG_CLIENT_ID = "X-Flake-ClientId";

    public OAuth2TokenEnhancer() {

    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
                                     OAuth2Authentication authentication) {
        OAuth2User userDetails = (OAuth2User) authentication.getPrincipal();
        authentication.getUserAuthentication().getPrincipal();
        Map<String, Object> info = new HashMap<>();
        info.put(TOKEN_SEG_USER_ID, userDetails.getUserid());
        info.put(TOKEN_SEG_USER_MOBILE, userDetails.getMobile());
        info.put(TOKEN_SEG_USER_NAME, userDetails.getUsername());
        info.put(TOKEN_SEG_USER_EXT, userDetails.getUserext());
        // client id
        info.put(TOKEN_SEG_CLIENT_ID, authentication.getOAuth2Request().getClientId());

        DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
        customAccessToken.setAdditionalInformation(info);

        OAuth2AccessToken enhancedToken = super.enhance(customAccessToken, authentication);
        return enhancedToken;
    }

}
