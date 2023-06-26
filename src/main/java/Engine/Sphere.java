package Engine;

import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_POLYGON;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import org.lwjgl.glfw.GLFW;




public class Sphere extends Circle {
    float radiusZ;
    int stackCount;
    int sectorCount;
    int nbo;
    Vector3f bigSpotLightDir = new Vector3f(-0.077f,-0.173f,0.062f);
    boolean xPositive = false;
    boolean yPositive = false;
    boolean zPositive = false;

    public Vector3f getLightDirection() {
        return lightDirection;
    }

    Vector3f lightDirection;


    public Sphere(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, List<Float> centerPoint, Float radiusX, Float radiusY, Float radiusZ,
                  int sectorCount, int stackCount) {
        super(shaderModuleDataList, vertices, color, centerPoint, radiusX, radiusY);
        this.radiusZ = radiusZ;
        this.stackCount = stackCount;
        this.sectorCount = sectorCount;
        createBox();
//        createSphere();
        setupVAOVBO();
    }

    public void createBox() {
        Vector3f temp = new Vector3f();
        ArrayList<Vector3f> tempVertices = new ArrayList<>();
        //TITIK 0
        temp.x = centerPoint.get(0) - radiusX / 2.0f;
        temp.y = centerPoint.get(1) + radiusY / 2.0f;
        temp.z = centerPoint.get(2) - radiusZ / 2.0f;
        tempVertices.add(temp);
        temp = new Vector3f();
        //TITIK 1
        temp.x = centerPoint.get(0) + radiusX / 2.0f;
        temp.y = centerPoint.get(1) + radiusY / 2.0f;
        temp.z = centerPoint.get(2) - radiusZ / 2.0f;
        tempVertices.add(temp);
        temp = new Vector3f();
        //TITIK 2
        temp.x = centerPoint.get(0) + radiusX / 2.0f;
        temp.y = centerPoint.get(1) - radiusY / 2.0f;
        temp.z = centerPoint.get(2) - radiusZ / 2.0f;
        tempVertices.add(temp);
        temp = new Vector3f();
        //TITIK 3
        temp.x = centerPoint.get(0) - radiusX / 2.0f;
        temp.y = centerPoint.get(1) - radiusY / 2.0f;
        temp.z = centerPoint.get(2) - radiusZ / 2.0f;
        tempVertices.add(temp);
        temp = new Vector3f();
        //TITIK 4
        temp.x = centerPoint.get(0) - radiusX / 2.0f;
        temp.y = centerPoint.get(1) + radiusY / 2.0f;
        temp.z = centerPoint.get(2) + radiusZ / 2.0f;
        tempVertices.add(temp);
        temp = new Vector3f();
        //TITIK 5
        temp.x = centerPoint.get(0) + radiusX / 2.0f;
        temp.y = centerPoint.get(1) + radiusY / 2.0f;
        temp.z = centerPoint.get(2) + radiusZ / 2.0f;
        tempVertices.add(temp);
        temp = new Vector3f();
        //TITIK 6
        temp.x = centerPoint.get(0) + radiusX / 2.0f;
        temp.y = centerPoint.get(1) - radiusY / 2.0f;
        temp.z = centerPoint.get(2) + radiusZ / 2.0f;
        tempVertices.add(temp);
        temp = new Vector3f();
        //TITIK 7
        temp.x = centerPoint.get(0) - radiusX / 2.0f;
        temp.y = centerPoint.get(1) - radiusY / 2.0f;
        temp.z = centerPoint.get(2) + radiusZ / 2.0f;
        tempVertices.add(temp);
        temp = new Vector3f();

        vertices.clear();
        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(3));

        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(2));
        vertices.add(tempVertices.get(3));

        vertices.add(tempVertices.get(4));
        vertices.add(tempVertices.get(5));
        vertices.add(tempVertices.get(7));

        vertices.add(tempVertices.get(5));
        vertices.add(tempVertices.get(6));
        vertices.add(tempVertices.get(7));

        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(4));
        vertices.add(tempVertices.get(7));

        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(3));
        vertices.add(tempVertices.get(7));

        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(5));
        vertices.add(tempVertices.get(6));

        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(2));
        vertices.add(tempVertices.get(6));

        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(1));
        vertices.add(tempVertices.get(5));

        vertices.add(tempVertices.get(0));
        vertices.add(tempVertices.get(4));
        vertices.add(tempVertices.get(5));

        vertices.add(tempVertices.get(2));
        vertices.add(tempVertices.get(3));
        vertices.add(tempVertices.get(6));

        vertices.add(tempVertices.get(3));
        vertices.add(tempVertices.get(6));
        vertices.add(tempVertices.get(7));

        normal = new ArrayList<>(Arrays.asList(
                // ini yg belakang
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),
                new Vector3f(0.0f, 0.0f, -1.0f),

                // ini yg depan
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),
                new Vector3f(0.0f, 0.0f, 1.0f),

                // ini yg kiri
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),
                new Vector3f(-1.0f, 0.0f, 0.0f),

                // ini yg kanan
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),
                new Vector3f(1.0f, 0.0f, 0.0f),

                // ini yg atas
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),
                new Vector3f(0.0f, 1.0f, 0.0f),

                // ini yg bawah
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f),
                new Vector3f(0.0f, -1.0f, 0.0f)
                ));
    }

    public void createSphere() {
        float pi = (float) Math.PI;

        float sectorStep = 2 * (float) Math.PI / sectorCount;
        float stackStep = (float) Math.PI / stackCount;
        float sectorAngle, StackAngle, x, y, z;

        for (int i = 0; i <= stackCount; ++i) {
            StackAngle = pi / 2 - i * stackStep;
            x = radiusX * (float) Math.cos(StackAngle);
            y = radiusY * (float) Math.cos(StackAngle);
            z = radiusZ * (float) Math.sin(StackAngle);

            for (int j = 0; j <= sectorCount; ++j) {
                sectorAngle = j * sectorStep;
                Vector3f temp_vector = new Vector3f();
                temp_vector.x = centerPoint.get(0) + x * (float) Math.cos(sectorAngle);
                temp_vector.y = centerPoint.get(1) + y * (float) Math.sin(sectorAngle);
                temp_vector.z = centerPoint.get(2) + z;
                vertices.add(temp_vector);
            }
        }
    }

    @Override
    public void setupVAOVBO() {
        super.setupVAOVBO();

        //set nbo
        nbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glBufferData(GL_ARRAY_BUFFER,
                Utils.listoFloat(normal),
                GL_STATIC_DRAW);

    }

    @Override
    public void drawSetup(Camera camera, Projection projection) {
        super.drawSetup(camera, projection);

        // Bind nbo
        glEnableVertexAttribArray(1);
        glBindBuffer(GL_ARRAY_BUFFER, nbo);
        glVertexAttribPointer(1, 3,
                GL_FLOAT,
                false,
                0, 0);

        // Calculate the position of the light
        Vector3f objectPosition = new Vector3f(0,0,0);
        Vector3f lightPosition = new Vector3f(10,0,0);
        float time = (float) glfwGetTime();
        float radius = 70.0f;
        float angle = (float)(1.7 * Math.PI) + time * 0.02f * (float) Math.PI;
        float lightX = (float) Math.sin(angle) * radius;
        float lightY = (float) Math.cos(angle) * radius;
        float lightZ = 0f;
        lightDirection = objectPosition.sub(lightPosition.add(lightX, lightY, lightZ)).normalize();


        // directional light
        float ambient, diffuse, specular;
        if (lightDirection.y >= 0 && lightDirection.x < 1 && lightDirection.x > -1) {
            ambient = 0.05f;
            diffuse = 0.1f;
            specular = 0.05f;
        } else {
            ambient = 0.5f;
            diffuse = 1f;
            specular = 1f;
        }
        uniformsMap.setUniform("dirLight.direction", lightDirection);
        uniformsMap.setUniform("dirLight.ambient", new Vector3f(ambient));
        uniformsMap.setUniform("dirLight.diffuse", new Vector3f(diffuse));
        uniformsMap.setUniform("dirLight.specular", new Vector3f(specular));



        // posisi pointLight
        Vector3f[] forLamps = new Vector3f[]{
            new Vector3f(12f, 10f, 0f),
        };
        for(int i = 0; i < forLamps.length; i++){
            uniformsMap.setUniform("pointLights[" + i + "].position", forLamps[i]);
            uniformsMap.setUniform("pointLights[" + i + "].ambient", new Vector3f(0.05f, 0.05f, 0.05f));
            uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f(1f, 1f, 1f));
            uniformsMap.setUniform("pointLights[" + i + "].specular", new Vector3f(1.0f, 1.0f, 1.0f));
            uniformsMap.setUniform("pointLights[" + i + "].constant", 1.0f);
            uniformsMap.setUniform("pointLights[" + i + "].linear", 0.09f);
            uniformsMap.setUniform("pointLights[" + i + "].quadratic", 0.032f);
        }
        Vector3f[] forTowerLights = new Vector3f[]{
                // urutan tower urut dari kanan sampe kiri depan
                new Vector3f(54.2f, 16f, 41.6f),
                new Vector3f(66f, 17f, 22f),
                new Vector3f(70.6f, 18.3f, -10.3f),
                new Vector3f(67.6f, 19f, -37.3f),
                new Vector3f(29.3f, 20.6f, -47.4f),
                new Vector3f(-13.8f, 18.8f, -53f),
                new Vector3f(-41.2f, 20f, -37f),
                new Vector3f(-53f, 18.2f, -9.8f),
                new Vector3f(-46.6f, 17.2f, 13.2f),
                new Vector3f(-31.4f, 16f, 40.7f),
        };
        float lightPower = 0.1f;
        if (lightDirection.y >= 0 && lightDirection.x < 1 && lightDirection.x > -1) lightPower = 7f;
        for(int i = 1; i < forTowerLights.length + 1; i++){
            uniformsMap.setUniform("pointLights[" + i + "].position", forTowerLights[i - 1]);
            uniformsMap.setUniform("pointLights[" + i + "].ambient", new Vector3f(0.05f, 0.05f, 0.05f));
            uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f((float) (Math.random() * lightPower), (float) (Math.random() * lightPower), (float) (Math.random() * lightPower)));
            uniformsMap.setUniform("pointLights[" + i + "].specular", new Vector3f(1.0f, 1.0f, 1.0f));
            uniformsMap.setUniform("pointLights[" + i + "].constant", 1.0f);
            uniformsMap.setUniform("pointLights[" + i + "].linear", 0.09f);
            uniformsMap.setUniform("pointLights[" + i + "].quadratic", 0.032f);
        }
        Vector3f[] forColourLamp = new Vector3f[]{
                new Vector3f(4.7f, 4.2f, 34.0f),
                new Vector3f(4.7f, 3.7f, 36.5f),
                new Vector3f(4.7f, 4.0f, 39.0f),
                new Vector3f(11.7f, 4.2f, 34.0f),
                new Vector3f(11.7f, 3.7f, 36.5f),
                new Vector3f(11.7f, 4.0f, 39.0f),
                new Vector3f(-37.3f, 4.2f, 5.0f),
                new Vector3f(-37.3f, 3.7f, 7.5f),
                new Vector3f(-37.3f, 4.0f, 10.0f),
                new Vector3f(-32.3f, 4.2f, 25.0f),
                new Vector3f(-32.3f, 3.7f, 27.5f),
                new Vector3f(-32.3f, 4.0f, 30.0f),
                new Vector3f(-32.3f, 8.2f, -25.0f),
                new Vector3f(-32.3f, 7.7f, -22.5f),
                new Vector3f(-32.3f, 8.0f, -20.0f),
        };
        lightPower = 3f;
        float lightColor = 0.1f;
        if (lightDirection.y >= 0 && lightDirection.x < 1 && lightDirection.x > -1) {
            lightPower = 1f ;
            lightColor = 1f;
        }
        int x = 1;
        for(int i = 11; i < forColourLamp.length + 11; i++){
            uniformsMap.setUniform("pointLights[" + i + "].position", forColourLamp[i - 11]);
            uniformsMap.setUniform("pointLights[" + i + "].ambient", new Vector3f(0.05f, 0.05f, 0.05f));
            switch (x){
                case 1 -> uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f(0.05f * lightPower,0.05f * lightPower,lightColor * lightPower));

                case 2 -> uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f(lightColor * lightPower,0.05f * lightPower,lightColor * lightPower));

                case 3 -> uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f(lightColor * lightPower,0.05f * lightPower,0.05f * lightPower));
                }
//            uniformsMap.setUniform("pointLights[" + i + "].diffuse", new Vector3f((float) (Math.random() * lightPower), (float) (Math.random() * lightPower), (float) (Math.random() * lightPower)));
            uniformsMap.setUniform("pointLights[" + i + "].specular", new Vector3f(0.1f, 0.1f, 0.1f));
            uniformsMap.setUniform("pointLights[" + i + "].constant", 1.0f);
            uniformsMap.setUniform("pointLights[" + i + "].linear", 0.09f);
            uniformsMap.setUniform("pointLights[" + i + "].quadratic", 0.032f);
            x++;
            if (x >= 4){
                x = 1;
            }
        }
        // posisi spotLight
        Vector3f _spotLightPosition = camera.getPosition();
        Vector3f _spotLightDirection = camera.getDirection();
        lightPower = 0.000f;
        if (lightDirection.y >= 0 && lightDirection.x < 1 && lightDirection.x > -1) lightPower = 10f;
        uniformsMap.setUniform("spotLight.position", _spotLightPosition);
        uniformsMap.setUniform("spotLight.direction", _spotLightDirection);
        uniformsMap.setUniform("spotLight.ambient", new Vector3f(0.0f, 0.0f, 0.0f));
        uniformsMap.setUniform("spotLight.diffuse", new Vector3f(lightPower, lightPower, lightPower));
        uniformsMap.setUniform("spotLight.specular", new Vector3f(lightPower));
        uniformsMap.setUniform("spotLight.constant", 1.0f);
        uniformsMap.setUniform("spotLight.linear", 0.09f);
        uniformsMap.setUniform("spotLight.quadratic", 0.032f);
        uniformsMap.setUniform("spotLight.cutOff", (float) Math.cos(Math.toRadians(12.5f)));
        uniformsMap.setUniform("spotLight.outerCutOff", (float) Math.cos(Math.toRadians(15.0f)));

        updateBigSpotLightDir();

        // posisi bigSpotLight
        Vector3f _bigSpotLightPosition = new Vector3f(70f,53f,-40f);
        Vector3f _bigSpotLightDirection = bigSpotLightDir;
//        System.out.println("x :"+ bigSpotLightDir.x + " y :" + bigSpotLightDir.y +" z :"+bigSpotLightDir.z);
        lightPower = 0.000f;
        if (lightDirection.y >= 0 && lightDirection.x < 1 && lightDirection.x > -1) lightPower = 400f;
        uniformsMap.setUniform("bigSpotLight.position", _bigSpotLightPosition);
        uniformsMap.setUniform("bigSpotLight.direction", _bigSpotLightDirection);
        uniformsMap.setUniform("bigSpotLight.ambient", new Vector3f(0.0f, 0.0f, 0.0f));
        uniformsMap.setUniform("bigSpotLight.diffuse", new Vector3f(lightPower, lightPower, lightPower));
        uniformsMap.setUniform("bigSpotLight.specular", new Vector3f(lightPower));
        uniformsMap.setUniform("bigSpotLight.constant", 1.0f);
        uniformsMap.setUniform("bigSpotLight.linear", 0.09f);
        uniformsMap.setUniform("bigSpotLight.quadratic", 0.032f);
        uniformsMap.setUniform("bigSpotLight.cutOff", (float) Math.cos(Math.toRadians(12.5f)));
        uniformsMap.setUniform("bigSpotLight.outerCutOff", (float) Math.cos(Math.toRadians(15.0f)));


        // posisi monsterSpotLight
        Vector3f _monsterSpotLightPosition = new Vector3f(15.6f, 29.6f, 44f);
        Vector3f _monsterSpotLightDirection = new Vector3f(0, 96, -40);
        lightPower = 0.000f;
        if (lightDirection.y >= 0 && lightDirection.x < 1 && lightDirection.x > -1) lightPower = 10f;
        uniformsMap.setUniform("monsterSpotLight.position", _monsterSpotLightPosition);
        uniformsMap.setUniform("monsterSpotLight.direction", _monsterSpotLightDirection);
        uniformsMap.setUniform("monsterSpotLight.ambient", new Vector3f(0.0f, 0.0f, 0.0f));
        uniformsMap.setUniform("monsterSpotLight.diffuse", new Vector3f(lightPower, lightPower, lightPower));
        uniformsMap.setUniform("monsterSpotLight.specular", new Vector3f(lightPower));
        uniformsMap.setUniform("monsterSpotLight.constant", 1.0f);
        uniformsMap.setUniform("monsterSpotLight.linear", 0.2f);
        uniformsMap.setUniform("monsterSpotLight.quadratic", 0.05f);
        uniformsMap.setUniform("monsterSpotLight.cutOff", (float) Math.cos(Math.toRadians(12.5f)));
        uniformsMap.setUniform("monsterSpotLight.outerCutOff", (float) Math.cos(Math.toRadians(15.0f)));



        uniformsMap.setUniform("viewPos", camera.getPosition());
    }
    public void updateBigSpotLightDir(){
        if(bigSpotLightDir.x < -0.178f){
            xPositive = true;
        }
        if(bigSpotLightDir.x > -0.049f){
            xPositive = false;}
        if(bigSpotLightDir.y < -0.123f){
            yPositive = true;
        }
        if(bigSpotLightDir.y > -0.084){
            yPositive = false;
        }
        if(bigSpotLightDir.z < 0.062f){
            zPositive = true;
        }
        if(bigSpotLightDir.z > 0.151){
            zPositive = false;
        }
        if (xPositive){
            bigSpotLightDir.x += 0.001;
        }else {
            bigSpotLightDir.x -= 0.001;
        }
        if (yPositive){
            bigSpotLightDir.y += 0.001;
        }else {
            bigSpotLightDir.y -= 0.001;
        }
        if (zPositive){
            bigSpotLightDir.z += 0.001;
        }else {
            bigSpotLightDir.z -= 0.001;
        }
    }
}
