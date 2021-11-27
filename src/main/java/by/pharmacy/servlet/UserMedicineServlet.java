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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/user-medicines")
public class UserMedicineServlet extends HttpServlet {

    private final MedicineService medicineService = MedicineService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String[] parameterValues = req.getParameterValues("medicineId");
        String[] arrayQuantity = req.getParameterValues("quantity");

        ArrayList<Integer> totalPriceEachMedicine = new ArrayList<>();
        int totalPrice = 0;

        ArrayList<String> quantity = new ArrayList<>();
        for (String s : arrayQuantity) {
            if (!s.equals("0")) {
                quantity.add(s);
            }
        }

        List<MedicineDto> medicineDtos = new ArrayList<>();
        for (String s : parameterValues) {
            medicineDtos.add(medicineService.findById(Integer.parseInt(s)));
        }

        int t;
        for (int i = 0; i < quantity.size(); i++) {
            t = Integer.parseInt(quantity.get(i)) * medicineDtos.get(i).getPrice();
            totalPriceEachMedicine.add(t);
            totalPrice+=t;
        }

        req.getSession().setAttribute("medicineDtos" , medicineDtos);
        req.getSession().setAttribute("quantity", quantity);
        req.getSession().setAttribute("totalPriceEachMedicine", totalPriceEachMedicine);
        req.getSession().setAttribute("totalPrice", totalPrice);
        req.getRequestDispatcher(JspHelper.getPath("user-medicine-cart")).forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String request = req.getParameter("request");
        req.setAttribute("request", request);
        req.getSession().setAttribute("medicines", medicineService.findAll());
        req.getRequestDispatcher(JspHelper.getPath("user-medicines")).forward(req, resp);
    }
}
