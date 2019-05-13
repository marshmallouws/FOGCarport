package PresentationLayer;

import data.LogInException;
import entity.Employee;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import logic.LogicFacade;

/**
 *
 * @author caspe
 */
public class LoginCommand extends Command {
    
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        if("logout".equals(request.getParameter("c")) && session.getAttribute("user") != null){
            session.removeAttribute("user");
            response.sendRedirect("");
            return;
        }
        
        LogicFacade logic = new LogicFacade();
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        try {
            Employee user = logic.logIn(username, password);
            
            if (user != null) {
                session.setAttribute("user", user);
                response.sendRedirect("byggecenter?view=backendpage");
            } else {
                response.sendRedirect("");
            }
            
        } catch (LogInException ex) {
            ex.printStackTrace();
        }
        
    }
    
}
