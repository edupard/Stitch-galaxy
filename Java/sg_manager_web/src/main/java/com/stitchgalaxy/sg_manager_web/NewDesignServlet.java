/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.sg_manager_web.data.ProductRef;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author tarasev
 */
public class NewDesignServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String destination = "/new_design.jsp";

        request.setAttribute("id", UUID.randomUUID());
        
        RequestDispatcher rd = getServletContext().getRequestDispatcher(destination);
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sId = request.getParameter("id");
        String sDate = request.getParameter("date");
        String sPriceUsd = request.getParameter("price_usd");
        String sSales = request.getParameter("sales");
        String sRating = request.getParameter("rating");
        String sRates = request.getParameter("rates");
        String sName = request.getParameter("name");
        String sDescription = request.getParameter("description");
        String sAuthor = request.getParameter("author");
        String sAuthorUri = request.getParameter("author_uri");
        String sTranslator = request.getParameter("translator");
        String sTranslatorUri = request.getParameter("translatorUri");
        String sComplexity = request.getParameter("complexity");
        String sAvgColor = request.getParameter("avg_color");
    }


    @Override
    public String getServletInfo() {
        return "SG manager new design servlet";
    }

}