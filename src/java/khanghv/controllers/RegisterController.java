/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package khanghv.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import khanghv.utils.EncryptCodeSHA256;
import khanghv.dtos.AccountDTO;
import khanghv.models.AccountDAO;

/**
 *
 * @author USER
 */
public class RegisterController extends HttpServlet {

    private final static String INVALID = "register.jsp";
    private final static String ERROR = "error.jsp";
    private final static String SUCCESS = "SendMailController";

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String url = ERROR;
        try {
            boolean isValid = true;
            String email = request.getParameter("txtEmail");
            String extend = request.getParameter("txtExtend");
            String password = request.getParameter("txtPassword");
            String fullName = request.getParameter("txtName");
            if (!email.matches("[A-Za-z0-9_.]{6,35}")) {
                isValid = false;
                request.setAttribute("EERR", "Invalid email address");
            }
            if (!password.matches("[A-Za-z0-9]{8,20}")) {
                isValid = false;
                request.setAttribute("PERR", "Invalid Password!");
            }
            if (!fullName.matches("[A-Za-z ]{8,50}")) {
                isValid = false;
                request.setAttribute("FERR", "Invalid Full Name!");
            }
            if (isValid) {
                String fullEmail = email + extend;
                String encryptedPass = EncryptCodeSHA256.toHexString(EncryptCodeSHA256.getSHA(password));
                String rootLink = "https://khanghv.blob.core.windows.net/khanghv/";
                String img = rootLink + "/Trang.jpg";
                Random rand = new Random();
               
                int getRandomImg = rand.nextInt(10);
                switch (getRandomImg) {
                    case 0:
                        img = rootLink + "Cam.jpg";
                        break;
                    case 1:
                        img = rootLink + "Den.jpg";
                        break;
                    case 2:
                        img = rootLink + "Do.jpg";
                        break;
                    case 3:
                        img = rootLink + "LaDam.jpg";
                        break;
                    case 4:
                        img = rootLink + "Nau.jpg";
                        break;
                    case 5:
                        img = rootLink + "Tim.jpg";
                        break;
                    case 6:
                        img = rootLink + "Trang.jpg";
                        break;
                    case 7:
                        img = rootLink + "Vang.jpg";
                        break;
                    case 8:
                        img = rootLink + "XanhLa.jpg";
                        break;
                    case 9:
                        img = rootLink + "XanhNgoc.jpg";
                        break;
                    default:
                        img = rootLink + "Den.jpg";
                        break;
                }
                String status = "new";
                String role = "member";
                AccountDTO dto = new AccountDTO(fullEmail, fullName, role, status, img);
                dto.setPassword(encryptedPass);
                AccountDAO dao = new AccountDAO();
                boolean isOk = dao.registerAccount(dto);
                if (isOk) {
                    url = SUCCESS;
                    request.setAttribute("OK", "Register Successfully! Please check your email to verify your account!");
                    request.setAttribute("LIAME", fullEmail);
                } else {
                    request.setAttribute("ERROR", "Register failed!");
                }

            } else {
                url = INVALID;
                request.setAttribute("EMAIL", email);
                request.setAttribute("FN", fullName);
            }
        } catch (Exception e) {
            if (e.getMessage().contains("duplicate")) {
                url = INVALID;
                request.setAttribute("EERR", "Existed Email!");
            } else {
                log("ERROR at RegisterController: " + e.getMessage());

            }

        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
