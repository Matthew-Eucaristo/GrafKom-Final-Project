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
    boolean cameraTransitionCompleted = false;

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
                .inlineTranslateObject(translate.x, translate.y, translate.z)
                .inlineScaleObject(scale.x, scale.y, scale.z)
                .inlineRotateObject((float) Math.toRadians(rotate.w), rotate.x, rotate.y, rotate.z));

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
        // take the trees in blender and create many trees as decoration
        createTree(1,null, new Vector4f(0f, 255f, 0f, 255f), new Vector4f(50f, 40f, 32f, 255f), new Vector3f(0f, 0f, 0f), 1,
                new Vector4f(1f, 0f, 0f, -90));

        // set as parent
        List<Object> trees = objects.get(2).getChildObject();

        createTree(1,trees, new Vector4f(0f, 255f, 0f, 255f), new Vector4f(50f, 40f, 32f, 255f), new Vector3f(-0.5f, 0f, 0f),
                1,
                new Vector4f(1f, 0f, 0f, -90));
        createTree(2,trees, new Vector4f(0f, 255f, 0f, 255f), new Vector4f(50f, 40f, 32f, 255f), new Vector3f(0.5f, 0f, 0f),
                1,
                new Vector4f(1f, 0f, 0f, -90));

    }
    private void createTree(int variant, List<Object> parent, Vector4f colorLeaf, Vector4f colorWood, Vector3f translate, float scaleXYZ,
            Vector4f rotate) {
        switch (variant) {
            case 1 -> {
                importObjects(shaderModuleDataList, parent, "resources/blender/tree/tree1leaf.fbx", colorLeaf, translate,
                        scaleXYZ, rotate);
                importObjects(shaderModuleDataList, parent, "resources/blender/tree/tree1wood.fbx", colorWood, translate,
                        scaleXYZ, rotate);
            }
            case 2 -> {
                importObjects(shaderModuleDataList, parent, "resources/blender/tree/tree2leaf.fbx", colorLeaf, translate,
                        scaleXYZ, rotate);
                importObjects(shaderModuleDataList, parent, "resources/blender/tree/tree2wood.fbx", colorWood, translate,
                        scaleXYZ, rotate);
            }
            case 3 -> {
                importObjects(shaderModuleDataList, parent,"resources/blender/tree/tree3leaf.fbx", colorLeaf, translate,
                        scaleXYZ, rotate);
                importObjects(shaderModuleDataList, parent,"resources/blender/tree/tree3wood.fbx", colorWood, translate,
                        scaleXYZ, rotate);
            }
            default -> {
                System.out.println("No such variant");
            }
        }

    }


    private void createMC(Object mainCharacter) {
        // create main character as chilc of the parent

        // rotations
        Vector4f rotation = new Vector4f(1f, 0f, 0f, -90f);

        // create the body
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/body.fbx",
                new Vector4f(170f, 200f, 170f, 255f), new Vector3f(0f, 0f, 0f), 1f, rotation);

        // create the head
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/head.fbx",
                new Vector4f(190f, 190f, 189f, 255f), new Vector3f(0f, 0f, 0f), 1f, rotation);

        // create the eye
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/eye.fbx",
                new Vector4f(10f, 10f, 200f, 255f), new Vector3f(0f, 0f, 0f), 1f, rotation);

        // create the hair
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/hair.fbx",
                new Vector4f(10f, 20f, 20f, 255f), new Vector3f(0f, 0f, 0f), 1f, rotation);

        // create the leg
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/leg.fbx",
                new Vector4f(100f, 255f, 255f, 255f), new Vector3f(0f, 0f, 0f), 1f, rotation);

        // create the mouth
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/mouth.fbx",
                new Vector4f(255f, 255f, 0f, 255f), new Vector3f(0f, 0f, 0f), 1f, rotation);

        // create the shirt
        importObjects(shaderModuleDataList, mainCharacter.getChildObject(), "resources/blender/mc/shirt.fbx",
                new Vector4f(255f, 0f, 255f, 255f), new Vector3f(0f, 0f, 0f), 1f, rotation);

        // rotation for all child of main character
        mainCharacter.getChildObject().forEach(object -> {
            object.inlineRotateObject((float) Math.toRadians(180), 0f, 1f, 0f);
        });

        // translation for all child of main character
        mainCharacter.getChildObject().forEach(object -> {
            object.inlineTranslateObject(0f, 1f, 0f);
        });
    }

    public void init() {
        window.init();
        GL.createCapabilities();
        mouseInput = window.getMouseInput();
        camera.setPosition(0f, 0f, 1f);

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
                .inlineScaleObjectXYZ(0.1f)
                .inlineRotateObject((float) Math.toRadians(180), 0f, 1f, 0f));

        // main character
        mainCharacter = objects.get(0);
        createMC(mainCharacter);

        // Terrain
        importObjects(shaderModuleDataList, "resources/blender/terrain.fbx", new Vector4f(58, 105, 0, 255), null, null,
                new Vector4f(1f, 0f, 0f, 90));

        // Trees
        createTrees();

        // Street lamps
        createStreetLamps();









        // Get the camera's view matrix.
        viewMatrix = camera.getViewMatrix();

    }

    public void input() {
        float cameraSpeed = 0.05f;
        float characterSpeed = 0.03f;
        float rotateSpeedInDegrees = 1f;

        // keybind for toggling FPS or TPS
        if (window.isKeyPressed(GLFW_KEY_1)) {
            if (!toggleKeyPressed) {
                toggleKeyPressed = true;

                // toggle camera transition
                cameraTransitionCompleted = false;

                // toggle camera mode
                if (cameraModeIsFPS) {
                    cameraModeIsFPS = false;
                } else {
                    cameraModeIsFPS = true;

                    // toggle camera transition
                    cameraTransitionCompleted = false;
                }
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
            glClearColor(0.0f,
                    0.0f, 0.0f,
                    0.0f);
            GL.createCapabilities();
            input();

            // code
            for (Object object : objects) {
                object.draw(camera, projection);
            }

            // update all the center point to the correct one
            objects.forEach(object -> {
                object.updateCenterPoint();
            });

            // camera transition
            cameraTransition();

            // set FPS/free
            if (cameraModeIsFPS && cameraTransitionCompleted) {
                // set to FPS mode
                Vector3f eyePosition = new Vector3f(
                        mainCharacter.getChildObject().get(2).getCenterPoint().get(0),
                        mainCharacter.getChildObject().get(2).getCenterPoint().get(1),
                        mainCharacter.getChildObject().get(2).getCenterPoint().get(2));

                // set the camera to the main character eye
                camera.setPosition(eyePosition.x, eyePosition.y + 2f, eyePosition.z);
                camera.lockInEye();

            }

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
                    mainCharacter.getChildObject().get(2).getCenterPoint().get(1),
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
                    mainCharacter.getChildObject().get(0).getCenterPoint().get(1),
                    mainCharacter.getChildObject().get(0).getCenterPoint().get(2));

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