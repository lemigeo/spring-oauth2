package com.oauth2.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;

import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import com.oauth2.service.Oauth2UserDetailsService;

@Configuration
@EnableAuthorizationServer
public class AuthConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;
	
	@Autowired
    private ClientDetailsService clientDetailsService;
	
	@Autowired
	private TokenStore tokenStore;
	
    @Autowired
    private Oauth2UserDetailsService userDetailsService;
    
	@Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
          .withClient("clientId")
          .secret("secret")
          .autoApprove(false)
          .authorizedGrantTypes("authorization_code", "implicit", "password", "refresh_token")
          .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")	
          .scopes("read", "write")
          .accessTokenValiditySeconds(3600)  //1 hour
          .refreshTokenValiditySeconds(2592000); //30 days
    }
 
	@Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer()));
        endpoints
        		.tokenStore(tokenStore)
        		.tokenEnhancer(tokenEnhancerChain)
        		.userApprovalHandler(userApprovalHandler(tokenStore))
        		.userDetailsService(userDetailsService)
            .authenticationManager(authenticationManager);
    }

    @Bean(value="tokenStore")
    public TokenStore tokenStore() {
    		return new InMemoryTokenStore();
    }
    
    @Bean(value="tokenEnhancer")
    public TokenEnhancer tokenEnhancer() {
        return new OAuth2TokenEnhanser();
    }
    
    @Bean(value="userApprovalHandler")
    public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore){
        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
        handler.setTokenStore(tokenStore);
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
        handler.setClientDetailsService(clientDetailsService);
        return handler;
    }
}
