package Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebFilter(filterName = "ServiceFilter")
public class ServiceFilter implements Filter {
    public void destroy() { }
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        Map<String,Object> loginData  =(Map<String, Object>)request.getSession().getAttribute("loginData");
        if(loginData==null)
        {
            response.sendError(400,"未登录");
        }
        else
        {
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) {

    }

}
