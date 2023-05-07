/**
 * @author Vatsal Thakkar
 */
package project.local;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;

public class IndexServlet extends HttpServlet {

    /**
     * POST Request handler for the Web App
     * Process incoming request from the index.jsp on form submission, and executes
     * query and provides result back. From Index jsp the query number is passed in
     * the request which Fetches data from the database using DBconnector accordinf
     * to query number. The result is stored in result set "rs" and is then passed
     * to the jsp to display as table.
     * 
     *
     * @author Vatsal Thakkar
     * @param req,res : req will handle the request received from index.jsp on form
     *                submission and res will send the appropriate response
     * @throws ServletException,IOException
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        PrintWriter out = res.getWriter();

        try {

            DBConnector dbconnector = new DBConnector();
            ProjectQueries projectQueries = new ProjectQueries();

            // Fetch the query number passed as parameter from the jsp
            String queryNo = req.getParameter("query");
            // Resultset to store the result
            ResultSet rs = null;

            // Execute the query that was called
            switch (queryNo) {
                case "Query 1":
                    rs = dbconnector.execute(projectQueries.query1());
                    break;

                case "Query 2":
                    rs = dbconnector.execute(projectQueries.query2());
                    break;

                case "Query 3":
                    rs = dbconnector.execute(projectQueries.query3());

                    break;

                case "Query 4":
                    rs = dbconnector.execute(projectQueries.query4());
                    break;

                case "Query 5":
                    rs = dbconnector.execute(projectQueries.query5());
                    break;

                case "Query 6":
                    rs = dbconnector.execute(projectQueries.query6());
                    break;

                default:
                    break;
            }

            if (rs != null) {
                // get the metda data from the result set
                ResultSetMetaData rsmd = rs.getMetaData();
                // get the number of columns from the result set
                int columnCount = rsmd.getColumnCount();

                // Array list to store the column names
                ArrayList<String> columns = new ArrayList<String>();
                // 2-D arraylist to store the result of the query
                ArrayList<ArrayList<String>> queryResult = new ArrayList<ArrayList<String>>();

                // Iterate over the metadata and get the names of the column
                for (int i = 1; i <= columnCount; i++) {
                    columns.add(rsmd.getColumnLabel(i));
                }

                // Iterate over the result set
                while (rs.next()) {
                    ArrayList<String> row = new ArrayList<>();

                    // loop over each row and store values
                    for (int i = 1; i <= columnCount; i++) {
                        row.add(rs.getString(i));
                    }
                    queryResult.add(row);

                }
                req.setAttribute("queryNo",queryNo);
                req.setAttribute("rows", queryResult); // set the result table to rows attribute
                req.setAttribute("columns", columns); // set the column names array to columns attribute
                req.getRequestDispatcher("index.jsp").forward(req, res); // forward the data to index.jsp to display
            }

        } catch (SQLException e) { // To handle exception and provide proper output in case of exception.
            out.println("Failed to create database connection");
            e.printStackTrace();
        }

    }
}
