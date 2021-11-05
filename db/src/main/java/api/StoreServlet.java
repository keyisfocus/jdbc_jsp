package api;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.StoreDao;
import models.Store;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@WebServlet({"/store/get", "/store/list", "/store/update", "/store/create", "/store/delete"})
public class StoreServlet extends HttpServlet {
    private final StoreDao storeDao;
    private final ObjectMapper mapper;

    public StoreServlet() {
        storeDao = new StoreDao();
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
                        case "/store/get" -> {
                            var id = Integer.parseInt(req.getParameter("id"));
                            var store = storeDao.get(id).orElseThrow();
                            writer.print(mapper.writeValueAsString(store));
                        }
                        case "/store/list" -> {
                            var list = storeDao.list();
                            writer.print(mapper.writeValueAsString(list));
                        }
                        default -> {
                            writer.print("{\"error\": \"Method not supported\"}");
                            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        }
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
                        case ("/store/create") -> {
                            var store = mapper.readValue(reader, Store.class);
                            var id = storeDao.save(store);

                            if (id != -1) resp.setStatus(HttpServletResponse.SC_OK);
                            else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                            writer.write(String.format("{\"id\": \"%d\"}", id));
                        }
                        case ("/store/update") -> {
                            var store = mapper.readValue(reader, Store.class);
                            if (storeDao.update(store)) resp.setStatus(HttpServletResponse.SC_OK);
                            else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        }
                        case ("/store/delete") -> {
                            var id = Integer.parseInt(req.getParameter("id"));
                            if (storeDao.delete(id)) resp.setStatus(HttpServletResponse.SC_OK);
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
