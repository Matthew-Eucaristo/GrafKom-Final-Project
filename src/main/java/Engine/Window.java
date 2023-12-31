package Engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    private static long window;
    private boolean open = true;
    private int width, height;
    private String title;

    private MouseInput mouseInput;

    private int x;
    private int y;


    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isOpen() {
        return open;
    }

    public static long getWindowHandle(){
        return window;
    }

    public void init() {
        //Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

        // Create the window
        window = glfwCreateWindow(width + 2, height + 2, title, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });


        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowPos(window, pWidth, pHeight);

            x = pWidth.get(0);
            y = pHeight.get(0);

            // Get the window size passed to glfwCreateWindow
            glfwGetWindowSize(window, pWidth, pHeight);



            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the window
            glfwSetWindowPos(
                    window,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);

        mouseInput = new MouseInput(window);

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
    }

    public void update() {

        // Set the clear color

        glfwSwapBuffers(window); // swap the color buffers
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
        glViewport(0, 0, width, height);

        if (glfwWindowShouldClose(window))
            open = false;

        mouseInput.input();

    }
    public void setFullscreen(boolean fullscreen) {
    long monitor = glfwGetPrimaryMonitor();
    GLFWVidMode vidmode = glfwGetVideoMode(monitor);

    if (fullscreen) {
        glfwSetWindowMonitor(window, monitor, 0, 0, vidmode.width(), vidmode.height(), vidmode.refreshRate());
        x = 0;
        y = 0;
    } else {
        glfwSetWindowMonitor(window, 0, x, y, width, height, GLFW_DONT_CARE);
    }
}


    public void cleanup() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(window, keyCode) == GLFW_PRESS;
    }

    public boolean isKeyReleased(int keyCode) {
        return glfwGetKey(window, keyCode) == GLFW_RELEASE;
    }
    public boolean isFocused() {
        return glfwGetWindowAttrib(window, GLFW_FOCUSED) == GLFW_TRUE;
    }

    public MouseInput getMouseInput() {
        return mouseInput;
    }
}
