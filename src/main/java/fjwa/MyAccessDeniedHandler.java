package fjwa;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by bilbowm on 15/10/2015.
 */
public class MyAccessDeniedHandler extends AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private String errorPage;

    public MyAccessDeniedHandler() {
        errorPage = "/403.html";
    }

    public MyAccessDeniedHandler(String errorPage) {
        this.errorPage = errorPage;
    }

    public String getErrorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException)
            throws IOException, ServletException {

        //do some business logic, then redirect to errorPage url
        response.sendRedirect(errorPage);

    }


}

