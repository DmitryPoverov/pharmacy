package by.pharmacy.servlet;

import by.pharmacy.dto.MedicineDto;
import by.pharmacy.service.MedicineService;
import by.pharmacy.util.JspHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/admin-medicines")
public class AdminMedicineServlet extends HttpServlet {

    private final MedicineService medicineService = MedicineService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (req.getParameter("medicineAddition")!=null) {
            MedicineDto medicineDto = MedicineDto.builder()
                    .name(req.getParameter("name"))
                    .price(Integer.parseInt(req.getParameter("price")))
                    .country(req.getParameter("country"))
                    .build();
            /*String save = */medicineService.save(medicineDto);
//            req.setAttribute("result", save);
            req.getSession().setAttribute("medicines", medicineService.findAll());
            req.getRequestDispatcher(JspHelper.getPath("admin-medicines")).forward(req, resp);
        }
        else
        {
            MedicineDto medicineDto = MedicineDto.builder()
                    .id(Integer.parseInt(req.getParameter("id")))
                    .name(req.getParameter("name"))
                    .country(req.getParameter("country"))
                    .price(Integer.parseInt(req.getParameter("price")))
                    .build();

            try (PrintWriter writer = resp.getWriter()) {
                String result = medicineService.delete(medicineDto);
                resp.setContentType("text/html");
                writer.write("<p>Result of deleting:</p>");
                writer.write("<p>" + result  + "</p>");

            } catch (Exception e) {
                throw new RuntimeException(e); // подумать над этим местом
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var requestDispatcher = req.getRequestDispatcher(JspHelper.getPath("admin-medicines"));

        req.getSession().setAttribute("medicines", medicineService.findAll());
        requestDispatcher.forward(req, resp);
    }
}