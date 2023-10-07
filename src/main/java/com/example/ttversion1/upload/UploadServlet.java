package com.example.ttversion1.upload;//package com.example.baeldungtest.upload;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.Part;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.nio.file.Files;
//import java.nio.file.Path;
//
//@MultipartConfig(maxFileSize = 1024*5,maxRequestSize = 1024*50)
//@WebServlet("/UploadServlet")
//public class UploadServlet extends HttpServlet {
//    private static final long serialVersionUID =1L;
//    public UploadServlet(){
//        super();
//    }
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        RequestDispatcher rd = request.getRequestDispatcher("/views/upload.html");
//        rd.forward(request,response);
//    }
//    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException{
//        try{
//            String id=request.getParameter("id");
//            String name =request.getParameter("name");
//            Part part= request.getPart("photo");
//
//            String realPath=request.getServletContext().getRealPath("/images");
//            String filename= Path.of(part.getSubmittedFileName()).getFileName().toString();
//
//            if (!Files.exists(Path.of(realPath))){
//                Files.createDirectories(Path.of(realPath));
//
//            }
//            part.write(realPath+"/"+filename);
//
//           try(PrintWriter out= response.getWriter()){
//               out.print("<H2>ID :"+id+"</H2>");
//               out.print("<H2>ID :"+id+"</H2>");
//               out.print("<H2>NAME :"+name+"</H2>");
//               out.print("<img src='images/"+filename+"' width='100px'>");
//           } catch (Exception e){
//               System.out.println("Error");
//           }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
