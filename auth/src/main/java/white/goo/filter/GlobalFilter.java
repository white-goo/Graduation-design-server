package white.goo.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import org.springframework.stereotype.Component;
import white.goo.config.RepeatedlyReadRequestWrapper;
import white.goo.core.SecurityManager;
import white.goo.dto.R;
import white.goo.util.SpringUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter("/*")
public class GlobalFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RepeatedlyReadRequestWrapper requestWrapper = new RepeatedlyReadRequestWrapper((HttpServletRequest) request);
        SecurityManager securityManager = (SecurityManager) SpringUtil.getBean("securityManager");
        if (securityManager.doValidate(requestWrapper)) {
            chain.doFilter(requestWrapper, response);
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().println(JSON.toJSONString(R.error("无权访问")));
            response.getWriter().flush();
            response.getWriter().close();
        }
    }
}
