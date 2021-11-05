package api;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.CarModelDao;
import models.CarModel;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@WebServlet({"/model/get", "/model/list", "/model/update", "/model/create", "/model/delete"})
public class CarModelServlet extends HttpServlet {
    private final CarModelDao carModelDao;
    private final ObjectMapper mapper;

    public CarModelServlet() {
        carModelDao = new CarModelDao();
        mapper = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        if (!req.getMethod().equalsIgnoreCase("get")) {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } else {
            try (var writer = resp.getWriter()) {
                try {
                    resp.setContentType("application/json");

                    switch (req.getServletPath()) {
                        case "/model/get" -> {
                            var id = Integer.parseInt(req.getParameter("id"));
                            var brand = carModelDao.get(id).orElseThrow();
                            writer.print(mapper.writeValueAsString(brand));
                        }
                        case "/model/list" -> {
                            var list = carModelDao.list();
                            writer.print(mapper.writeValueAsString(list));
                        }
                        default -> resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    }
                } catch (NumberFormatException e) {
                    writer.print(String.format("{\"error\": %s}", mapper.writeValueAsString(e.getMessage())));
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } catch (NoSuchElementException e) {
                    writer.print(String.format("{\"error\": %s}", mapper.writeValueAsString(e.getMessage())));
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                } catch (SQLException e) {
                    writer.print(String.format("{\"error\": %s}", mapper.writeValueAsString(e.getMessage())));
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } catch (IOException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        if (!req.getMethod().equalsIgnoreCase("post")) {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        } else {
            try (var writer = resp.getWriter(); var reader = req.getReader()) {
                try {
                    if (!req.getContentType().equals("application/json")) {
                        resp.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                    }
                    resp.setContentType("application/json");

                    switch (req.getServletPath()) {
                        case ("/model/create") -> {
                            var model = mapper.readValue(reader, CarModel.class);
                            var id = carModelDao.save(model);

                            if (id != -1) resp.setStatus(HttpServletResponse.SC_OK);
                            else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                            writer.write(String.format("{\"id\": \"%d\"}", id));
                        }
                        case ("/model/update") -> {
                            var model = mapper.readValue(reader, CarModel.class);
                            if (carModelDao.update(model)) resp.setStatus(HttpServletResponse.SC_OK);
                            else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        }
                        case ("/model/delete") -> {
                            var id = Integer.parseInt(req.getParameter("id"));
                            if (carModelDao.delete(id)) resp.setStatus(HttpServletResponse.SC_OK);
                            else resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        }
                        default -> resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    }
                } catch (DatabindException | NumberFormatException e) {
                    writer.print(String.format("{\"error\": %s}", mapper.writeValueAsString(e.getMessage())));
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                } catch (SQLException e) {
                    writer.print(String.format("{\"error\": %s}", mapper.writeValueAsString(e.getMessage())));
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } catch (IOException e) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
    }
}
