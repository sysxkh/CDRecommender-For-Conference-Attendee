/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import application.HasPaper;
import application.IsInConference;
import application.RL_Reader;
import application.RL_Writer_Conference;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import textHandler.fileHelper;

/**
 *
 * @author Kehao Xu
 */
public class getList extends HttpServlet {





    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        String result = "";
        String authorid=request.getParameter("authorid");
        
        String cid = request.getParameter("cid");
        
        if(!cid.equals("134") && !cid.equals("135"))
             result = "No such conference !";
        else if(authorid.isEmpty())
            result="No author id!";
        else
        {
            String OS = System.getProperty("os.name").toLowerCase();
            String swrPath = "";
            boolean hasAuthor = false;
            String cacheDir = "";
            if(OS.contains("linux"))
            {
                swrPath = "/home/kex3";
                cacheDir = "/var/lib/tomcat7/webapps/AR/cache";
            }
            if(OS.contains("windows"))
            {
                swrPath = "C:\\study\\Java\\IndependentStudy";
                cacheDir = "C:\\study\\AR\\web\\cache";
            }


            String cachePath = cacheDir+File.separator+cid+"_"+authorid+".json";
            RL_Reader r = new RL_Reader(cachePath);

            boolean isExist = r.Result();

            if(isExist)
            {            
                result = getHtml(r.getString(),cid,authorid);
               // result = r.getString();
            }
            else
            {
                
                if(fileHelper.writeRecord(cid, authorid, cacheDir+File.separator+"waitingList"))
                    result = "<h1>The Recommendation List is Calculating! Please wait.</h1>";
                else
                    result = "Sorry, There are some errors occur !";
            }
        }
        
        response.getWriter().print(result);
        
    }
    
    public String getHtml(String jsonData,String cid,String authorid)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<meta charset=\"ISO-8859-1\" />");
        sb.append("<script src=\"assets/js/jquery.min.js\"></script>");
        sb.append('\n');
        sb.append("<script src=\"assets/js/jquery.dynatable.js\"></script>");
        sb.append('\n');
//        sb.append("<link rel=\"stylesheet\" href=\"assets/css/main.css\" />");
//        sb.append('\n');
        sb.append("<link rel=\"stylesheet\" media=\"all\" href=\"assets/css/jquery.dynatable.css\">");
        sb.append('\n');
        sb.append("</head>");
        sb.append("<div>");
        sb.append("<h2> Introduction : </h2>");
        sb.append("<p>");
        sb.append("This recommendation list shows the authors who you may want to know.</br>");
        sb.append("The recommending is according to a score. This score is consist of two other socre: </br>");
        sb.append("First, the content similarity between those two authors, which calculated by Vector Space Model.</br>");
        sb.append("Second, the Jaccards score between those two authors, which calculated by Jaccard Coefficient. </br>");
        sb.append("Those two scores both have 50% weight in the final score.</br>");
        sb.append("</p>");
        sb.append("</br>");
        sb.append("<table id=\"RTable\">");
        sb.append("<thead>");
        sb.append("<th>Name</th>");
        sb.append("<th>Score</th>");
        sb.append("<th>Simscore</th>");
        sb.append("<th>Jcscore</th>");
        sb.append("<tbody>");
        sb.append("</tbody>");
        sb.append("</table>");
        sb.append("</div>");
        sb.append('\n');
        sb.append("<script type=\"text/javascript\">");
        sb.append('\n');
        sb.append("$(\"#RTable\").dynatable({ dataset: { ");
        sb.append("perPageDefault: 20,");
        sb.append("perPageOptions:[20] ,");
        sb.append("records: ");
        sb.append(jsonData);
        sb.append("},");
        sb.append('\n');
        sb.append("writers:{ 'name':function(record) {");
        sb.append("return \"<a href=\\\"http://halley.exp.sis.pitt.edu/cn3/social_profile.php?conferenceID=\"+");
        sb.append("\"");
        sb.append(cid);
        sb.append("\"+\"&source_aaid=\"+");
        sb.append("\"");
        sb.append(authorid);
        sb.append("\"+\"&aaid=\"+record['id']+\"\\\" target=\\\"_blank\\\">\"+record['name']+\"</a>\"");
        sb.append("}},");
        //sb.append("features:{paginate:false}");
        sb.append(" });");
        sb.append('\n');
        sb.append("</script>");
        sb.append("<script type=\"text/javascript\">");
        sb.append('\n');
        sb.append("$(\"#RTable\").find(\"tr\").first().css(\"background-color\",\"0066FF\");");        
        sb.append('\n');
        sb.append("$(\"#dynatable-per-page-RTable\").parent().hide();");
        sb.append("</script>");
        sb.append("</html>");
        return sb.toString();
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
