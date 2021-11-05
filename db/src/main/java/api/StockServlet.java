package api;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dao.StockDao;
import models.Stock;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;

@WebServlet({"/stock/get", "/stock/list", "/stock/update", "/stock/create", "/stock/delete"})
public class StockServlet extends HttpServlet {
    private final StockDao stockDao;
    private final ObjectMapper mapper;

    public StockServlet() {
        stockDao = new StockDao();
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
                        case "/stock/get" -> {
                            var id = Integer.parseInt(req.getParameter("id"));
                            var stock = stockDao.get(id).orElseThrow();
                            writer.print(mapper.writeValueAsString(stock));
                        }
                        case "/stock/list" -> {
                            var list = stockDao.list();
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
                        case ("/stock/create") -> {
                            var stock = mapper.readValue(reader, Stock.class);
                            var id = stockDao.save(stock);

                            if (id != -1) resp.setStatus(HttpServletResponse.SC_OK);
                            else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                            writer.write(String.format("{\"id\": \"%d\"}", id));
                        }
                        case ("/stock/update") -> {
                            var stock = mapper.readValue(reader, Stock.class);
                            if (stockDao.update(stock)) resp.setStatus(HttpServletResponse.SC_OK);
                            else resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        }
                        case ("/stock/delete") -> {
                            var id = Integer.parseInt(req.getParameter("id"));
                            if (stockDao.delete(id)) resp.setStatus(HttpServletResponse.SC_OK);
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
