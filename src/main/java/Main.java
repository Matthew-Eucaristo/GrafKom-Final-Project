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

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL30.*;

public class Main {
    private Window window = new Window(800, 800, "Hello World");
    private ArrayList<Object> objects = new ArrayList<>();
    private ArrayList<Object> objectsRectangle = new ArrayList<>();
    private ArrayList<Object> objectsPointsControl = new ArrayList<>();

    private MouseInput mouseInput;
    int countDegree = 0;
    Projection projection = new Projection(window.getWidth(), window.getHeight());
    Camera camera = new Camera();

    Matrix4f viewMatrix;

    ShaderProgram objectShader;

    private void createTrees(){
        // take the trees in blender and create many trees as decoration

    }

    public void init() {
        window.init();
        GL.createCapabilities();
        mouseInput = window.getMouseInput();
        camera.setPosition(0f, 0f, 1f);

        // create the shader program
        List<ShaderProgram.ShaderModuleData> shaderModuleDataList = new ArrayList<>();
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("resources/shaders/scene.vert", GL_VERTEX_SHADER));
        shaderModuleDataList.add(new ShaderProgram.ShaderModuleData("resources/shaders/scene.frag", GL_FRAGMENT_SHADER));

        objectShader = new ShaderProgram(shaderModuleDataList);
        objectShader.link();

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
                        .inlineTranslateObject(0f,0f,0f)
                        .inlineScaleObjectXYZ(1f)
                        .inlineRotateObject((float) Math.toRadians(00), 1f, 0f, 0f)
                );

        // test drive dari blender
        // Terrain
        objects.add(new ObjectLoader(
                        shaderModuleDataList,
                        new ArrayList<>(),
                        new Vector4f(0.0f, 1.0f, 0.0f, 1.0f), // color
                        Arrays.asList(0.0f, -0.5f, 0.0f),
                        0.5f,
                        0.5f,
                        0.5f,
                        36,
                        18,
                        "resources/blender/terrain.fbx" // path to the object
                )
                        .inlineTranslateObject(0f,5f,0f)
                        .inlineScaleObjectXYZ(1f)
                        .inlineRotateObject((float) Math.toRadians(90), 1f, 0f, 0f)
        );

        createTrees();

        // setup camera
        // Get the object's position.
        Vector3f objectPosition = new Vector3f(
                objects.get(0).getCenterPoint().get(0),
                objects.get(0).getCenterPoint().get(1),
                objects.get(0).getCenterPoint().get(2));

        // Set the camera's position and up vector.
        // camera.lookAt(objectPosition, new Vector3f(0, 1, 0));

        // Get the camera's view matrix.
        viewMatrix = camera.getViewMatrix();


    }

    public void input() {
        float cameraSpeed = 0.1f;
        float rotateSpeedInDegrees = 1f;

        // ini buat yang WASD
        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(cameraSpeed / 10);
        }
        if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(cameraSpeed / 10);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(cameraSpeed / 10);
        }
        if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(cameraSpeed / 10);
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

        // ini yang buat obyek utama maju mundur kiri kanan seperti third person
        // menggunakan arrow key
        // but when it rotates, move the same vector as the object.get(0)
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            objects.get(0).translateObject(0f, 0f, -cameraSpeed);
            camera.getPosition().add(0f,0f,-cameraSpeed);
            camera.recalculate();
        }
        if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            objects.get(0).translateObject(0f, 0f, cameraSpeed);
            camera.getPosition().add(0f,0f,cameraSpeed);
            camera.recalculate();
        }
        if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            objects.get(0).translateObject(-cameraSpeed, 0f, 0f);
            camera.getPosition().add(-cameraSpeed,0f,0f);
            camera.recalculate();
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            objects.get(0).translateObject(cameraSpeed, 0f, 0f);
            camera.getPosition().add(cameraSpeed,0f,0f);
            camera.recalculate();
        }
        if (window.isKeyPressed(GLFW_KEY_PAGE_UP)){
            objects.get(0).translateObject(0f, cameraSpeed, 0f);
            camera.getPosition().add(0f,cameraSpeed,0f);
            camera.recalculate();
        }
        if (window.isKeyPressed(GLFW_KEY_PAGE_DOWN)){
            objects.get(0).translateObject(0f, -cameraSpeed, 0f);
            camera.getPosition().add(0f,-cameraSpeed,0f);
            camera.recalculate();
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

            // Restore state
            glDisableVertexAttribArray(0);

            // Poll for window events.
            // The key callback above will only be
            // invoked during this call.
            glfwPollEvents();
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