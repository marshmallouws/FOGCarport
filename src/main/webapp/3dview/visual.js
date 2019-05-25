window.addEventListener('DOMContentLoaded', function () {
    startVisual();
});

function cLoadScreen() {
    //init the loader
    this.containerEl = document.getElementById("cLoadScreenContainer");
}
cLoadScreen.prototype.displayLoadingUI = function () {
    this.containerEl.style.display = "block";
};
cLoadScreen.prototype.hideLoadingUI = function () {
    $('#cLoadScreenContainer').fadeOut("slow");
};


function orderButtonDisabled(disabled){
    if(disabled){
        $('#getOffer_btn').prop("disabled", true);
        $('#getOffer_btn').removeClass("btn-success");
        $('#getOffer_btn').addClass("btn-danger");
    } else {
        $('#getOffer_btn').prop("disabled", false);
        $('#getOffer_btn').addClass("btn-success");
        $('#getOffer_btn').removeClass("btn-danger");
    }
}


var canvas;
var engine;
var scene;
function startVisual() {
    canvas = document.getElementById('renderCanvas');
    engine = new BABYLON.Engine(canvas, true);
    engine.loadingScreen = new cLoadScreen();
    updateScene();

    engine.runRenderLoop(function () {
        scene.render();
    });

    window.addEventListener('resize', function () {
        engine.resize();
    });

    $("#widthIn, #lengthIn, #roofType").change(function () {
        if ($(this).val() > 0) {
            updateScene(); } else {
            orderButtonDisabled(true);
            }
    });
    $("#shedWidthIn, #shedLengthIn").change(function () {
        updateScene();
    });
    $("#roofAngleIn").change(function () {
        if($(this).val()<=0){
            $("#flatRoofType").slideDown("fast");
        } else {
            $("#flatRoofType").slideUp("fast");
        }
        updateScene();
    });
}

function updateScene() {
    var wIn = document.getElementById("widthIn");
    var lIn = document.getElementById("lengthIn");
    var shedLin = document.getElementById("shedLengthIn");
    var shedWin = document.getElementById("shedWidthIn")
    var roofTin = document.getElementById("roofType");
    var heightInput = document.getElementById("heightIn");
    if(heightInput.value > heightInput.max-0)heightInput.value = heightInput.max;
    else if (heightInput.value < heightInput.min-0) heightInput.value = heightInput.min;
    width = (wIn.value / 100) * 1.2;
    length = (lIn.value / 100) * 2;
    height = (heightInput.value / 100) * 2;
    shedLength = (shedLin.value / 100) * 2;
    shedWidth = (shedWin.value / 100) * 1.2;
    shed = shedLength < 0.5 || shedWidth < 0.5 ? false : true;
    roofAngle = document.getElementById("roofAngleIn").value;
    roof = roofAngle < 5 ? false : true;
    roofType = roofTin.value;
        
    ///check if values are negative
    if(width<=0||length<=0||height<=0||(!roof && roofType <=0)){
        orderButtonDisabled(true);
        return;
    }
    ////////// check if shed is too big
    if (shed && shedWidth > width) {
        shedWin.style.backgroundColor = "#e67e7e";
        $('#shedWidthIn > option').each(function () {
            if ($(this).val() < wIn.value) {
                $(this).css("background-color", "white");
            } else {
                $(this).css("background-color", "");
            }
        });
        return;
    } else {
        shedWin.style.backgroundColor = "";
    }
    if (shed && shedLength > length) {
        shedLin.style.backgroundColor = "#e67e7e";
        $('#shedLengthIn > option').each(function () {
            if ($(this).val() < lIn.value) {
                $(this).css("background-color", "white");
            } else {
                $(this).css("background-color", "");
            }
        });
        return;
    } else {
        shedLin.style.backgroundColor = "";
    }
    ///is roof type selected?
    if (!roof && roofType<=0) {
        roofTin.style.backgroundColor = "#e67e7e";
        $('#roofType > option').each(function () {
            if ($(this).val() > 0) {
                $(this).css("background-color", "white");
            } else {
                $(this).css("background-color", "");
            }
        });
        return;
    } else {
        roofTin.style.backgroundColor = "";
    }
    //////////////////////////
    // Everything passed - create scene and enable order button.
    engine.displayLoadingUI();
    scene = createScene();
    orderButtonDisabled(false);
}

var roofAngle = 0;
var mat;
var roofmat;
var concretemat;
var roofmat2;
var doormat;
var rooftile;
var shadowGenerator;

var createScene = function () {
    // Create a basic BJS Scene object.
    scene = new BABYLON.Scene(engine);
    var assetsManager = new BABYLON.AssetsManager(scene);
    // Create Camera
    // Parameters: alpha, beta, radius, target position, scene
    var camera = new BABYLON.ArcRotateCamera("Camera", 3 * Math.PI / 2, Math.PI / 2, 10, new BABYLON.Vector3(-15, 3, -15), scene);
    camera.lowerRadiusLimit = 5;
    camera.upperRadiusLimit = width + length + height;
    camera.useAutoRotationBehavior = true;
    camera.allowUpsideDown = false;
    camera.checkCollisions = true;
    camera.collisionRadius = new BABYLON.Vector3(0.5, 0.5, 0.5);
    // Target the camera to middle of carport build.
    camera.setTarget(new BABYLON.Vector3(length / 2, height / 2.5, 0));
    camera.radius = length + height;
    // Attach the camera to the canvas.
    camera.attachControl(canvas, false);
    scene.activeCamera.panningSensibility = 0;

    // Create a basic light, aiming 0,1,0 - meaning, to the sky.
    var _light = new BABYLON.HemisphericLight('light1', new BABYLON.Vector3(0, 1, 0), scene);
    _light.intensity = 0.8;
    _light.specular = new BABYLON.Color3(0, 0, 0);
    // Directional light and shadows
    var light = new BABYLON.DirectionalLight("dir01", new BABYLON.Vector3(-.8, -2, -1), scene);
    light.position = new BABYLON.Vector3(20, 50, 20);
    light.intensity = 1;
    light.specular = new BABYLON.Color3(0, 0, 0);
    shadowGenerator = new BABYLON.ShadowGenerator(1024, light);
    shadowGenerator.usePoissonSampling = true;

    // Skybox
    var skybox = BABYLON.MeshBuilder.CreateBox("skyBox", {size: length * 500.0}, scene);
    var skyboxMaterial = new BABYLON.StandardMaterial("skyBox", scene);
    skyboxMaterial.backFaceCulling = false;
    skyboxMaterial.reflectionTexture = new BABYLON.CubeTexture("3dview/textures/skybox/skybox4", scene);
    skyboxMaterial.reflectionTexture.coordinatesMode = BABYLON.Texture.SKYBOX_MODE;
    skyboxMaterial.disableLighting = true;
    skybox.material = skyboxMaterial;

    /// Materials / Textures
    mat = new BABYLON.StandardMaterial("wood", scene);
    var woodTextureTask = assetsManager.addTextureTask("woodTask", "3dview/textures/wood.png");
    woodTextureTask.onSuccess = function (task) {
        mat.diffuseTexture = task.texture;
        mat.diffuseTexture.hasAlpha = true;
        mat.backFaceCulling = false;
        mat.diffuseTexture.uScale = 1;
    }

    woodmat = new BABYLON.StandardMaterial("woodplank", scene);
    var woodplankTextureTask = assetsManager.addTextureTask("woodplankTask", "3dview/textures/woodplanks.jpg");
    woodplankTextureTask.onSuccess = function (task) {
        woodmat.diffuseTexture = task.texture;
        woodmat.diffuseTexture.hasAlpha = false;
        woodmat.backFaceCulling = false;
        woodmat.diffuseTexture.wAng = 1.575;
        woodmat.diffuseTexture.uScale = 0.9;
        woodmat.diffuseTexture.vScale = 0.8;
    }

    roofmat = new BABYLON.StandardMaterial("plast", scene);
    var plastTextureTask = assetsManager.addTextureTask("plastTask", "3dview/textures/plast.jpg");
    plastTextureTask.onSuccess = function (task) {
        roofmat.diffuseTexture = task.texture;
        roofmat.diffuseTexture.hasAlpha = false;
        roofmat.backFaceCulling = false;
        roofmat.diffuseTexture.uScale = 20;
            var roofColor = $('#roofType').find(":selected").text();
            if(roofColor.includes("blå"))roofmat.diffuseColor = new BABYLON.Color3(0.32, 0.36, 0.44);
            if(roofColor.includes("grøn"))roofmat.diffuseColor = new BABYLON.Color3(0.36, 0.44, 0.36);
            if(roofColor.includes("rød"))roofmat.diffuseColor = new BABYLON.Color3(0.44, 0.36, 0.32);
    }

    concretemat = new BABYLON.StandardMaterial("ground", scene);
    var concreteTextureTask = assetsManager.addTextureTask("concreteTask", "3dview/textures/ground.jpg");
    concreteTextureTask.onSuccess = function (task) {
        concretemat.diffuseTexture = task.texture;
        concretemat.diffuseTexture.hasAlpha = false;
        concretemat.backFaceCulling = false;
        concretemat.diffuseTexture.uScale = 10;
        concretemat.diffuseTexture.vScale = 10;
        concretemat.diffuseTexture.uOffset = 0.25;
        concretemat.diffuseTexture.vOffset = 0.5;
    }

    dirtmat = new BABYLON.StandardMaterial("ground", scene);
    var dirtTextureTask = assetsManager.addTextureTask("dirtTask", "3dview/textures/dirt.jpg");
    dirtTextureTask.onSuccess = function (task) {
        dirtmat.diffuseTexture = task.texture;
        dirtmat.diffuseTexture.hasAlpha = false;
        dirtmat.backFaceCulling = false;
        dirtmat.diffuseTexture.uScale = 20;
        dirtmat.diffuseTexture.vScale = 20;
        dirtmat.diffuseTexture.uOffset = 0.25;
        dirtmat.diffuseTexture.vOffset = 0.5;
    }

    roofmat2 = new BABYLON.StandardMaterial("whitewood", scene);
    var whitewoodTextureTask = assetsManager.addTextureTask("whitewoodTask", "3dview/textures/whitewood.jpg");
    whitewoodTextureTask.onSuccess = function (task) {
        roofmat2.diffuseTexture = task.texture;
        roofmat2.diffuseTexture.hasAlpha = false;
        roofmat2.backFaceCulling = false;
        roofmat2.diffuseTexture.uScale = 2;
        roofmat2.diffuseTexture.vScale = 1;
    }

    rooftile = new BABYLON.StandardMaterial("blacktile", scene);
    var tileTextureTask = assetsManager.addTextureTask("tileTask", "3dview/textures/blacktile.jpg");
    tileTextureTask.onSuccess = function (task) {
        rooftile.diffuseTexture = task.texture;
        rooftile.diffuseTexture.hasAlpha = false;
        rooftile.backFaceCulling = false;
        rooftile.diffuseTexture.uScale = 1.0;
        rooftile.diffuseTexture.vScale = 1.8;
    }

    doormat = new BABYLON.StandardMaterial("door", scene);
    var doorTextureTask = assetsManager.addTextureTask("doorTask", "3dview/textures/door.png");
    doorTextureTask.onSuccess = function (task) {
        doormat.diffuseTexture = task.texture;
        doormat.diffuseTexture.hasAlpha = false;
        doormat.backFaceCulling = false;
        doormat.diffuseTexture.wAng = 1.57;
    }

    //Build carport
    createSupports(mat, shed);
    if (shed)
        createShed(woodmat);
    createRoof(roofmat, roofmat2, roof);

    //Build environment
    var platform = BABYLON.MeshBuilder.CreateCylinder("platform", {diameter: (length + width + height) + 2, height: 0.4, tessellation: 32}, scene);
    platform.material = concretemat;
    platform.position.z = 0;
    platform.position.y = -2.2;
    platform.position.x = length / 2;
    platform.checkCollisions = true;
    platform.receiveShadows = true;

    var ground = BABYLON.MeshBuilder.CreateGround('ground1', {height: width * 100, width: length * 50, subdivisions: 2}, scene);
    ground.position.y = -2.2;
    ground.material = dirtmat;
    ground.checkCollisions = true;
    ground.receiveShadows = true;

    //build trees and bushes only after loading their sprites.
    var treesTask = assetsManager.addTextureTask("treesTask", "3dview/images/tree.png");
    treesTask.onSuccess = function (task) {
        var spriteManagerTrees = new BABYLON.SpriteManager("treesManager", task.texture, 500, 950, scene);
        //trees at random positions
        for (var i = 0; i < 100; i++) {
            var tree = new BABYLON.Sprite("tree", spriteManagerTrees);
            var pos = getRandomSpritePosition(false);
            tree.position.x = pos[0];
            tree.position.z = pos[1];
            tree.position.y = 9;
            tree.size = Math.random() * (50 - 30) + 30;
            tree.color = new BABYLON.Color4(.14, .33, .17, Math.random() * (1 - .5) + .5);
        }
    }

    var bushTask = assetsManager.addTextureTask("bushTask", "3dview/images/bush.png");
    bushTask.onSuccess = function (task) {
        var spriteManagerBush = new BABYLON.SpriteManager("bushManager", task.texture, 500, 950, scene);
        //bushes at random positions
        for (var i = 0; i < 100; i++) {
            var bush = new BABYLON.Sprite("tree", spriteManagerBush);
            var pos = getRandomSpritePosition(true);
            bush.position.x = pos[0];
            bush.position.z = pos[1];
            bush.position.y = -4;
            bush.size = Math.random() * (20 - 10) + 10;
            bush.color = new BABYLON.Color4(.14, .33, .17, Math.random() * (1 - .5) + .5);
        }
    }
    
    //start loading assets.
    assetsManager.load();   
    
    //make sure sidebar menu is visible.
    $(".sidebar-wrapper").css("z-index","9990");

    // Return the created scene.
    return scene;

    //calculate random position for sprites.
    function getRandomSpritePosition(close) {
        var distance = close ? width * 1.2 + length * 1.2 + height : width * 2 + length * 2 + height;
        var pos1 = Math.random() * 500 - 250;
        var pos2 = Math.random() * 500 - 250;
        return (pos1 >= distance || pos2 <= -distance) || (pos2 >= distance || pos1 <= -distance) ?
                new Array(pos1, pos2) : getRandomSpritePosition();
    }
}


var supportSize = 0.35;
function createSupports(mat, shed) {
    var cLength = shed ? length - shedLength : length;
    for (var i = 0; i < cLength; i++) {
        //Add a support beam at least every 8 units.
        if (i % 8 !== 0)
            continue;
        var box = BABYLON.MeshBuilder.CreateBox('box' + i + '_', {height: height, width: supportSize, depth: supportSize}, scene);
        box.material = mat;
        box.position.z = -width;
        box.position.y = -2 + (height * 0.5);
        box.position.x = i === 0 ? 0.5 : i;
        shadowGenerator.getShadowMap().renderList.push(box);
        box.receiveShadows = true;
    }
    //if there's room, place a support beam at the back in the left side.
    if (shedWidth < width) {
        cLength = length;
        supportEnd(mat, true);
    }

    for (var i = 0; i < cLength; i++) {
        //Add a support beam at least every 8 units.
        if (i % 8 !== 0)
            continue;
        var box = BABYLON.MeshBuilder.CreateBox('box' + i, {height: height, width: supportSize, depth: supportSize}, scene);
        box.material = mat;
        box.position.z = width;
        box.position.y = -2 + (height * 0.5);
        box.position.x = i === 0 ? 0.5 : i;
        shadowGenerator.getShadowMap().renderList.push(box);
        box.receiveShadows = true;
    }

    //if there's no shed, add a support beam in the back on each side.
    if (!shed) {
        supportEnd(mat, true);
        supportEnd(mat, false);
    }

    //Remme
    var box = BABYLON.MeshBuilder.CreateBox('box_raem1', {height: 0.5, width: length, depth: 0.2}, scene);
    box.material = mat;
    box.position.z = width + 0.2;
    box.position.y = height - 2.3;
    box.position.x = length / 2;
    shadowGenerator.getShadowMap().renderList.push(box);
    box.receiveShadows = true;

    var box = BABYLON.MeshBuilder.CreateBox('box_raem2', {height: 0.5, width: length, depth: 0.2}, scene);
    box.material = mat;
    box.position.z = -width - 0.2;
    box.position.y = height - 2.3;
    box.position.x = length / 2;
    shadowGenerator.getShadowMap().renderList.push(box);
    box.receiveShadows = true;
}

function supportEnd(mat, left) {
    var cWidth = left ? width : -width;
    var box = BABYLON.MeshBuilder.CreateBox('boxend', {height: height, width: supportSize, depth: supportSize}, scene);
    box.material = mat;
    box.position.z = cWidth;
    box.position.y = -2 + (height * 0.5);
    box.position.x = length - 0.5;
    shadowGenerator.getShadowMap().renderList.push(box);
    box.receiveShadows = true;
}

function createShed(mat) {
    //back
    var box = BABYLON.MeshBuilder.CreateBox('boxBackWall', {height: height, width: 0.2, depth: shedWidth * 2.0}, scene);
    box.position.x = length + .2;
    box.position.y = -2 + (height * 0.5);
    box.position.z = -(width - shedWidth);
    box.material = mat;
    box.checkCollisions = true;
    shadowGenerator.getShadowMap().renderList.push(box);
    box.receiveShadows = true;

    //front
    var box = BABYLON.MeshBuilder.CreateBox('boxFrontWall', {height: height, width: 0.2, depth: shedWidth * 2.0}, scene);
    box.position.x = length + .2 - shedLength;
    box.position.y = -2 + (height * 0.5);
    box.position.z = -(width - shedWidth);
    box.material = mat;
    box.checkCollisions = true;
    shadowGenerator.getShadowMap().renderList.push(box);
    box.receiveShadows = true;

    //door
    var box = BABYLON.MeshBuilder.CreateBox('boxDoor', {height: 4.5, width: 0.35, depth: 2.5}, scene);
    box.position.x = length + .2 - shedLength;
    box.position.y = .28;
    box.position.z = -width + 2;
    box.material = doormat;
    box.receiveShadows = true;

    //left side
    var box = BABYLON.MeshBuilder.CreateBox('boxSideWall1', {height: height, width: 0.2, depth: shedLength}, scene);
    box.position.x = length + .2 - shedLength / 2;
    box.position.y = -2 + (height * 0.5);
    box.position.z = -(width - shedWidth * 2);
    box.material = mat;
    box.rotation.y = new BABYLON.Angle.FromDegrees(90).radians();
    box.checkCollisions = true;
    shadowGenerator.getShadowMap().renderList.push(box);
    box.receiveShadows = true;

    //right side
    var box = BABYLON.MeshBuilder.CreateBox('boxSideWall2', {height: height, width: 0.2, depth: shedLength}, scene);
    box.position.x = length + .2 - shedLength / 2;
    box.position.y = -2 + (height * 0.5);
    box.position.z = -width;
    box.material = mat;
    box.rotation.y = new BABYLON.Angle.FromDegrees(90).radians();
    box.checkCollisions = true;
    shadowGenerator.getShadowMap().renderList.push(box);
    box.receiveShadows = true;

    //top
    var box = BABYLON.MeshBuilder.CreateBox('boxRoof', {height: 0.1, width: shedLength, depth: shedWidth * 2}, scene);
    box.position.x = length + .2 - shedLength / 2;
    box.position.y = height - 2;
    box.position.z = -(width - shedWidth);
    box.material = roofmat;
    box.checkCollisions = true;
    box.receiveShadows = true;
}


function createRoof(roofmat, roofmat2, roof) {
    if (!roof) {
        //build flat roof
        var box = BABYLON.MeshBuilder.CreateBox('boxRoof', {height: 0.2, width: length + 1.08, depth: width * 2 + 1}, scene);
        box.position.x = length / 2;
        box.position.y = height - 2;
        box.material = roofmat;
        box.checkCollisions = true;
        shadowGenerator.getShadowMap().renderList.push(box);
        box.receiveShadows = true;
    } else {
        //build angled roof
        //roof foundation
        var box = BABYLON.MeshBuilder.CreateBox('boxRoof', {height: 0.1, width: length + 1.01, depth: width * 2 + 1}, scene);
        box.position.x = length / 2;
        box.position.y = height - 2;
        box.material = roofmat2;
        box.checkCollisions = true;
        shadowGenerator.getShadowMap().renderList.push(box);
        box.receiveShadows = true;

        //roof tiles
        /////////////////////////////////////////////////////
        var _A = roofAngle;
        var _b = width * 100;
        var _B = 180.0 - 90.0 - _A;
        var _a = ((Math.sin(toRadians(_A)) * _b) / Math.sin(toRadians(_B)) / 100);
        var _c = ((Math.sin(toRadians(90)) * _b) / Math.sin(toRadians(_B)) / 100);

        //right tile
        var box = BABYLON.MeshBuilder.CreateBox('boxRoofSide1', {height: 0.5, width: length + 1.75, depth: _c + 0.5}, scene);
        box.setPivotMatrix(BABYLON.Matrix.Translation(0, 0, -_c / 2));
        box.rotation.x = -new BABYLON.Angle.FromDegrees(_A).radians();
        box.position.z = -(_c + 0.5) / 2;
        box.position.x = length / 2;
        box.position.y = height - 2 + _a;
        box.material = rooftile;
        box.checkCollisions = true;
        shadowGenerator.getShadowMap().renderList.push(box);
        box.receiveShadows = true;

        //left tile
        var box = BABYLON.MeshBuilder.CreateBox('boxRoofSide2', {height: 0.5, width: length + 1.75, depth: _c + 0.5}, scene);
        box.setPivotMatrix(BABYLON.Matrix.Translation(0, 0, _c / 2));
        box.rotation.x = new BABYLON.Angle.FromDegrees(_A).radians();
        box.position.z = (_c + 0.5) / 2;
        box.position.x = length / 2;
        box.position.y = height - 2 + _a;
        box.material = rooftile;
        box.checkCollisions = true;
        shadowGenerator.getShadowMap().renderList.push(box);
        box.receiveShadows = true;

        //top tile
        var top = BABYLON.MeshBuilder.CreateCylinder("rooftop", {diameter: 0.55, height: length + 1.75, tessellation: 16}, scene);
        top.material = rooftile;
        top.rotation.x = new BABYLON.Angle.FromDegrees(90).radians();
        top.rotation.y = new BABYLON.Angle.FromDegrees(90).radians();
        top.position.z = 0;
        top.position.y = height - 2 + _a + (_a / 20);
        top.position.x = length / 2;
        top.checkCollisions = true;
        box.receiveShadows = true;

        //roof planks
        //////////////////////////////////////
        //right front
        var box = BABYLON.MeshBuilder.CreateBox('rooffacade', {height: 0.6, width: 0.1, depth: _c + 0.55 + (_A / 100)}, scene);
        box.setPivotMatrix(BABYLON.Matrix.Translation(0, 0, -_c / 2));
        box.rotation.x = -new BABYLON.Angle.FromDegrees(_A).radians();
        box.position.z = -(_c + 0.5) / 2;
        box.position.x = -.9;
        box.position.y = height - 2 + _a;
        box.material = roofmat2;
        box.checkCollisions = true;
        shadowGenerator.getShadowMap().renderList.push(box);
        box.receiveShadows = true;

        //right back
        var box = BABYLON.MeshBuilder.CreateBox('rooffacade', {height: 0.6, width: 0.1, depth: _c + 0.55 + (_A / 100)}, scene);
        box.setPivotMatrix(BABYLON.Matrix.Translation(0, 0, -_c / 2));
        box.rotation.x = -new BABYLON.Angle.FromDegrees(_A).radians();
        box.position.z = -(_c + 0.5) / 2;
        box.position.x = length + .9;
        box.position.y = height - 2 + _a;
        box.material = roofmat2;
        box.checkCollisions = true;
        shadowGenerator.getShadowMap().renderList.push(box);
        box.receiveShadows = true;

        //left front
        var box = BABYLON.MeshBuilder.CreateBox('rooffacade', {height: 0.6, width: 0.1, depth: _c + 0.55 + (_A / 100)}, scene);
        box.setPivotMatrix(BABYLON.Matrix.Translation(0, 0, _c / 2));
        box.rotation.x = new BABYLON.Angle.FromDegrees(_A).radians();
        box.position.z = ((_c + 0.5) / 2);
        box.position.x = -.9;
        box.position.y = height - 2 + _a;
        box.material = roofmat2;
        box.checkCollisions = true;
        shadowGenerator.getShadowMap().renderList.push(box);
        box.receiveShadows = true;

        //left back
        var box = BABYLON.MeshBuilder.CreateBox('rooffacade', {height: 0.6, width: 0.1, depth: _c + 0.55 + (_A / 100)}, scene);
        box.setPivotMatrix(BABYLON.Matrix.Translation(0, 0, _c / 2));
        box.rotation.x = new BABYLON.Angle.FromDegrees(_A).radians();
        box.position.z = ((_c + 0.5) / 2);
        box.position.x = length + .9;
        box.position.y = height - 2 + _a;
        box.material = roofmat2;
        box.checkCollisions = true;
        shadowGenerator.getShadowMap().renderList.push(box);
        box.receiveShadows = true;

        //roof front / back panels
        //////////////////////////////////////
        //Create custom meshes
        var roofFront = new BABYLON.Mesh("rFront", scene);
        var roofBack = new BABYLON.Mesh("rBack", scene);

        //Set arrays for positions and indices
        var positions = [
            0, //startpoint
            _a, //top point
            2,
            -width, //left point
            0,
            2,
            width, //right point  
            0,
            2];
        var indices = [0, 1, 2];
        var uvs = [0.5, 0, 0, 0, 1, 1];
        //Empty array to contain calculated values
        var normals = [];

        var vertexData = new BABYLON.VertexData();
        BABYLON.VertexData.ComputeNormals(positions, indices, normals);

        //Assign positions, indices and normals to vertexData
        vertexData.positions = positions;
        vertexData.indices = indices;
        vertexData.normals = normals;
        vertexData.uvs = uvs;
        var roofLength = length + 2.5;

        roofFront.rotation.y = 1.57;
        roofFront.position.x = length - roofLength;
        roofFront.position.y = height - 2;
        roofFront.material = roofmat2;
        roofFront.checkCollisions = true;
        roofFront.receiveShadows = true;

        roofBack.rotation.y = -1.57;
        roofBack.position.x = roofLength;
        roofBack.position.y = height - 2;
        roofBack.material = roofmat2;
        roofBack.checkCollisions = true;
        roofBack.receiveShadows = true;

        //Apply vertexData to custom mesh
        vertexData.applyToMesh(roofFront);
        vertexData.applyToMesh(roofBack);
        shadowGenerator.getShadowMap().renderList.push(roofFront);
        shadowGenerator.getShadowMap().renderList.push(roofBack);
    }

    function toRadians(degrees) {
        degrees = (degrees / 180) * Math.PI;
        return degrees;
    }

}