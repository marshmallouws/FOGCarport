<%-- 
    Document   : orderpage
    Created on : Apr 23, 2019, 10:52:48 AM
    Author     : caspe
--%>
<%@page import="entity.Product"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Integer> carportSelectWidth = (List<Integer>) request.getAttribute("carportSelectWidth");
    List<Integer> carportSelectLength = (List<Integer>) request.getAttribute("carportSelectLength");
    List<Integer> shedSelectWidth = (List<Integer>) request.getAttribute("shedSelectWidth");
    List<Integer> shedSelectLength = (List<Integer>) request.getAttribute("shedSelectLength");
    List<Product> roofTypes = (List<Product>) request.getAttribute("roofTypes");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Byg din carport! - Fog</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
        <script src="https://preview.babylonjs.com/babylon.js"></script> 
        <script src="https://preview.babylonjs.com/loaders/babylonjs.loaders.min.js"></script>
        <script src="https://code.jquery.com/pep/0.4.1/pep.js"></script>
        <link rel="stylesheet" href="3dview/visual.css">    
        <script src="3dview/visual.js"></script>
    </head>
    <body>
        <div class="header"><img src="3dview/images/fog-logo.png" /></div>
        <div class="orderpage_wrapper">
            <div id="canvasWrapper">
                <div id="canvasArea">
                    <div id="cLoadScreenContainer">
                        <span class="center"></span>
                        <img src="3dview/images/fog-logo.png"/>
                        <svg width="100%" height="100%" viewBox="0 0 100 100" preserveAspectRatio="xMidYMid">
                        <path stroke="none" d="M10 50A40 40 0 0 0 90 50A40 42 0 0 1 10 50" fill="#004687" transform="rotate(179.561 50 51)">
                        <animateTransform attributeName="transform" type="rotate" calcMode="linear" values="0 50 51;360 50 51" keyTimes="0;1" dur="1s" begin="0s" repeatCount="indefinite">
                        </animateTransform>
                        </path>
                        </svg>
                    </div>
                    <img class="watermark" src="3dview/images/fog-logo.png" />
                    <a class="reset" onclick="updateScene()">
                        <svg viewBox="0 0 20 20"  preserveAspectRatio="xMidYMid">
                        <path d="M3.254,6.572c0.008,0.072,0.048,0.123,0.082,0.187c0.036,0.07,0.06,0.137,0.12,0.187C3.47,6.957,3.47,6.978,3.484,6.988c0.048,0.034,0.108,0.018,0.162,0.035c0.057,0.019,0.1,0.066,0.164,0.066c0.004,0,0.01,0,0.015,0l2.934-0.074c0.317-0.007,0.568-0.271,0.56-0.589C7.311,6.113,7.055,5.865,6.744,5.865c-0.005,0-0.01,0-0.015,0L5.074,5.907c2.146-2.118,5.604-2.634,7.971-1.007c2.775,1.912,3.48,5.726,1.57,8.501c-1.912,2.781-5.729,3.486-8.507,1.572c-0.259-0.18-0.618-0.119-0.799,0.146c-0.18,0.262-0.114,0.621,0.148,0.801c1.254,0.863,2.687,1.279,4.106,1.279c2.313,0,4.591-1.1,6.001-3.146c2.268-3.297,1.432-7.829-1.867-10.101c-2.781-1.913-6.816-1.36-9.351,1.058L4.309,3.567C4.303,3.252,4.036,3.069,3.72,3.007C3.402,3.015,3.151,3.279,3.16,3.597l0.075,2.932C3.234,6.547,3.251,6.556,3.254,6.572z">
                        </path>
                        </svg>
                    </a>
                    <canvas id="renderCanvas"></canvas>
                </div>
            </div>



        <div class="container options-wrapper">
            <h1>Orderpage</h1>

            <form method="POST" action="byggecenter?view=addorder">
                <div class="form-group">
                    <label for="carportWidth">Carport bredde</label>
                    <select id="widthIn" class="form-control" name="carportWidth" id="carportWidth">
                        <option value=0>Vælg bredde</option>
                        <% for (int width : carportSelectWidth) {%>
                        <option value=<%= width%> <%if (width == 360) {%>selected<%}%>><%= width%> cm</option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="carportLength">Carport længde</label>
                    <select id="lengthIn" class="form-control" name="carportLength" id="carportLength">
                        <option value=0>Vælg længde</option>
                        <% for (int length : carportSelectLength) {%>
                        <option value=<%= length%> <%if (length == 720) {%>selected<%}%>><%= length%> cm</option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="roofType">Tag</label>
                    <select class="form-control" name="roofType" id="roofType">
                        <option value=0>Vælg tag</option>
                        <% for (Product roof : roofTypes) {%>
                        <option value=<%= roof.getId()%>><%= roof.getName()%></option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="roofAngle">Tag-vinkel</label>
                    <select id="roofAngleIn" class="form-control" name="roofAngle" id="roofAngle">
                        <option value=0>Ingen rejsning</option>
                        <option value="15">15 grader</option>
                        <option value="20">20 grader</option>
                        <option value="25" selected="selected">25 grader</option>
                        <option value="30">30 grader</option>
                        <option value="35">35 grader</option>
                        <option value="40">40 grader</option>
                        <option value="45">45 grader</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="shedWidth">Redskabsrum bredde:</label>
                    <select id="shedWidthIn" class="form-control" name="shedWidth" id="shedWidth">
                        <option value=0>Ønsker ikke redskabsrum</option>
                        <% for (int width : shedSelectWidth) {%>
                        <option value=<%= width%> <%if (width == 330) {%>selected<%}%>><%= width%> cm</option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="shedLength">Redskabsrum længde:</label>
                    <select id="shedLengthIn" class="form-control" name="shedLength" id="shedLength">
                        <option value=0>Ønsker ikke redskabsrum</option>
                        <% for (int length : shedSelectLength) {%>
                        <option value=<%= length%> <%if (length == 210) {%>selected<%}%>><%= length%> cm</option>
                        <% }%>
                    </select>
                </div>

                <label>Højde</label>
                <input id="heightIn" type="number" value="305" min="225" max="500">

                <div class="form-group">
                    <label for="fullname">Navn</label>
                    <input type="text" class="form-control" name="fullname" id="fullname">
                </div>

                <div class="form-group">
                    <label for="address">Adresse</label>
                    <input type="text" class="form-control" name="address" id="address">
                </div>

                <div class="form-group">
                    <label for="zip">Postnummer</label>
                    <input type="text" class="form-control" name="zip" id="zip">
                </div>

                <div class="form-group">
                    <label for="phone">Telefon</label>
                    <input type="text" class="form-control" name="phone" id="phone">
                </div>

                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="text" class="form-control" name="email" id="email">
                </div>

                <!--               <div class="form-group">
                                   <label for="message">Evt. bemærkninger</label>
                                   <textarea class="form-control" name="message" id="message"></textarea>
                               </div>
                -->
                <button type="submit" class="btn btn-success">Bestil Carport</button>    
                
            </form>

        </div><!-- container end -->
        
        </div>
    </body>
</html>
