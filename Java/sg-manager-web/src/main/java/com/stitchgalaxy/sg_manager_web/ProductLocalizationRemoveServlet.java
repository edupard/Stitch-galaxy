/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.service.DomainDataService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

/**
 *
 * @author tarasev
 */
@WebServlet("/product-localization-remove")
public class ProductLocalizationRemoveServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String locale = request.getParameter("locale");
            Long productId = Long.parseLong(request.getParameter("product"));
            DomainDataService.removeProductLocalization(productId, locale);
        } catch (Exception e) {
            ErrorHandler errorHandler = new ErrorHandler(e, request, response, this);
            errorHandler.process();
            return;
        }
        response.sendRedirect(String.format("%s%s?product=%s", request.getContextPath(), "/product-view", request.getParameter("product")));

    }
}