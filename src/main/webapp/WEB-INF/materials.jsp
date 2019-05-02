<%-- 
    Document   : materials
    Created on : 01-May-2019, 10:40:02
    Author     : vl48
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
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
                        $('#materials_createBtn').removeClass('active');
                        $("#materials_createUI").slideToggle("fast", function () {
                            $("#materials_editUI").slideToggle("fast");
                        });
                    }
                });

                $('#materials_createBtn').click(function () {
                    if (!$(this).hasClass('active')) {
                        $(this).addClass('active');
                        $('#materials_editBtn').removeClass('active');
                        $("#materials_editUI").slideToggle("fast", function () {
                            $("#materials_createUI").slideToggle("fast");
                        });
                    }
                });

                $('#materials_catSelect').one('change', function () {
                    $('#materials_matSelect_wrapper').slideDown("fast");
                });

                $('#materials_catSelect').change(function () {
                    $.get("byggecenter?view=mats&c=products&id=" + $(this).val(), function (jsonResp) {
                        var jsonObj = JSON.parse(jsonResp);
                        var list = $('#materials_matSelect');
                        list.val("");
                        $('#materials_matEdit_wrapper').slideUp("fast");
                        $('option[data-option]', list).remove();
                        $.each(jsonObj, function (key, value) { // Iterate over the JSON array.
                            var optionTag = $("<option data-option>").text(value.category.name + " #" + value.id + " " + value.height + "x" + value.length + "x" + value.width).appendTo(list);
                            optionTag.val(value.id);
                            optionTag.appendTo(list);
                        });
                    });
                });

                $('#materials_matSelect').change(function () {
                    $('#materials_matEdit_name').html($("#materials_matSelect option:selected").text());
                    $.get("byggecenter?view=mats&c=product&id=" + $(this).val(), function (jsonResp) {
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
                            $('#materials_saveMsg_msg').html("Der skete en fejl. Kunne ikke gemme ændringer.");
                        } else {
                            $('#materials_saveMsg_icon').removeClass("ui-icon-alert");
                            $('#materials_saveMsg_icon').addClass("ui-icon-circle-check");
                            $('#materials_saveMsg_msg').html("Success! Ændringerne er nu gemt.");
                        }
                    })
                            .fail(function () {
                                $('#materials_saveMsg_icon').removeClass("ui-icon-circle-check");
                                $('#materials_saveMsg_icon').addClass("ui-icon-alert");
                                $('#materials_saveMsg_msg').html("Der skete en fejl. Kunne ikke gemme ændringer. #404");
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

                function setupProductEdit(jsonResp) {
                    var jsonObj = JSON.parse(jsonResp);
                    cProduct = jsonObj;
                    //status
                    $('#materials_productActive').prop("checked", jsonObj.active);
                    $('#materials_productStock').html(jsonObj.stock);
                    $('#materials_productPrice').val(jsonObj.price);
                    //mål
                    $('#materials_productHeight').val(jsonObj.height);
                    $('#materials_productHeight').attr("disabled", !jsonObj.category.height);
                    $('#materials_productLength').val(jsonObj.length);
                    $('#materials_productLength').attr("disabled", !jsonObj.category.length);
                    $('#materials_productWidth').val(jsonObj.width);
                    $('#materials_productWidth').attr("disabled", !jsonObj.category.width);
                    //save
                    $('#materials_saveBtn').val(jsonObj.id);
                }
            });
        </script>
        <div id="materials_topWrapper">
            <button id="materials_editBtn" class="btn btn-primary active">Redigér materialer</button>
            <button id="materials_createBtn" class="btn btn-primary">Opret materialer</button>
            <div id="materials_ui_wrapper">
                <!-- Edit materials -->
                <div id="materials_editUI">
                    <div class="materials_select_wrapper">
                        Kategori:
                        <select id="materials_catSelect" class="form-control">
                            <option value="" disabled selected>Vælg kategori</option>
                        </select>
                    </div>
                    <div id="materials_matSelect_wrapper" class="materials_select_wrapper" style="display:none;">
                        Materiale:
                        <select id="materials_matSelect" class="form-control">
                            <option value="" disabled selected>Vælg materiale</option>
                        </select>
                    </div>
                    <div id="materials_matEdit_wrapper" style="display:none;">
                        <h2 id="materials_matEdit_name"></h2>
                        <div id="materials_matEdit_form">
                            <div class="materials_formBox">
                                <h5>Status</h5>
                                <input id="materials_productActive" type="checkbox" name="active" value=""> Aktiv<br>
                                På lager: <span id="materials_productStock">0</span> stk.<br>
                                Pris: <input id="materials_productPrice" type="number" name="price" min="0">
                            </div>
                            <div class="materials_formBox">
                                <h5>Mål</h5>
                                <div class="materials_inputWrapper">
                                    Højde:<br>
                                    <input id="materials_productHeight" type="number" name="height" min="0">
                                </div>
                                <div class="materials_inputWrapper">
                                    Længde:<br>
                                    <input id="materials_productLength" type="number" name="length" min="0">
                                </div>
                                <div class="materials_inputWrapper">
                                    Bredde:<br>
                                    <input id="materials_productWidth" type="number" name="width" min="0">
                                </div>
                            </div>
                        </div>
                        <button id="materials_saveBtn" class="btn btn-info">Gem ændringer</button>
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
                    Not yet implemented.
                </div>
                <!-- create end -->
            </div>
        </div>

        <div id="materials_saveMsg" title="Ændringer" style="display:none;">
            <p>
                <span id="materials_saveMsg_icon" class="ui-icon ui-icon-circle-check" style="float:left; margin:0 7px 50px 0;"></span>
                <span id="materials_saveMsg_msg">Success! Ændringerne er nu gemt.</span>
            </p>
        </div>
    </body>
</html>
