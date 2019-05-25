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
public class MaterialPageCommand extends Command {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, FOGException {
        if(request.getSession().getAttribute("user")==null)throw new FOGException("Du skal være logget ind for at tilgå denne side.");
        String cmd = request.getParameter("c");
        if (cmd == null || cmd.isEmpty()) {
            request.getRequestDispatcher("/WEB-INF/materials.jsp").forward(request, response);
            return;
        }

        LogicFacade logic = new LogicFacade();
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        String json;
        switch (cmd) {
            case "categories":
                json = logic.getCategories();
                break;

            case "products":
                json = logic.getProductVariantsList(request.getParameter("categoryID"), request.getParameter("productID"));
                break;

            case "productsInCat":
                json = logic.getProductsInCategories(request.getParameter("categoryID"));
                break;

            case "productVariant":
                json = logic.getProductVariant(request.getParameter("id"));
                break;

            case "productMain":
                json = logic.getProductMain(request.getParameter("id"));
                break;

            case "save":
                json = logic.updateProductVariant(request.getParameter("product"));
                break;

            case "create":

                if ("variant".equals(request.getParameter("type"))) {
                    json = logic.createProductVariant(request.getParameter("product"));
                    break;
                } else {
                    json = logic.createProduct(request.getParameter("product"));
                    break;
                }

            default:
                json = "error";
                break;
        }
        response.getWriter().write(json);
    }

}
