<!--  
    @author: Vatsal Thakkar

    JSP page for the implementation of front end. Which contains the query description and buttons to execute query
-->

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.util.ArrayList"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <!-- Style tag for visua; appearance of page -->
    <style>
        @import "https://fonts.googleapis.com/css?family=Open+Sans+Condensed:300";

        html,
        body {
          width: 100%;
          height: 100%;
          flex-direction: row;
          flex-wrap: wrap;
          font-family: "Poppins", sans-serif;
          font-weight: 300;
          color: #444;
          line-height: 1.9;
          background-color: #f3f3f3;
          text-align: center;
        }

        /* ============
            Table
        =============== */
        
        .styled-table {
          border-collapse: collapse;
          font-size: 0.9em;
          font-family: sans-serif;
          min-width: 400px;
          box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
        }

        .styled-table thead tr {
          background-color: #009879;
          color: #ffffff;
          text-align: center;
          border-radius: 10px;
        }

        .styled-table th,
        .styled-table td {
          padding: 12px 15px;
        }

        .styled-table tbody tr {
          border-bottom: 1px solid #dddddd;
        }

        .styled-table tbody tr:nth-of-type(even) {
          background-color: #e0e0e0;
        }

        .styled-table tbody tr:last-of-type {
          border-bottom: 2px solid #009879;
          border-bottom-right-radius: 10px;
        }

        .col1 {
          text-align: justify;
          line-height: 1;
        }
        table {
          margin-left: auto;
          margin-right: auto;
          border-radius: 7px;
        }
        /* ============
            MODAL
        =============== */
        .modal, .modal_loading {
          position: fixed;
          overflow: scroll;
          top: 50%;
          left: 50%;
          transform: translate(-50%, -50%);
          height: fit-content;
          width: fit-content;
          max-width: 100rem;
          max-height: 45rem;
          background-color: #f3f3f3;
          padding: 2rem 2rem;
          box-shadow: 0 4rem 6rem rgba(0, 0, 0, 0.3);
          z-index: 1000;
          transition: all 0.5s;
          border-radius: 10px;
        }

        .overlay {
          position: absolute;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
          background-color: rgba(0, 0, 0, 0.5);
          backdrop-filter: blur(4px);
          z-index: 100;
          transition: all 0.5s;
        }

        .modal__form {
          margin: 0 3rem;
          display: grid;
          grid-template-columns: 1fr 2fr;
          align-items: center;
          gap: 2.5rem;
        }

        .modal__form label {
          font-size: 1.7rem;
          font-weight: 500;
        }

        .modal__form input {
          font-size: 1.7rem;
          padding: 1rem 1.5rem;
          border: 1px solid #ddd;
          border-radius: 0.5rem;
        }

        .modal__form button {
          grid-column: 1 / span 2;
          justify-self: center;
          margin-top: 1rem;
        }

        .btn--close-modal {
          font-family: inherit;
          color: inherit;
          position: absolute;
          top: 0.5rem;
          right: 1rem;
          font-size: 2rem;
          cursor: pointer;
          border: none;
          background: none;
        }

        .hidden {
          visibility: hidden;
          opacity: 0;
        }

        .modal-table {
          overflow: scroll;
          min-height: max-content;
        }
        /* .modal-chart{
          width:50%;
        } */
        .modal-body{
          display: flex;
          /* grid-template-columns: 2fr 1fr; */
          grid-gap: 20px;
        }

        /* 
        ==============
        Query Buttons
        ==============
        */
        .btn{
          cursor: pointer;
          margin: 9.5px;
          border-radius: 20px;
          color: #fff;
          background-color: #2bb371;
          padding: 5px;
          height: 40px;
          width: 75px;
          transition: .3s;
          border-color: white;
        }

        .btn:hover{
          color: #00924b;
          background-color: white;
          border-color: #00924b;
        }



        /* ============
            Button
        =============== */

        .button {
          margin-top: 30px;
          flex: auto;
          display: inline-block;
          padding: 0.5rem 1.25rem;
          border-radius: 10rem;
          color: #fff;
          font-weight: 200;
          transition: all 0.3s;
          position: relative;
          overflow: hidden;
          z-index: 1;
          text-decoration: none;
        }
        .button:after {
          content: "";
          position: absolute;
          bottom: 0;
          left: 0;
          width: 100%;
          height: 100%;
          background-color: #2bb371;
          border-radius: 10rem;
          z-index: -2;
        }
        .button:before {
          content: "";
          position: absolute;
          bottom: 0;
          left: 0;
          width: 0%;
          height: 100%;
          background-color: #00924b;
          transition: all 0.3s;
          border-radius: 10rem;
          z-index: -1;
        }
        .button:hover {
          color: #fff;
        }
        .button:hover:before {
          width: 100%;
        }

    </style>
    <title>Database Management Term Project</title>
   

  </head>
  <body>
    <h1>CSCI-6370 Database Management - Term Project</h1>
    <h2> Pro-Kabaddi League Analysis</h2>
    <p><strong>Note:</strong> Click on the Buttons to execute respected Queries. Also, there is a button at the bottom of the table to display the result of recently executed query in case if you close the modal window.</p>
   
    <!-- 
      form to submit the queries 
    -->

    <form id= "fs" action="/dbms/" method="post">
    <table class="styled-table">
      <tbody>
        <tr>
          <td>1.</td>
          <td class="col1">
            The success rate of raiders who have made the most attempted raids across all seasons
          </td>
          <td>
            <button class='btn' type="submit" name="query" onclick="startLoading();" onload= "showBarChart();" value="Query 1">Query-1</button>
          </td>
        </tr>
        <tr>
          <td>2.</td>
          <td class="col1">
            Season-wise comparison between the maximum and average raid points, tackle points, and total points.
          </td>
          <td>
            <button class='btn' type="submit" name="query" onclick="startLoading();" onload = "showLineChart();" value="Query 2">Query-2</button>
          </td>
        </tr>
        <tr>
          <td>3.</td>
          <td class="col1">
            For each team, the comparison between their win percentage at home matches, their win percentage at neutral venues, and their win percentage at away matches.
          </td>
          <td>
            <button class='btn' type="submit" name="query" onclick="startLoading();" value="Query 3">Query-3</button>
            
          </td>
        </tr>
        <tr>
          <td>4.</td>
          <td class="col1">
            The win rate of a team is being compared between the scenarios where they won the toss and where they did not win the toss
          </td>
          <td>
            <button class='btn' type="submit" name="query" onclick="startLoading();" value="Query 4">Query-4</button>
          </td>
        </tr>
        <tr>
          <td>5.</td>
          <td class="col1">
            The success rate of defenders who have attempted the highest number of tackles across all seasons.
            </ul>
          </td>
          <td>
            <button class='btn' type="submit" name="query" onclick="startLoading();" value="Query 5">Query-5</button>
          </td>
        </tr>
        <tr>
          <td>6.</td>
          <td class="col1">
            The success and failure rates of teams in knockout or decider matches .
          </td>
          <td>
            <button class='btn' type="submit" name="query" onclick="startLoading();" value="Query 6">Query-6</button>
          </td>
        </tr>
      </tbody>
    </table>
  </form>
  <a  class="button">Show Result</a>
  
  <!-- 
    Modal window to show the output 
  -->

    <div class="modal hidden">
      <button class="btn--close-modal">&times;</button>
      <h3 class="modal__header">Result of Query</h3>
      <div class="modal-body">
        <div class="modal-table">
          <%
          if(request.getAttribute("rows") != null){

              ArrayList<ArrayList <String> > rows = (ArrayList<ArrayList <String> >) request.getAttribute("rows");
              ArrayList<String> cols = (ArrayList<String>) request.getAttribute("columns");
          %>
          <div id="show_res_div"></div>
          <%
              if(rows.size() == 0){
          %>
    
          <div style="text-align: center; color: red;">None of the tuples satisfy this query</div>
    
          <%
              }
              else{

          %>
          <table class="styled-table">
          <thead>
            <tr>
              <%
                  for(int i = 0; i < cols.size(); i++){
              %>
                      <th><%= cols.get(i) %></th>
              <%
                  }
              %>
            </tr>
          </thead>
     
          <tbody>
          <%
              for(int i = 0; i < rows.size(); i++){
        
              ArrayList<String> row = (ArrayList<String>) rows.get(i);
          %>
                  <tr>
                      <% 
                          for(int j = 0; j < row.size(); j++){

                      %>

                      <td> <%= row.get(j) %></td>
                    
                      <%
                          }
                      %>
                    
                  </tr>
          <%
             
              } // end for
          %>
          </tbody>
        </table>
        <%
      } //else
      } // end if
  %>
        </div>
      </div>
    </div>
    <div class="overlay hidden"></div>

    <div class="modal_loading hidden">
      <h3 class="modal__header">Loading Query</h3>
      <div class="modal-body">
        Query is in execution. Please wait . . .
      </div>
    </div>
    
    <footer class="footer">
      <p class="footer__copyright">
        &copy; Copyright by <a class="footer__lin href="#"> Vatsal Thakkar</a>
      </p>
    </footer>
  </body>
  
  <!-- 
    JavaScript

   -->
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    
  <script type="text/javascript">
    const modal = document.querySelector(".modal");
    const modal_loading = document.querySelector(".modal_loading");
    
    const overlay = document.querySelector(".overlay");
    const btnsOpenModal = document.querySelectorAll(".button");
    const btnCloseModal = document.querySelector(".btn--close-modal");

    // workaround
    const hidden_div = document.getElementById("show_res_div");
    if(hidden_div){
      modal.classList.remove("hidden");
      overlay.classList.remove("hidden");
    }

    const openModal = function (e) {
      e.preventDefault();
      modal.classList.remove("hidden");
      overlay.classList.remove("hidden");
    };

    const closeModal = function () {
      modal.classList.add("hidden");
      overlay.classList.add("hidden");
    };

    const startLoading = function(){
      modal_loading.classList.remove("hidden");
      overlay.classList.remove("hidden");
    }

    btnsOpenModal.forEach((btn) => btn.addEventListener("click", openModal));

    btnCloseModal.addEventListener("click", closeModal);
    overlay.addEventListener("click", closeModal);

    document.addEventListener("keydown", function (e) {
      if (e.key === "Escape" && !modal.classList.contains("hidden")) {
        closeModal();
      }
    });
   

  </script>

</html>
