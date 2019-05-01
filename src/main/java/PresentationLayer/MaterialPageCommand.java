package PresentationLayer;

import data.OrderMapper;
import entity.Order;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import logic.LogicFacade;

/**
 *
 * @author vl48
 */
public class MaterialPageCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cmd = request.getParameter("c");
        if(cmd==null||cmd.isEmpty()){
            request.getRequestDispatcher("/WEB-INF/materials.jsp").forward(request, response);
            return;
        }
    
    LogicFacade logic = new LogicFacade();
    response.setContentType("text/plain");
    response.setCharacterEncoding("UTF-8"); 
    String json;
        switch(cmd){
            case "categories":
                json=logic.getCategories();
                break;
                
            case "products":
                json=logic.getProductList(request.getParameter("id"));
                break;
                
            case "product":
                json=logic.getProduct(request.getParameter("id"));
                break;
                
            case "save":
                json=logic.saveProduct(request.getParameter("product"));
                break;
                
            default:
                json = "error";
                break;
        }
        
        response.getWriter().write(json);  
    }
    
}
