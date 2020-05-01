package comp3170.demos.week9.sceneobjects;

import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

import comp3170.SceneObject;
import comp3170.Shader;

public class Cylinder extends SceneObject {

	private final float TAU = (float) (Math.PI * 2);
	private final int N_DIVISIONS = 20;
	
	private Vector3f[] vertices;
	private int vertexBuffer;

	private Vector3f[] faceNormals;
	private int faceNormalBuffer;

	private Vector3f[] vertexNormals;
	private int vertexNormalBuffer;

	private Vector3f[] barycentric;
	private int barycentricBuffer;

	private float[] colour = { 1.0f, 1.0f, 1.0f}; // white
	private float specularity = 10;
	
	private Matrix3f normalMatrix;
	private Vector4f lightDir;
	private Vector4f viewDir;
	
	
	public Cylinder(Shader shader) {
		super(shader);
	
		this.lightDir = new Vector4f();
		this.viewDir = new Vector4f();
		this.normalMatrix = new Matrix3f();
			
		createVerticesAndNormals();
        createBarycentric();
               
		this.vertexBuffer = shader.createBuffer(this.vertices);
		this.faceNormalBuffer = shader.createBuffer(this.faceNormals);
		this.vertexNormalBuffer = shader.createBuffer(this.vertexNormals);
		this.barycentricBuffer = shader.createBuffer(this.barycentric);
	}

	private void createVerticesAndNormals() {
		int nTriangles = 2 * N_DIVISIONS + 2 * N_DIVISIONS;
        this.vertices = new Vector3f[3 * nTriangles];
        this.faceNormals = new Vector3f[3 * nTriangles];
        this.vertexNormals = new Vector3f[3 * nTriangles];
        
        Vector3f p0, p1, p2, fn;
        Vector3f v10 = new Vector3f();
        Vector3f v20 = new Vector3f();

        // construct the curved side
        
        int nv = 0;
        int nfn = 0;
        int nvn = 0;
        for (int i = 0; i < N_DIVISIONS; i++) {
        	float a0 = i * TAU / N_DIVISIONS; 
        	float x0 = (float) Math.cos(a0);
        	float y0 = 0;
        	float z0 = (float) Math.sin(a0);

        	float a1 = (i+1) * TAU / N_DIVISIONS; 
        	float x1 = (float) Math.cos(a1);
        	float y1 = 1;
        	float z1 = (float) Math.sin(a1);        	

        	// Lower triangle
        	
        	p0 = new Vector3f(x1, y1, z1);
        	p1 = new Vector3f(x1, y0, z1);
        	p2 = new Vector3f(x0, y0, z0);

        	vertices[nv++] = p0;
        	vertices[nv++] = p1;
        	vertices[nv++] = p2;
        	
        	// compute face normals using cross product

        	p1.sub(p0, v10);	// v10 = p1 - p0
        	p2.sub(p0, v20);	// v20 = p2 - p0
        	fn = new Vector3f();
        	v10.cross(v20, fn);	// fn = v10 x v20;
        	
        	faceNormals[nfn++] = fn;
        	faceNormals[nfn++] = fn;
        	faceNormals[nfn++] = fn;

        	// vertex normals point straight out

        	vertexNormals[nvn++] = new Vector3f(x1, 0, z1);
        	vertexNormals[nvn++] = new Vector3f(x1, 0, z1);
        	vertexNormals[nvn++] = new Vector3f(x0, 0, z0);
        	
        	// Upper triangle
        	
        	p0 = new Vector3f(x0, y0, z0);
        	p1 = new Vector3f(x0, y1, z0);
        	p2 = new Vector3f(x1, y1, z1);

        	vertices[nv++] = p0;
        	vertices[nv++] = p1;
        	vertices[nv++] = p2;
        	
        	// compute face normals using cross product

        	p1.sub(p0, v10);	// v10 = p1 - p0
        	p2.sub(p0, v20);	// v20 = p2 - p0
        	fn = new Vector3f();
        	v10.cross(v20, fn);	// fn = v10 x v20;
        	
        	faceNormals[nfn++] = fn;
        	faceNormals[nfn++] = fn;
        	faceNormals[nfn++] = fn;

        	// vertex normals point straight out

        	vertexNormals[nvn++] = new Vector3f(x0, 0, z0);
        	vertexNormals[nvn++] = new Vector3f(x0, 0, z0);
        	vertexNormals[nvn++] = new Vector3f(x1, 0, z1);
        	
        }
        
        // construct the top and bottom
        
        for (int i = 0; i < N_DIVISIONS; i++) {
        	float a0 = i * TAU / N_DIVISIONS; 
        	float x0 = (float) Math.cos(a0);
        	float y0 = 0;
        	float z0 = (float) Math.sin(a0);

        	float a1 = (i+1) * TAU / N_DIVISIONS; 
        	float x1 = (float) Math.cos(a1);
        	float y1 = 1;
        	float z1 = (float) Math.sin(a1);        	
        	
        	// top
        	
        	vertices[nv++] = new Vector3f(0, y1, 0);
        	vertices[nv++] = new Vector3f(x1, y1, z1);
        	vertices[nv++] = new Vector3f(x0, y1, z0);

        	// all normals point straight up
        	
        	faceNormals[nfn++] = new Vector3f(0, 1, 0);
        	faceNormals[nfn++] = new Vector3f(0, 1, 0);
        	faceNormals[nfn++] = new Vector3f(0, 1, 0);

        	vertexNormals[nvn++] = new Vector3f(0, 1, 0);
        	vertexNormals[nvn++] = new Vector3f(0, 1, 0);
        	vertexNormals[nvn++] = new Vector3f(0, 1, 0);

        	// bottom
        	
        	vertices[nv++] = new Vector3f(0, y0, 0);
        	vertices[nv++] = new Vector3f(x0, y0, z0);
        	vertices[nv++] = new Vector3f(x1, y0, z1);

        	// all normals point straight down
        
        	faceNormals[nfn++] = new Vector3f(0, -1, 0);
        	faceNormals[nfn++] = new Vector3f(0, -1, 0);
        	faceNormals[nfn++] = new Vector3f(0, -1, 0);

        	vertexNormals[nvn++] = new Vector3f(0, -1, 0);
        	vertexNormals[nvn++] = new Vector3f(0, -1, 0);
        	vertexNormals[nvn++] = new Vector3f(0, -1, 0);
        }
	}

	private void createBarycentric() {
		this.barycentric = new Vector3f[vertices.length];

		Vector3f b0 = new Vector3f(1,0,0);
		Vector3f b1 = new Vector3f(0,1,0);
		Vector3f b2 = new Vector3f(0,0,1);
		
		for (int i = 0; i < vertices.length; i += 3) {
			barycentric[i] =   b0;
			barycentric[i+1] = b1;
			barycentric[i+2] = b2;
		}
	}
	
	
	public void setLightDir(Vector4f lightDir) {
		this.lightDir.set(lightDir);
	}

	public void setViewDir(Vector4f viewDir) {
		this.viewDir.set(viewDir);
	}
	
	@Override
	protected void drawSelf(Shader shader) {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		shader.setUniform("u_mvpMatrix", this.mvpMatrix);
		shader.setAttribute("a_position", this.vertexBuffer);

		if (shader.hasAttribute("a_barycentric")) {
			shader.setAttribute("a_barycentric", barycentricBuffer);
		}

		if (shader.hasAttribute("a_normal")) {
			shader.setAttribute("a_normal", vertexNormalBuffer);
//			shader.setAttribute("a_normal", faceNormalBuffer);
		}

		if (shader.hasUniform("u_normalMatrix")) {
			getWorldMatrix(this.worldMatrix);
			this.worldMatrix.normal(this.normalMatrix);			
			shader.setUniform("u_normalMatrix", normalMatrix);
		}
		
		
		if (shader.hasUniform("u_diffuseMaterial")) {
			shader.setUniform("u_diffuseMaterial", this.colour);
		}

		if (shader.hasUniform("u_specularMaterial")) {
			shader.setUniform("u_specularMaterial", this.colour);
		}

		if (shader.hasUniform("u_specularity")) {
			shader.setUniform("u_specularity", specularity);
		}

		if (shader.hasUniform("u_lightDir")) {
			shader.setUniform("u_lightDir", this.lightDir);
		}

		if (shader.hasUniform("u_viewDir")) {
			shader.setUniform("u_viewDir", this.viewDir);
		}

		
		gl.glDrawArrays(GL.GL_TRIANGLES, 0, this.vertices.length);

	}

}
