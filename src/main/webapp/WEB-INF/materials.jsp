<%-- 
    Document   : materials
    Created on : 01-May-2019, 10:40:02
    Author     : vl48
--%>

<%@page contentType="text/html" pageEncoding="Windows-1252"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="/WEB-INF/parts/headmeta.jspf" %>
        <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
        <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/redmond/jquery-ui.css">
        <title>Materialer & Produkter</title>
        <style>
            #materials_topWrapper {
                max-width:1180px;
                width:100%;
                margin:20px auto;
                padding:20px;
            }    

            #materials_ui_wrapper {
                border-top: 3px #DEDEDE solid;
                padding-top:10px;
            }

            .materials_select_wrapper {
                margin-bottom:10px;
            }

            #materials_matEdit_wrapper {
                padding:10px;
                border-top: 3px #DEDEDE solid;
            }

            .materials_formBox {
                padding:10px;
            }

            .materials_inputWrapper {
                display:inline-block;
                margin-right:5px;
            }

            .materials_formBox input {
                border: 0;
                max-width:100px;
            }
            .materials_formBox input:enabled {
                cursor: text;
            }
            .materials_formBox input:enabled:hover {
                font-weight:700;
            }

            #materials_saveBtn{
                margin-top:20px;   
            }
        </style>
    </head>
    <body>
        <script>
            $(function () {
                $('#materials_editBtn').click(function () {
                    if (!$(this).hasClass('active')) {
                        $(this).addClass('active');
                        if ($('#materials_createBtn').hasClass('active')) {
                            $("#materials_createUI").slideUp("fast");
                            $('#materials_createBtn').removeClass('active');
                        }
                        if ($('#blueprint_editBtn').hasClass('active')) {
                            $("#blueprint_editUI").slideUp("fast");
                            $('#blueprint_editBtn').removeClass('active');
                        }
                        $("#materials_editUI").slideDown("fast");
                    }
                });
                $('#materials_createBtn').click(function () {
                    if (!$(this).hasClass('active')) {
                        $(this).addClass('active');
                        if ($('#materials_editBtn').hasClass('active')) {
                            $("#materials_editUI").slideUp("fast");
                            $('#materials_editBtn').removeClass('active');
                        }
                        if ($('#blueprint_editBtn').hasClass('active')) {
                            $("#blueprint_editUI").slideUp("fast");
                            $('#blueprint_editBtn').removeClass('active');
                        }
                        $("#materials_createUI").slideDown("fast");
                    }
                });
                $('#blueprint_editBtn').click(function () {
                    if (!$(this).hasClass('active')) {
                        $(this).addClass('active');
                        if ($('#materials_createBtn').hasClass('active')) {
                            $("#materials_createUI").slideUp("fast");
                            $('#materials_createBtn').removeClass('active');
                        }
                        if ($('#materials_editBtn').hasClass('active')) {
                            $("#materials_editUI").slideUp("fast");
                            $('#materials_editBtn').removeClass('active');
                        }
                        $("#blueprint_editUI").slideDown("fast");
                    }
                });
                // EDIT
                $('#materials_catSelect').one('change', function () {
                    $('#materials_prodSelect_wrapper').slideDown("fast");
                });
                $('#materials_prodSelect').one('change', function () {
                    $('#materials_matSelect_wrapper').slideDown("fast");
                });
                // CREATE
                $('#materials_catSelect_input').one('change', function () {
                    $('#materials_prodSelect_input_wrapper').slideDown("fast");
                    $('#materials_matCreate_wrapper').slideDown("fast");
                });
                // BLUEPRINTS
                $('#blueprints_modelSelect_input').one('change', function () {
                    $('#blueprint_table_wrapper').slideDown("fast");
                });
                // Pick Category
                $('#materials_catSelect').change(function () {
                    $.get("byggecenter?view=mats&c=productsInCat&categoryID=" + $(this).val(), function (jsonResp) {
                        var jsonObj = JSON.parse(jsonResp);
                        var list = $('#materials_prodSelect');
                        list.val("");
                        $('option[data-option]', list).remove();
                        $.each(jsonObj, function (key, value) { // Iterate over the JSON array.
                            var optionTag = $("<option data-option>").text(value.category.name + " #" + value.id + " " + value.name).appendTo(list);
                            optionTag.val(value.id);
                            optionTag.appendTo(list);
                        });
                    });
                });
                // Pick Product
                $('#materials_prodSelect').change(function () {
                    $.get("byggecenter?view=mats&c=products&categoryID=" + $('#materials_catSelect').val() + "&productID=" + $(this).val(), function (jsonResp) {
                        var jsonObj = JSON.parse(jsonResp);
                        var list = $('#materials_matSelect');
                        list.val("");
                        $('#materials_matEdit_wrapper').slideUp("fast");
                        $('option[data-option]', list).remove();
                        $.each(jsonObj, function (key, value) { // Iterate over the JSON array.
                            var optionTag = $("<option data-option>").text(value.category.name + " #" + value.id + " " + value.height + "x" + value.length + "x" + value.width).appendTo(list);
                            optionTag.val(value.variant_id);
                            optionTag.appendTo(list);
                        });
                    });
                });
                $('#materials_matSelect').change(function () {
                    $('#materials_matEdit_name').html($("#materials_matSelect option:selected").text());
                    $.get("byggecenter?view=mats&c=productVariant&id=" + $(this).val(), function (jsonResp) {
                        setupProductEdit(jsonResp);
                        $('#materials_matEdit_wrapper').slideDown("fast");
                    });
                });
                $.get("byggecenter?view=mats&c=categories", function (jsonResp) {
                    var jsonObj = JSON.parse(jsonResp);
                    var list = $('#materials_catSelect');
                    $('option[data-option]', list).remove();
                    $.each(jsonObj, function (key, value) { // Iterate over the JSON array.
                        var optionTag = $("<option data-option>").text(value.name);
                        optionTag.val(value.id);
                        optionTag.appendTo(list);
                    });
                });


                // OPRET PRODUKTER
                // load cat again
                $.get("byggecenter?view=mats&c=categories", function (jsonResp) {
                    var jsonObj = JSON.parse(jsonResp);
                    var list = $('#materials_catSelect_input');
                    $('option[data-option]', list).remove();
                    $.each(jsonObj, function (key, value) { // Iterate over the JSON array.
                        var optionTag = $("<option data-option>").text(value.name);
                        optionTag.val(value.id);
                        optionTag.appendTo(list);
                    });
                });
                // Pick Category Create
                $('#materials_catSelect_input').change(function () {
                    $.get("byggecenter?view=mats&c=productsInCat&categoryID=" + $(this).val(), function (jsonResp) {
                        var jsonObj = JSON.parse(jsonResp);
                        var list = $('#materials_prodSelect_input');
                        list.val("");
                        $('option[data-option]', list).remove();
                        $.each(jsonObj, function (key, value) { // Iterate over the JSON array.
                            var optionTag = $("<option data-option>").text(value.category.name + " #" + value.id + " " + value.name).appendTo(list);
                            optionTag.val(value.id);
                            optionTag.appendTo(list);
                        });
                    });
                });

                // Pick Product Create
                $('#materials_prodSelect_input').change(function () {
                    var div = $("#materials_matCreateVariant_wrapper");
                    if ($(this).val() == 0) {
                        if (div.hasClass('active')) {
                            div.slideUp();
                            div.removeClass('active');
                        } else {
                            div.slideDown("fast");
                        }
                    } else {
                        $.get("byggecenter?view=mats&c=productMain&id=" + $(this).val(), function (jsonResp) {
                            setupProductCreate(jsonResp);
                            $("#materials_matCreateVariant_wrapper :input").attr("disabled", false);
                            div.addClass('active');
                            div.slideDown("fast");
                        });
                    }

                });

                // BLUEPRINTS
                $.get("byggecenter?view=mats&c=models", function (jsonResp) {
                    var list = $('#blueprints_modelSelect_input');
                    if (jsonResp == "error") {

                    } else {
                        var jsonObj = JSON.parse(jsonResp);
                        $('option[data-option]', list).remove();
                        $.each(jsonObj, function (key, value) { // Iterate over the JSON array.
                            var optionTag = $("<option data-option>").text(value.title);
                            optionTag.val(value.id);
                            optionTag.appendTo(list);
                        });
                    }
                });

                $('#blueprints_modelSelect_input').change(function () {
                    $.get("byggecenter?view=mats&c=blueprint&modelID=" + $(this).val(), function (jsonResp) {
                        var jsonObj = JSON.parse(jsonResp);
                        var table = $('#blueprint_table').find('tbody');
                        $.each(jsonObj, function (key, value) {

                            var tr = $('<tr>');
                            var td_id = $('<td class="blueprint_td_id">').html(value.id).hide();
                            var td_1 = $('<td class="blueprint_td_usage">').html(value.usage);
                            var td_2 = $('<td class="blueprint_td_categoryID">').html(value.category_id);
                            var td_3 = $('<td class="blueprint_td_productID">').html(value.product_id);
                            var input_msg = $('<input type="text" class="blueprint_input_message" />').val(value.message);
                            var td_4 = $('<td class="blueprint_td_message">').append(input_msg);
                            tr.append(td_id, td_1, td_2, td_3, td_4);
                            table.append(tr);
                        });
                    });
                });

                $('#blueprint_saveBtn').click(function () {
                    var list = [];
                    $('#blueprint_table_body tr').each(function () {
                        var id = $(this).find(".blueprint_td_id").html();
                        var usage = $(this).find(".blueprint_td_usage").html();
                        var categoryID = $(this).find(".blueprint_td_categoryID").html();
                        var productID = $(this).find(".blueprint_td_productID").html();
                        var message = $(this).find(".blueprint_input_message").val();

                        var blueprint = {
                            id: id,
                            usage: usage,
                            category_id: categoryID,
                            product_id: productID,
                            message: message
                        };
                        list.push(blueprint);
                    });

                    $.post("byggecenter?view=mats&c=blueprintUpdate", {blueprintJSON: JSON.stringify(list)}, function (data) {
                        if (data === "error") {
                            $('#materials_saveMsg_icon').removeClass("ui-icon-circle-check");
                            $('#materials_saveMsg_icon').addClass("ui-icon-alert");
                            $('#materials_saveMsg_msg').html("Der skete en fejl. Kunne ikke gemme ?ndringer.");
                        } else {
                            $('#materials_saveMsg_icon').removeClass("ui-icon-alert");
                            $('#materials_saveMsg_icon').addClass("ui-icon-circle-check");
                            $('#materials_saveMsg_msg').html("Success! ?ndringerne er nu gemt.");
                        }
                    })
                            .fail(function () {
                                $('#materials_saveMsg_icon').removeClass("ui-icon-circle-check");
                                $('#materials_saveMsg_icon').addClass("ui-icon-alert");
                                $('#materials_saveMsg_msg').html("Der skete en fejl. Kunne ikke gemme ?ndringer. #404");
                            })
                            .always(function () {
                                $("#materials_saveMsg").dialog({
                                    draggable: false,
                                    resizable: false,
                                    modal: true,
                                    closeOnEscape: false,
                                    buttons: {
                                        Ok: function () {
                                            //$('#materials_prodSelect_wrapper').slideUp("fast");
                                            //$('#materials_matSelect_wrapper').slideUp("fast");
                                            //$('#materials_matEdit_wrapper').slideUp("fast");
                                            $(this).dialog("close");
                                        }
                                    }
                                });
                                $('#materials_saveSpinner').css("display", "none");
                            });



                });

                var cProduct;
                $('#materials_saveBtn').click(function () {
                    var currentProduct = cProduct;
                    currentProduct.active = $('#materials_productActive').prop("checked");
                    currentProduct.price = $('#materials_productPrice').val();
                    currentProduct.height = $('#materials_productHeight').val();
                    currentProduct.length = $('#materials_productLength').val();
                    currentProduct.width = $('#materials_productWidth').val();
                    $('#materials_saveSpinner').css("display", "inline-block");
                    $.post("byggecenter?view=mats&c=save", {product: JSON.stringify(currentProduct)}, function (data) {
                        if (data === "error") {
                            $('#materials_saveMsg_icon').removeClass("ui-icon-circle-check");
                            $('#materials_saveMsg_icon').addClass("ui-icon-alert");
                            $('#materials_saveMsg_msg').html("Der skete en fejl. Kunne ikke gemme ?ndringer.");
                        } else {
                            $('#materials_saveMsg_icon').removeClass("ui-icon-alert");
                            $('#materials_saveMsg_icon').addClass("ui-icon-circle-check");
                            $('#materials_saveMsg_msg').html("Success! ?ndringerne er nu gemt.");
                        }
                    })
                            .fail(function () {
                                $('#materials_saveMsg_icon').removeClass("ui-icon-circle-check");
                                $('#materials_saveMsg_icon').addClass("ui-icon-alert");
                                $('#materials_saveMsg_msg').html("Der skete en fejl. Kunne ikke gemme ?ndringer. #404");
                            })
                            .always(function () {
                                $("#materials_saveMsg").dialog({
                                    draggable: false,
                                    resizable: false,
                                    modal: true,
                                    closeOnEscape: false,
                                    buttons: {
                                        Ok: function () {
                                            //$('#materials_prodSelect_wrapper').slideUp("fast");
                                            //$('#materials_matSelect_wrapper').slideUp("fast");
                                            //$('#materials_matEdit_wrapper').slideUp("fast");
                                            $(this).dialog("close");
                                        }
                                    }
                                });
                                $('#materials_saveSpinner').css("display", "none");
                            });
                });
                $('#materials_createNewBtn').click(function () {
                    var prodNew;
                    var query;
                    var div = $("#materials_matCreateVariant_wrapper");
                    if ($("#materials_matCreateVariant_wrapper").hasClass('active')) {
                        var prodV_id = $('#materials_prodSelect_input').val();
                        var prodV_length = $("#product_length_input").val();
                        var prodV_price = $("#product_price_input").val();
                        var prodV_stock = $("#product_stock_input").val();
                        var prodV_active = $("#product_active_input").prop("checked");
                        prodNew = {
                            id: prodV_id,
                            length: prodV_length,
                            price: prodV_price,
                            stock: prodV_stock,
                            active: prodV_active
                        };
                        query = "byggecenter?view=mats&c=create&type=variant";

                    } else {
                        var prod_name = $("#product_name_input").val();
                        var prod_thick = $("#product_thick_input").val();
                        var prod_width = $("#product_width_input").val();
                        var prod_catID = $("#materials_catSelect_input").val();
                        prodNew = {
                            name: prod_name,
                            thickness: prod_thick,
                            width: prod_width,
                            category_id: prod_catID
                        };
                        query = "byggecenter?view=mats&c=create";
                    }

                    $.post(query, {product: JSON.stringify(prodNew)}, function (data) {
                        if (data == "error") {
                            $('#materials_saveMsg_icon').removeClass("ui-icon-circle-check");
                            $('#materials_saveMsg_icon').addClass("ui-icon-alert");
                            $('#materials_saveMsg_msg').html("Der skete en fejl. Kunne ikke oprette produktet.");
                        } else {
                            $('#materials_saveMsg_icon').removeClass("ui-icon-alert");
                            $('#materials_saveMsg_icon').addClass("ui-icon-circle-check");
                            $('#materials_saveMsg_msg').html("Success! Produktet blev oprettet.");
                        }

                    })
                            .fail(function () {
                                $('#materials_saveMsg_icon').removeClass("ui-icon-circle-check");
                                $('#materials_saveMsg_icon').addClass("ui-icon-alert");
                                $('#materials_saveMsg_msg').html("Der skete en fejl. Kunne ikke oprette produktet. #404");
                            })
                            .always(function () {
                                $("#materials_saveMsg").dialog({
                                    draggable: false,
                                    resizable: false,
                                    modal: true,
                                    closeOnEscape: false,
                                    buttons: {
                                        Ok: function () {
                                            $(this).dialog("close");
                                        }
                                    }
                                });
                                $('#materials_saveSpinner').css("display", "none");
                            });
                });

                function setupProductCreate(jsonResp) {
                    var jsonObj = JSON.parse(jsonResp);
                    //cProduct = jsonObj;

                    $("#product_name_input").val(jsonObj.name).attr("disabled", true);
                    $("#product_thick_input").val(jsonObj.thickness).attr("disabled", true);
                    $("#product_width_input").val(jsonObj.width).attr("disabled", true);
                    $("#product_active_input").prop("checked", jsonObj.active);
                }

                function setupProductEdit(jsonResp) {
                    var jsonObj = JSON.parse(jsonResp);
                    cProduct = jsonObj;
                    //status
                    $('#materials_productActive').prop("checked", jsonObj.active);
                    $('#materials_productStock').html(jsonObj.stock);
                    $('#materials_productPrice').val(jsonObj.price);
                    //m?l
                    $('#materials_productHeight').val(jsonObj.height);
                    //$('#materials_productHeight').attr("disabled", !jsonObj.category.height);
                    $('#materials_productLength').val(jsonObj.length);
                    //$('#materials_productLength').attr("disabled", !jsonObj.category.length);
                    $('#materials_productWidth').val(jsonObj.width);
                    //$('#materials_productWidth').attr("disabled", !jsonObj.category.width);
                    //save
                    $('#materials_saveBtn').val(jsonObj.id);
                }
            });
        </script>
        <%@ include file="/WEB-INF/parts/navigation.jspf" %>
        <div class="page-wrapper menu-spacer">
            <div id="materials_topWrapper">
                <button id="materials_editBtn" class="btn btn-primary active">Redig?r materialer</button>
                <button id="materials_createBtn" class="btn btn-primary">Opret materialer</button>
                <button id="blueprint_editBtn" class="btn btn-primary">Rediger Blueprint</button>
                <div id="materials_ui_wrapper">
                    <!-- Edit materials -->
                    <div id="materials_editUI">

                        <div class="materials_select_wrapper">
                            Kategori:
                            <select id="materials_catSelect" class="form-control">
                                <option value="" disabled selected>V?lg kategori</option>
                            </select>
                        </div>

                        <!-- 
                        <button id="materials_createCategoryBtn" class="btn btn-success">Opret kategori</button>
                        -->

                        <div id="materials_prodSelect_wrapper" class="materials_select_wrapper" style="display:none;">
                            Produkter:
                            <select id="materials_prodSelect" class="form-control">
                                <option value="" disabled selected>V?lg produkt</option>
                            </select>
                        </div>

                        <div id="materials_matSelect_wrapper" class="materials_select_wrapper" style="display:none;">
                            Materiale:
                            <select id="materials_matSelect" class="form-control">
                                <option value="" disabled selected>V?lg materiale</option>
                            </select>
                        </div>


                        <div id="materials_matEdit_wrapper" style="display:none;">
                            <h2 id="materials_matEdit_name"></h2>
                            <div id="materials_matEdit_form">
                                <div class="materials_formBox">
                                    <h5>Status</h5>
                                    <input id="materials_productActive" type="checkbox" name="active" value=""> Aktiv<br>
                                    P? lager: <span id="materials_productStock">0</span> stk.<br>
                                    Pris: <input id="materials_productPrice" type="number" name="price" min="0">
                                </div>
                                <div class="materials_formBox">
                                    <h5>M?l</h5>
                                    <div class="materials_inputWrapper">
                                        H?jde:<br>
                                        <input id="materials_productHeight" type="number" name="height" min="0">
                                    </div>
                                    <div class="materials_inputWrapper">
                                        L?ngde:<br>
                                        <input id="materials_productLength" type="number" name="length" min="0">
                                    </div>
                                    <div class="materials_inputWrapper">
                                        Bredde:<br>
                                        <input id="materials_productWidth" type="number" name="width" min="0">
                                    </div>
                                </div>
                            </div>
                            <button id="materials_saveBtn" class="btn btn-info">Gem ?ndringer</button>
                            <div id="materials_saveSpinner" style="display:none;">
                                <div class="spinner-border" role="status">
                                    <span class="sr-only">Gemmer...</span>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- edit end -->

                    <!-- create materials -->
                    <div id="materials_createUI" style="display:none;">
                        <div class="materials_select_wrapper">
                            Kategori:
                            <select id="materials_catSelect_input" class="form-control">
                                <option value="" disabled selected>V?lg kategori</option>
                            </select>
                        </div>

                        <div id="materials_prodSelect_input_wrapper" class="materials_select_wrapper" style="display:none;">
                            Produkter:
                            <select id="materials_prodSelect_input" class="form-control">
                                <option value="0" selected>Opret Nyt Produkt</option>
                            </select>
                        </div>

                        <div id="materials_matCreate_wrapper" style="display:none;">
                            Navn<input id="product_name_input" class="form-control" type="text" name="prodname">
                            Tykkelse<input id="product_thick_input" class="form-control" type="number" name="prodthick">
                            Bredde<input id="product_width_input" class="form-control" type="number" name="prodwidth">

                            <div id="materials_matCreateVariant_wrapper" style="display:none;">
                                <p>Opret Ny Variant<p>
                                    L?ngde<input id="product_length_input" disabled class="form-control" type="number" name="prodlength">
                                    Pris<input id="product_price_input" disabled class="form-control" type="number" name="prodprice">
                                    Antal<input id="product_stock_input" disabled class="form-control" type="number" name="prodstock">
                                    Aktiv<input id="product_active_input" disabled class="form-control" type="checkbox" name="prodactive" value="">
                            </div>
                            <button id="materials_createNewBtn" class="btn btn-info">Opret Produkt</button>
                        </div>
                    </div>
                    <!-- create end -->

                    <!-- edit blueprint -->
                    <div id="blueprint_editUI" style="display:none;">
                        <div id="blueprint_wrapper">
                            <div id="blueprint_select_wrapper">
                                <select id="blueprints_modelSelect_input">
                                    <option value="" disabled selected>V?lg model</option>
                                </select>
                            </div>

                            <div id="blueprint_table_wrapper" style="display:none;">
                                <table id="blueprint_table" class="table">
                                    <thead>
                                    <th>Usage</th>
                                    <th>Kategori</th>
                                    <th>Produkt</th>
                                    <th>Besked</th>
                                    </thead>
                                    <tbody id="blueprint_table_body">

                                    </tbody>
                                </table>
                                <button id="blueprint_saveBtn" class="btn btn-info">Gem Blueprint</button>
                            </div>
                        </div>
                    </div>
                    <!-- edit blueprint end -->
                </div>
            </div>

            <div id="materials_saveMsg" title="?ndringer" style="display:none;">
                <p>
                    <span id="materials_saveMsg_icon" class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
                    <span id="materials_saveMsg_msg">Success! ?ndringerne er nu gemt.</span>
                </p>
            </div>
        </div>
    </body>
</html>
