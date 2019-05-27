package PresentationLayer;

import data.FOGException;
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
public class SVGCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException {
        String id = request.getParameter("order");
        if (id == null || id.isEmpty()) {
            throw new FOGException("Du skal være logget ind for at tilgå denne side.");
        }
        
        try {
        LogicFacade lf = new LogicFacade();
        Order order = lf.getOrder(Integer.parseInt(id));
        request.setAttribute("order", order);
        request.getRequestDispatcher("/WEB-INF/svg.jsp").forward(request, response);
        } catch(Exception e){
            throw new FOGException("Kunne ikke vise carport tegning.");
        }
    }

}
