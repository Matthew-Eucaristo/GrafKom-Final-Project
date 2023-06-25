import Engine.*;
import Engine.Object;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.naming.ldap.PagedResultsControl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL30.*;

public class Main {
    private Window window = new Window(800, 800, "Hello World");
    private ArrayList<Object> objects = new ArrayList<>();

    private MouseInput mouseInput;
    int countDegree = 0;
    Projection projection = new Projection(window.getWidth(), window.getHeight());
    Camera camera = new Camera();

    Matrix4f viewMatrix;

    ShaderProgram objectShader;
    List<ShaderProgram.ShaderModuleData> shaderModuleDataList;

    Object mainCharacter;
    boolean toggleKeyPressed = false;

    boolean cameraModeIsFPS = false;
    boolean cameraDT = false;
    boolean cameraTransitionCompleted = true;

    boolean dropTowerSwitch = true;

    private void importObjects(List<ShaderProgram.ShaderModuleData> shaderModuleDataList, List<Object> parent,
                               String filename,
                               Vector4f color, Vector3f translate, Vector3f scale, Vector4f rotate) {
        // check if parent is null
        if (parent == null) {
            parent = objects;
        }
        // check if the translate or scale or rotate is null
        if (translate == null) {
            translate = new Vector3f(0f, 0f, 0f);
        }
        if (scale == null) {
            scale = new Vector3f(1f, 1f, 1f);
        }
        if (rotate == null) {
            rotate = new Vector4f(0f, 0f, 0f, 0f);
        }

        // divide the color to the 255
        color.x /= 255f;
        color.y /= 255f;
        color.z /= 255f;
        color.w /= 255f;

        // add objects
        parent.add(new ObjectLoader(
                shaderModuleDataList,
                new ArrayList<>(),
                color,
                Arrays.asList(0.0f, -0.5f, 0.0f),
                0.5f,
                0.5f,
                0.5f,
                36,
                18,
                filename // path to the object
        )
                .inlineScaleObject(scale.x, scale.y, scale.z)
                .inlineRotateObject((float) Math.toRadians(rotate.w), rotate.x, rotate.y, rotate.z)
                .inlineTranslateObject(translate.x, translate.y, translate.z));

    }

    private void importObjects(List<ShaderProgram.ShaderModuleData> shaderModuleDataList, String filename,
                               Vector4f color, Vector3f translate, Vector3f scale, Vector4f rotate) {
        importObjects(shaderModuleDataList, null, filename, color, translate, scale, rotate);
    }

    private void importObjects(List<ShaderProgram.ShaderModuleData> shaderModuleDataList, String filename,
                               Vector4f color, Vector3f translate, float scaleXYZ, Vector4f rotate) {
        importObjects(shaderModuleDataList, filename, color, translate, new Vector3f(scaleXYZ, scaleXYZ, scaleXYZ),
                rotate);
    }

    private void importObjects(List<ShaderProgram.ShaderModuleData> shaderModuleDataList, List<Object> parent,
                               String filename,
                               Vector4f color, Vector3f translate, float scaleXYZ, Vector4f rotate) {
        importObjects(shaderModuleDataList, parent, filename, color, translate,
                new Vector3f(scaleXYZ, scaleXYZ, scaleXYZ),
                rotate);
    }

    public void createCaroussel() {

        // caroussel
        importObjects(shaderModuleDataList, "resources/blender/Caroussel/carousel(2).obj",
                new Vector4f(197f, 204f, 8f, 255f), // warna
                new Vector3f(-40f, 1f, 20f), 1, // translasi dan scaling object
                new Vector4f(1f, 0f, 0f, -360)); // rotasi
    }
    public void createSwingride() {
        //swing ride
        importObjects(shaderModuleDataList, "resources/blender/Swing_Ride/SwingRide.obj",
                new Vector4f(197f, 204f, 8f, 255f), // warna
                new Vector3f(-12, 1f, 20f), 3, // translasi dan scaling object
                new Vector4f(1f, 0f, 0f, -360)); // rotasi
    }

    public boolean updateDropTowerSit(float y) {

        if (y > 15) {
            dropTowerSwitch = false;
        } else if (y < 1) {
            dropTowerSwitch = true;
        }
        return dropTowerSwitch;
    }

    private void createStreetLamps() {
        float scale = 0.05f;
        // create the street lamps
        importObjects(shaderModuleDataList, "resources/blender/street lamp/street_lamp.fbx",
                new Vector4f(31f, 21f, 14f, 255f), new Vector3f(100f, 20f, 100f), scale, new Vector4f(1f, 0f, 0f, 0));

        // set as parent
        List<Object> streetLamps = objects.get(3).getChildObject();

        importObjects(shaderModuleDataList, streetLamps, "resources/blender/street lamp/street_lamp.fbx",
                new Vector4f(31f, 21f, 14f, 255f), new Vector3f(100f, 20f, 0f), scale, new Vector4f(1f, 0f, 0f, 0));
        importObjects(shaderModuleDataList, streetLamps, "resources/blender/street lamp/street_lamp.fbx",
                new Vector4f(31f, 21f, 14f, 255f), new Vector3f(300f, 20f, 100f), scale, new Vector4f(1f, 0f, 0f, 0));
        importObjects(shaderModuleDataList, streetLamps, "resources/blender/street lamp/street_lamp.fbx",
                new Vector4f(31f, 21f, 14f, 255f), new Vector3f(100f, 20f, 0f), scale, new Vector4f(1f, 0f, 0f, 0));
        importObjects(shaderModuleDataList, streetLamps, "resources/blender/street lamp/street_lamp.fbx",
                new Vector4f(31f, 21f, 14f, 255f), new Vector3f(200f, 20f, 0f), scale, new Vector4f(1f, 0f, 0f, 0));
        importObjects(shaderModuleDataList, streetLamps, "resources/blender/street lamp/street_lamp.fbx",
                new Vector4f(31f, 21f, 14f, 255f), new Vector3f(300f, 20f, 0f), scale, new Vector4f(1f, 0f, 0f, 0));

    }

    private void createTrees() {
        // set rotations
        Vector4f rotation = new Vector4f(0, 0f, 0f, 0f);

        // color leaf
        Vector4f colorLeaf = new Vector4f(0, 255f, 0, 255f);

        // color wood
        Vector4f colorWood = new Vector4f(50, 45, 23, 255f);

        // take the trees in blender and create many trees as decoration
        createTree(null, colorLeaf, colorWood,
                new Vector3f(), 1,
                rotation);

        // set as parent
        List<Object> trees = objects.get(2).getChildObject();

        // create child trees
        createTree(trees, colorLeaf, colorWood,
                new Vector3f(75, 11, 43),
                1,
                rotation);
        createTree(trees, colorLeaf, colorWood,
                new Vector3f(83, 17, 41),
                1,
                rotation);
        createTree(trees, colorLeaf, colorWood,
                new Vector3f(66.8f, 2, 49),
                1,
                rotation);

    }

    private void createTree(List<Object> parent, Vector4f colorLeaf, Vector4f colorWood,
                            Vector3f translate, float scaleXYZ,
                            Vector4f rotate) {
        importObjects(shaderModuleDataList, parent, "resources/blender/tree/leaf.obj", colorLeaf,
                translate,
                scaleXYZ, rotate);
        importObjects(shaderModuleDataList, parent, "resources/blender/tree/wood.obj", colorWood,
                translate,
                scaleXYZ, rotate);

    }

    private void createMC(Object mainCharacter) {
        // create main character as child of the parent

        // rotations
        Vector4f rotation = new Vector4f(1f, 0f, 0f, -90f);

        // transitions
        Vector3f translate = new Vector3f(-7.5f, -1.19f, -57f);

        // create the body
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/body.fbx",
                new Vector4f(170f, 200f, 170f, 255f), translate, 1f, rotation);

        // create the head
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/head.fbx",
                new Vector4f(190f, 190f, 189f, 255f), translate, 1f, rotation);

        // create the eye
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/eye.fbx",
                new Vector4f(10f, 10f, 200f, 255f), translate, 1f, rotation);

        // create the hair
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/hair.fbx",
                new Vector4f(10f, 20f, 20f, 255f), translate, 1f, rotation);

        // create the leg
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/leg.fbx",
                new Vector4f(100f, 255f, 255f, 255f), translate, 1f, rotation);

        // create the mouth
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/mouth.fbx",
                new Vector4f(255f, 255f, 0f, 255f), translate, 1f, rotation);

        // create the shirt
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/shirt.fbx",
                new Vector4f(255f, 0f, 255f, 255f), translate, 1f, rotation);

        // rotation for all child of main character
        mainCharacter.getChildObject().forEach(object -> {
            object.inlineRotateObject((float) Math.toRadians(180), 0f, 1f, 0f);
        });

        // translation for all child of main character
        mainCharacter.getChildObject().forEach(object -> {
            object.inlineTranslateObject(0f, 1f, 0f);
        });
    }

    private void createCastles() {
        importObjects(shaderModuleDataList, null, "resources/blender/gate/gate_door.obj",
                new Vector4f(169, 138, 100, 255), null, 1f, new Vector4f(1f, 0f, 0f, 0));

        // set as parent
        List<Object> gate = objects.get(4).getChildObject();

        // towers
        importObjects(shaderModuleDataList, gate, "resources/blender/gate/towers.obj", new Vector4f(225, 210, 160, 255),
                null, 1f, new Vector4f(1f, 0f, 0f, 0));

        // walls
        importObjects(shaderModuleDataList, gate, "resources/blender/gate/walls.obj", new Vector4f(81, 60, 49, 255),
                null, 1f, null);

    }

    private void createDropTower() {
        Vector3f translate = new Vector3f(10f, 1f, 2f);

        importObjects(shaderModuleDataList, null, "resources/blender/drop tower/DropTower.obj",
                new Vector4f(179, 140, 12, 255), null, null, null);

        // set as parent
        List<Object> dropTower = objects.get(5).getChildObject();


        // square
        importObjects(shaderModuleDataList, dropTower, "resources/blender/drop tower/DTSquare.obj", new Vector4f(12, 46, 148, 255), null, null, null);

        // sit
        importObjects(shaderModuleDataList, dropTower, "resources/blender/drop tower/DTSit.obj", new Vector4f(179, 140, 12, 255), null, null, null);

        // platform
        importObjects(shaderModuleDataList, dropTower, "resources/blender/drop tower/DTPlatform.obj", new Vector4f(156, 8, 20, 255), null, null, null);

        // ramp
        importObjects(shaderModuleDataList, dropTower, "resources/blender/drop tower/DTRamp.obj", new Vector4f(82, 81, 79, 255), null, null, null);

        // rounded fence
        importObjects(shaderModuleDataList, dropTower, "resources/blender/drop tower/DTRoundedFence.obj", new Vector4f(143, 141, 134, 255), null, null, null);

        // fence
        importObjects(shaderModuleDataList, dropTower, "resources/blender/drop tower/DTFence.obj", new Vector4f(143, 141, 134, 255), null, null, null);

        // fence 2
        importObjects(shaderModuleDataList, dropTower, "resources/blender/drop tower/DTFence2.obj", new Vector4f(143, 141, 134, 255), null, null, null);

        // head
        importObjects(shaderModuleDataList, dropTower, "resources/blender/drop tower/DTHead.obj", new Vector4f(156, 8, 20, 255), null, null, null);

        // structure
        importObjects(shaderModuleDataList, dropTower, "resources/blender/drop tower/DTStructure.obj", new Vector4f(179, 140, 12, 255), null, null, null);

    }

    private void createFerrisWheel() {
        Vector3f translate = new Vector3f(-5f, 1f, -5f);

        importObjects(shaderModuleDataList, null, "resources/blender/ferris wheel/FWSpindle.obj",
                new Vector4f(213, 215, 219,255), null, null,null);

        // set as parent
        List<Object> ferrisWheel = objects.get(6).getChildObject();

        // wheel
        importObjects(shaderModuleDataList, ferrisWheel, "resources/blender/ferris wheel/FWWheel.obj", new Vector4f(213, 215, 219,255), null, null,null);

        // sit
        importObjects(shaderModuleDataList, ferrisWheel, "resources/blender/ferris wheel/FWSit.obj", new Vector4f(156, 8, 20,255), null, null, null);

        // other sit
        importObjects(shaderModuleDataList, ferrisWheel, "resources/blender/ferris wheel/FWOtherSit.obj", new Vector4f(156, 8, 20,255), null, null, null);

        // structure
        importObjects(shaderModuleDataList, ferrisWheel, "resources/blender/ferris wheel/FWStructure.obj", new Vector4f(156, 8, 20,255), null, null, null);

        // fence
        importObjects(shaderModuleDataList, ferrisWheel, "resources/blender/ferris wheel/FWFence.obj", new Vector4f(143, 141, 134,255), null, null, null);

        // fence1
        importObjects(shaderModuleDataList, ferrisWheel, "resources/blender/ferris wheel/FWFence1.obj", new Vector4f(143, 141, 134,255), null, null,null);

        // fence2
        importObjects(shaderModuleDataList, ferrisWheel, "resources/blender/ferris wheel/FWFence2.obj", new Vector4f(143, 141, 134,255), null, null,null);

        // fence3
        importObjects(shaderModuleDataList, ferrisWheel, "resources/blender/ferris wheel/FWFence3.obj", new Vector4f(143, 141, 134,255), null, null,null);

        // ramp
        importObjects(shaderModuleDataList, ferrisWheel, "resources/blender/ferris wheel/FWRamp.obj", new Vector4f(143, 141, 134,255),null, null,null);

        // structure
        importObjects(shaderModuleDataList, ferrisWheel, "resources/blender/ferris wheel/FWPlatform.obj", new Vector4f(213, 215, 219,255), null, null,null);

        objects.get(6).updateCenterPoint();


    }
    private void createColourLamps() {
        float scale = 1;
        // create the colour lamps
        importObjects(shaderModuleDataList, "resources/blender/colour lamp/ColourLamp.obj",
                new Vector4f(31f, 21f, 14f, 255f), null, null, null);

        // set as parent
        List<Object> colourLamps = objects.get(8).getChildObject();


//        importObjects(shaderModuleDataList, streetLamps, "resources/blender/street lamp/street_lamp.fbx",
//                new Vector4f(31f, 21f, 14f, 255f), new Vector3f(100f, 20f, 0f), scale, new Vector4f(1f, 0f, 0f, 0));
//        importObjects(shaderModuleDataList, streetLamps, "resources/blender/street lamp/street_lamp.fbx",
//                new Vector4f(31f, 21f, 14f, 255f), new Vector3f(300f, 20f, 100f), scale, new Vector4f(1f, 0f, 0f, 0));
//        importObjects(shaderModuleDataList, streetLamps, "resources/blender/street lamp/street_lamp.fbx",
//                new Vector4f(31f, 21f, 14f, 255f), new Vector3f(100f, 20f, 0f), scale, new Vector4f(1f, 0f, 0f, 0));
//        importObjects(shaderModuleDataList, streetLamps, "resources/blender/street lamp/street_lamp.fbx",
//                new Vector4f(31f, 21f, 14f, 255f), new Vector3f(200f, 20f, 0f), scale, new Vector4f(1f, 0f, 0f, 0));
//        importObjects(shaderModuleDataList, streetLamps, "resources/blender/street lamp/street_lamp.fbx",
//                new Vector4f(31f, 21f, 14f, 255f), new Vector3f(300f, 20f, 0f), scale, new Vector4f(1f, 0f, 0f, 0));

    }

    public void init() {
        window.init();
        GL.createCapabilities();
        mouseInput = window.getMouseInput();
        camera.setPosition(5f, 35f, 140f);

        // create the shader program
        // usahain di atas
        shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER));
        shaderModuleDataList
                .add(new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER));

        objectShader = new ShaderProgram(shaderModuleDataList);
        objectShader.link();

        // this is for the main character
        objects.add(new Sphere(
                // this is for the object sementara buat chara yg digerakkin
                shaderModuleDataList,
                new ArrayList<>(),
                new Vector4f(1.0f, 1.0f, 0.0f, 1.0f),
                Arrays.asList(0.0f, 0.0f, 0.0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18)
                .inlineTranslateObject(0f, 5f, 0f)
                .inlineScaleObjectXYZ(0f)
                .inlineRotateObject((float) Math.toRadians(180), 0f, 1f, 0f));

        // main character
        mainCharacter = objects.get(0);
        createMC(mainCharacter);

        // Terrain
        importObjects(shaderModuleDataList, "resources/blender/terrain/terrain.obj", new Vector4f(58, 105, 0, 255),
                null, null,
                new Vector4f(0f, 0f, 0f, 0));

        // Trees
        createTrees();

        // Street lamps
        createStreetLamps();

        // Gate and castles
        createCastles();

        // caroussel
        createCaroussel();
        createSwingride();

        // Drop Tower
        createDropTower();

        // Ferris Wheel
        createFerrisWheel();

        // Bumper Car
        createBumperCar();

        // Colour Lamp
        createColourLamps();


        // Random Object
        objects.add(new Sphere(
                // this is for the object sementara buat chara yg digerakkin
                shaderModuleDataList,
                new ArrayList<>(),
                new Vector4f(1.0f, 1.0f, 1.0f, 1.0f),
                Arrays.asList(10f, 0f, 0f),
                0.125f,
                0.125f,
                0.125f,
                36,
                18));
        // .inlineTranslateObject(10f, 2f, 10f)
        // .inlineScaleObjectXYZ(50f)
        // .inlineRotateObject((float) Math.toRadians(180), 0f, 0f, 0f));


        // Get the camera's view matrix.
        viewMatrix = camera.getViewMatrix();

    }

    private void createBumperCar() {
        // alas bumpercar
        importObjects(shaderModuleDataList, null, "resources/blender/bumper car/alas_bumpercar.obj",
                new Vector4f(96,101,89,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // set as parent
        List<Object> bumperCar = objects.get(7).getChildObject();

        // all child
        // asuna body
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/asuna_body.obj",
                new Vector4f(253,229,205,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // asuna hair
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/asuna_hair.obj",
                new Vector4f(214,163,84,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // asuna shirt
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/asuna_shirt.obj",
                new Vector4f(163,58,21,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // asuna shoes
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/asuna_shoes.obj",
                new Vector4f(182,180,183,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // bagian dalam kursi
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/bagian_dalam_kursi.obj",
                new Vector4f(142,142,142,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // batang_kayu
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/batang_kayu.obj",
                new Vector4f(119,102,22,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // besi buat kaki
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/besi_buat_kaki.obj",
                new Vector4f(40,40,40,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // bumper color1
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/bumper_color1.obj",
                new Vector4f(253,254,100,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // fence
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/fence.obj",
                new Vector4f(227,226,224,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // iron fence
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/iron_fence.obj",
                new Vector4f(82,97,127,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // leaf
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/leaf.obj",
                new Vector4f(123,167,100,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // main_bumper
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/main_bumper.obj",
                new Vector4f(23,21,30,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // main lights
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/main_lights.obj",
                new Vector4f(255,255,255,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // main skeleton body
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/main_skeleton_body.obj",
                new Vector4f(205,205,205,211), null, null, new Vector4f(1f, 0f, 0f, 0));

        // plane
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/plane.obj",
                new Vector4f(168,157,147,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // pot
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/pot.obj",
                new Vector4f(136,90,82,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // sandaran kepala
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/sandaran_kepala.obj",
                new Vector4f(128,128,128,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // setir
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/setir.obj",
                new Vector4f(100,101,99,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // tent
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/tent.obj",
                new Vector4f(195,80,17,255), null, null, new Vector4f(1f, 0f, 0f, 0));

        // tiang listrik
        importObjects(shaderModuleDataList, bumperCar, "resources/blender/bumper car/tiang_listrik.obj",
                new Vector4f(165,148,134,255), null, null, new Vector4f(1f, 0f, 0f, 0));
    }

    public void input() {
        float cameraSpeed = 0.2f;
        float characterSpeed = 0.03f;
        float rotateSpeedInDegrees = 1f;

        // keybind for toggling FPS or TPS
        if (window.isKeyPressed(GLFW_KEY_1)) {
            if (!toggleKeyPressed) {
                toggleKeyPressed = true;

                // toggle camera transition
                cameraTransitionCompleted = false;

                if (cameraDT) {
                    cameraDT = false;
                }

                // toggle camera mode
                if (cameraModeIsFPS) {
                    cameraModeIsFPS = false;
                } else {
                    cameraModeIsFPS = true;

                    // toggle camera transition
                    cameraTransitionCompleted = false;
                }
            }
        } else if (window.isKeyPressed(GLFW_KEY_2)) {
            if (!toggleKeyPressed) {
                toggleKeyPressed = true;

                // toggle camera transition
                cameraTransitionCompleted = false;


                // toggle camera mode
                if (cameraDT) {
                    cameraDT = false;
                } else {
                    cameraDT = true;
                    cameraTransitionCompleted = false;

                }
                System.out.println(cameraDT);
            }


        } else {
            toggleKeyPressed = false;
        }


        // ini buat yang WASD
        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(cameraSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(cameraSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(cameraSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(cameraSpeed);
        }

        // ini buat Q dan E buat rotate
        // this is to rotate around the object
        if (window.isKeyPressed(GLFW_KEY_Q)) {
            camera.addRotation(0, (float) Math.toRadians(-1 * rotateSpeedInDegrees));
            camera.moveRight((float) Math.toRadians(rotateSpeedInDegrees));
        }
        if (window.isKeyPressed(GLFW_KEY_E)) {
            camera.addRotation(0, (float) Math.toRadians(rotateSpeedInDegrees));
            camera.moveLeft((float) Math.toRadians(rotateSpeedInDegrees));
        }

        if (window.isKeyPressed(GLFW_KEY_Z)) {
            camera.addRotation((float) Math.toRadians(-1 * rotateSpeedInDegrees), 0);
            camera.moveDown((float) Math.toRadians(rotateSpeedInDegrees));
        }
        if (window.isKeyPressed(GLFW_KEY_X)) {
            camera.addRotation((float) Math.toRadians(rotateSpeedInDegrees), 0);
            camera.moveUp((float) Math.toRadians(rotateSpeedInDegrees));
        }
        if (window.isKeyPressed(GLFW_KEY_PAGE_UP)) {
            camera.moveUp(cameraSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_PAGE_DOWN)) {
            camera.moveDown(cameraSpeed);
        }

        // this is for moving the character
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            objects.get(0).translateObject(0f, 0f, -characterSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            objects.get(0).translateObject(0f, 0f, characterSpeed);
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            objects.get(0).translateObject(-characterSpeed, 0f, 0f);
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            objects.get(0).translateObject(characterSpeed, 0f, 0f);
        }

        // ini yang buat mouse button
        if (mouseInput.isLeftButtonPressed()) {
            Vector2f displayVec = window.getMouseInput().getDisplVec();
            // this is so that the camera will move around the object
            camera.addRotation((float) Math.toRadians(displayVec.x * rotateSpeedInDegrees),
                    (float) Math.toRadians(displayVec.y * rotateSpeedInDegrees));

            camera.moveUp((float) Math.toRadians(displayVec.y * rotateSpeedInDegrees));
            camera.moveRight((float) Math.toRadians(displayVec.x * rotateSpeedInDegrees));
        }
        if (window.getMouseInput().getScroll().y != 0) {
            projection.setFOV(projection.getFOV() - (window.getMouseInput().getScroll().y * 0.01f));
            window.getMouseInput().setScroll(new Vector2f());
        }
    }

    public void loop() {
        while (window.isOpen()) {
            window.update();
            glClearColor(0,22/255f,87/255f,1);
            GL.createCapabilities();
            input();

            // code
            for (Object object : objects) {
                object.draw(camera, projection);
            }

            // update all the center point to the correct one
            objects.forEach(Object::updateCenterPoint);

            // camera transition
            cameraTransition();

            // set FPS/free
            if (cameraModeIsFPS && cameraTransitionCompleted && !cameraDT) {
                // set to FPS mode
                Vector3f eyePosition = new Vector3f(
                        mainCharacter.getChildObject().get(2).getCenterPoint().get(0),
                        mainCharacter.getChildObject().get(2).getCenterPoint().get(1),
                        mainCharacter.getChildObject().get(2).getCenterPoint().get(2));

                // set the camera to the main character eye
                camera.setPosition(eyePosition.x, eyePosition.y + 2f, eyePosition.z);
                camera.lockInEye();

            }
            // set CameraDT
            if (cameraDT && cameraTransitionCompleted) {
                // set to DT mode
                Vector3f eyePosition = new Vector3f(
                        objects.get(5).getChildObject().get(0).getCenterPoint().get(0) + 27.2f,
                        objects.get(5).getChildObject().get(0).getCenterPoint().get(1),
                        objects.get(5).getChildObject().get(0).getCenterPoint().get(2)+0.8f);

                // set the camera to the main character eye
                camera.setPosition(eyePosition.x, eyePosition.y + 2f, eyePosition.z);
//                camera.lockInEye();

            }
            // Translate Drop Tower Sit
            if (updateDropTowerSit(objects.get(5).getChildObject().get(0).getCenterPoint().get(1))) {
                objects.get(5).getChildObject().get(0).inlineTranslateObject(0f, 0.05f, 0f);
                objects.get(5).getChildObject().get(1).inlineTranslateObject(0f, 0.05f, 0f);
            } else {
                objects.get(5).getChildObject().get(0).inlineTranslateObject(0f, -0.2f, 0f);
                objects.get(5).getChildObject().get(1).inlineTranslateObject(0f, -0.2f, 0f);
            }

//            System.out.println("Awal" + objects.get(6).getChildObject().get(0).getCenterPoint());
//            objects.get(6).getChildObject().get(0).updateCenterPoint();
//            float selisihX = objects.get(8).getCenterPoint().get(0)- objects.get(6).getChildObject().get(0).getCenterPoint().get(0);
//            float selisihY = objects.get(8).getCenterPoint().get(1)- objects.get(6).getChildObject().get(0).getCenterPoint().get(1);
//            float selisihZ = objects.get(8).getCenterPoint().get(2)- objects.get(6).getChildObject().get(0).getCenterPoint().get(2);
            
            objects.get(6).getChildObject().get(0).inlineTranslateObject(-6f,-24.5f,0f);
            objects.get(6).getChildObject().get(0).inlineRotateObject(0.01f,0,0,1);
            objects.get(6).getChildObject().get(0).inlineTranslateObject(6f,24.5f,0f);

            objects.get(6).getChildObject().get(1).inlineTranslateObject(-6f,-24.5f,0f);
            objects.get(6).getChildObject().get(1).inlineRotateObject(0.01f,0,0,1);
            objects.get(6).getChildObject().get(1).inlineTranslateObject(6f,24.5f,0f);

            objects.get(6).getChildObject().get(2).inlineTranslateObject(-6f,-24.5f,0f);
            objects.get(6).getChildObject().get(2).inlineRotateObject(0.01f,0,0,1);
            objects.get(6).getChildObject().get(2).inlineTranslateObject(6f,24.5f,0f);


            // Restore state
            glDisableVertexAttribArray(0);

            // Poll for window events.
            // The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
        }
    }

    private void cameraTransition() {
        float acceptedOffset = 0.3f;
        if (cameraTransitionCompleted)
            return;
        if (cameraModeIsFPS) {
            // target is the eye
            Vector3f target = new Vector3f(
                    mainCharacter.getChildObject().get(2).getCenterPoint().get(0),
                    mainCharacter.getChildObject().get(2).getCenterPoint().get(1) + 1.5f,
                    mainCharacter.getChildObject().get(2).getCenterPoint().get(2));

            camera.setTargetPosition(target);
            camera.updatePosition();

            if (camera.getPosition().distance(target) < acceptedOffset) {
                cameraTransitionCompleted = true;
                System.out.println("Transition Completed!");
            }

        } else {
            // target is the body
            Vector3f target = new Vector3f(
                    mainCharacter.getChildObject().get(0).getCenterPoint().get(0),
                    mainCharacter.getChildObject().get(0).getCenterPoint().get(1) + 1.5f,
                    mainCharacter.getChildObject().get(0).getCenterPoint().get(2) + 2f);

            camera.setTargetPosition(target);
            camera.updatePosition();

            if (camera.getPosition().distance(target) < acceptedOffset) {
                cameraTransitionCompleted = true;
                System.out.println("Transition Completed!");
            }
        }

    }

    public void run() {

        init();
        loop();

        // Terminate GLFW and
        // free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public static void main(String[] args) {
        new Main().run();
    }
}