package com.svmall.oauth.config;

/**
 * @author zlf
 * @data 2023/5/27
 * @@description
 */
import com.svmall.oauth.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * /oauth/authorize：授权端点
 * /oauth/token：令牌端点
 * /oauth/confirm_access：用户确认授权提交端点
 * /oauth/error：授权服务错误信息端点
 * /oauth/check_token：用于资源服务访问的令牌解析端点
 * /oauth/token_key：提供公有密匙的端点，如果你使用 JWT 令牌的话
 *
 * @author liuyalong
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * Refresh Token 时需要自定义实现，否则抛异常 <br>
     * Lazy 注解是为了防止循环注入（is there an unresolvable circular reference?）
     */
    @Autowired
    private UserDetailsService  userDetailsService;

    @Autowired
    private JwtTokenStore jwtTokenStore;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;


    @Autowired
    private UserApprovalHandler userApprovalHandler;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Value("${auth.clientId}")
    private String clientId;
    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory().withClient(clientId).secret(this.passwordEncoder.encode(clientSecret))
                .authorizedGrantTypes("password")
                .scopes("all");

//        clients.inMemory()
//                // 客户端id
//                .withClient("client_id")
//                //客户端密码
//                .secret("secret")
//                //授权类型
//                .authorizedGrantTypes(
//                        "password",
//                        "authorization_code",
//                        "client_credentials",
//                        "refresh_token",
//                        "implicit"
//                )
//                .scopes("all")
//                .resourceIds("oauth2-resource")
//                .accessTokenValiditySeconds(60 * 60 * 12)
//                //0表示不过期
//                .refreshTokenValiditySeconds(0)
//                //重定向地址,返回code使用
//                .redirectUris("http://baidu.com");
    }

    /**
     * checkTokenAccess 权限设置为isAuthenticated，不然资源服务器 来请求403
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer.allowFormAuthenticationForClients()
                .passwordEncoder(new BCryptPasswordEncoder())
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }



    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                // 这里必须配置userDetailsService,不然不能用refresh_token
                .userDetailsService(userDetailsService)
                .userApprovalHandler(userApprovalHandler)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                //巨坑,花了三天时间,这行必须加上,不然不会生成jwt令牌,只会生成普通令牌
                .accessTokenConverter(jwtAccessTokenConverter)
                .tokenStore(jwtTokenStore);
    }

}