package me.elordenador.imageserver;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name="generateCodeServlet", value="/api/v1/generateCode")
public class GenerateCodeServlet extends HttpServlet {
    class GeneratedToken {
        private String token;
        public GeneratedToken() {
            this.token = Generator.generateCustomToken();
        }
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        GeneratedToken gt = new GeneratedToken();
        Gson gson = new Gson();
        String json = gson.toJson(gt);
        out.println(json);

    }

}
