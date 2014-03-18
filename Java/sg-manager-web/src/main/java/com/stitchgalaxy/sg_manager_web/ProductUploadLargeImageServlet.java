/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stitchgalaxy.sg_manager_web;

import com.stitchgalaxy.dto.CommandUploadProductFile;
import com.stitchgalaxy.dto.FileType;
import com.stitchgalaxy.service.DomainDataService;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author tarasev
 */
@WebServlet("/product-upload-large-image")
@MultipartConfig
public class ProductUploadLargeImageServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long productId = Long.parseLong(request.getParameter("product"));
            Part filePart = request.getPart("file");
            InputStream filecontent = filePart.getInputStream();
            CommandUploadProductFile command = new CommandUploadProductFile();
            command.setProductId(productId);
            command.setFileContent(filecontent);
            command.setFileType(FileType.IMAGE);
            DomainDataServiceUtils.getDomainDataService(this).uploadProductFile(command);
        } catch (Exception e) {
            ErrorHandler errorHandler = new ErrorHandler(e, request, response, this);
            errorHandler.process();
            return;
        }
        response.sendRedirect(String.format("%s%s?product=%s", request.getContextPath(), "/product-view", request.getParameter("product")));
    }
}
