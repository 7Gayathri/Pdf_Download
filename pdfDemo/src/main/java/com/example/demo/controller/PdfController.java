package com.example.demo.controller;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
//import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
@RequestMapping("/pdf")
public class PdfController {

	@Autowired
    private SpringTemplateEngine templateEngine;

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadPdf() throws Exception {
        // Prepare Thymeleaf context
        Context context = new Context();
        context.setVariable("title", "PDF Document");
        context.setVariable("content", "This is a sample PDF content.");

        // Render Thymeleaf template to HTML
        String htmlContent = templateEngine.process("template", context);

        // Convert HTML to PDF
        ByteArrayInputStream pdfStream = convertHtmlToPdf(htmlContent);

        // Prepare response
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=example.pdf");

        return new ResponseEntity<>(new InputStreamResource(pdfStream), headers, HttpStatus.OK);
    }

    private ByteArrayInputStream convertHtmlToPdf(String htmlContent) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
        	ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Error generating PDF", e);
        }

        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}

