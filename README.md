# spring-oauth2
spring-oauth2

default port: 8082

- oauth/token?grant_type=password&username={username}&password={password}
- oauth/token?grant_type=refresh_token&refresh_token={token}
- admin/{text}?access_token={token}