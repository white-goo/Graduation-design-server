package white.goo.config.shiro;

import org.apache.shiro.authc.AuthenticationToken;

public class JwtToken implements AuthenticationToken {

    private String jwtToken;

    public JwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    //这两个方法的返回值于Realm中InFo的第二个参数要对应
    public Object getPrincipal() {
        return jwtToken;
    }

    public Object getCredentials() {
        return jwtToken;
    }
}
