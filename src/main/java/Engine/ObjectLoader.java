package Engine;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector4f;
import org.lwjgl.assimp.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

public class ObjectLoader extends Sphere{
    List<String> lines;
    public List<Vector3f> newVertices = new ArrayList<>();
    public List<Vector3f> newNormals = new ArrayList<>();
    public List<Vector2f> newTextures = new ArrayList<>();
    public List<Integer> newIndex = new ArrayList<>();
    AIScene scene;

    public ObjectLoader(List<ShaderModuleData> shaderModuleDataList, List<Vector3f> vertices, Vector4f color, List<Float> centerPoint, Float radiusX, Float radiusY, Float radiusZ,
                        int sectorCount, int stackCount, String fileName){
        super(shaderModuleDataList, vertices, color, centerPoint, radiusX, radiusY, radiusZ, sectorCount, stackCount);

        scene = Assimp.aiImportFile(fileName, Assimp.aiProcess_Triangulate | Assimp.aiProcess_FlipUVs);
        loadFiles();

        loadObject();
    }

    public void loadFiles(){

        int numMeshes = scene.mNumMeshes();
        for (int x = 0; x <numMeshes; x++) { //kalo ada beberapa model
            AIMesh mesh = AIMesh.create(scene.mMeshes().get(x));

            // vertices
            AIVector3D.Buffer verticesBuffer = mesh.mVertices();
            int numVertices = mesh.mNumVertices();


            for (int i = 0; i < numVertices; i++) {
                AIVector3D vertex = verticesBuffer.get(i);
                Vector3f verticesVec = new Vector3f(vertex.x(), vertex.y(), vertex.z());
                newVertices.add(verticesVec);
            }

            //  normal
            AIVector3D.Buffer normalsBuffer = mesh.mNormals();
            int numNormals = mesh.mNumVertices();

            for (int i = 0; i < numNormals; i++) {
                AIVector3D vertex = normalsBuffer.get(i);
                Vector3f verticesVec = new Vector3f(vertex.x(), vertex.y(), vertex.z());
                newNormals.add(verticesVec);
            }

            //indices
            AIFace.Buffer facesBuffer = mesh.mFaces();
            int numFaces = mesh.mNumFaces();

            for (int i = 0; i < numFaces; i++) {
                AIFace face = facesBuffer.get(i);
                int numIndices = face.mNumIndices();
                for (int j = 0; j < numIndices; j++) {
                    int index = face.mIndices().get(j);
                    newIndex.add(index);
                }
            }
        }

    }

    private void loadObject(){
        this.vertices = newVertices;
        this.normal = newNormals;
        this.index = newIndex;

        setupVAOVBO();

        ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER,
                Utils.listoInt(index), GL_STATIC_DRAW);
    }

    @Override
    public void draw(Camera camera, Projection projection) {
        drawSetup(camera, projection);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glDrawElements(GL_TRIANGLES,
                index.size(),GL_UNSIGNED_INT,
                0);

        for(Object child:childObject){
            child.draw(camera,projection);
        }
    }


}
