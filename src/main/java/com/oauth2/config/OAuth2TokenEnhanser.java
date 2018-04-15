package com.oauth2.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class OAuth2TokenEnhanser implements TokenEnhancer {
	
	@Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> extraInfos = new HashMap<>();
        extraInfos.put("extra", authentication.getName());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(extraInfos);
        return accessToken;
    }
}