package comp3170;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL.GL_UNSIGNED_INT;
import static com.jogamp.opengl.GL2ES2.GL_ACTIVE_ATTRIBUTES;
import static com.jogamp.opengl.GL2ES2.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH;
import static com.jogamp.opengl.GL2ES2.GL_ACTIVE_UNIFORMS;
import static com.jogamp.opengl.GL2ES2.GL_ACTIVE_UNIFORM_MAX_LENGTH;
import static com.jogamp.opengl.GL2ES2.GL_BOOL;
import static com.jogamp.opengl.GL2ES2.GL_BOOL_VEC2;
import static com.jogamp.opengl.GL2ES2.GL_BOOL_VEC3;
import static com.jogamp.opengl.GL2ES2.GL_BOOL_VEC4;
import static com.jogamp.opengl.GL2ES2.GL_COMPILE_STATUS;
import static com.jogamp.opengl.GL2ES2.GL_FLOAT_MAT2;
import static com.jogamp.opengl.GL2ES2.GL_FLOAT_MAT3;
import static com.jogamp.opengl.GL2ES2.GL_FLOAT_MAT4;
import static com.jogamp.opengl.GL2ES2.GL_FLOAT_VEC2;
import static com.jogamp.opengl.GL2ES2.GL_FLOAT_VEC3;
import static com.jogamp.opengl.GL2ES2.GL_FLOAT_VEC4;
import static com.jogamp.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2ES2.GL_INFO_LOG_LENGTH;
import static com.jogamp.opengl.GL2ES2.GL_INT;
import static com.jogamp.opengl.GL2ES2.GL_INT_SAMPLER_2D_MULTISAMPLE;
import static com.jogamp.opengl.GL2ES2.GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY;
import static com.jogamp.opengl.GL2ES2.GL_INT_VEC2;
import static com.jogamp.opengl.GL2ES2.GL_INT_VEC3;
import static com.jogamp.opengl.GL2ES2.GL_INT_VEC4;
import static com.jogamp.opengl.GL2ES2.GL_LINK_STATUS;
import static com.jogamp.opengl.GL2ES2.GL_SAMPLER_2D;
import static com.jogamp.opengl.GL2ES2.GL_SAMPLER_2D_MULTISAMPLE;
import static com.jogamp.opengl.GL2ES2.GL_SAMPLER_2D_MULTISAMPLE_ARRAY;
import static com.jogamp.opengl.GL2ES2.GL_SAMPLER_2D_SHADOW;
import static com.jogamp.opengl.GL2ES2.GL_SAMPLER_3D;
import static com.jogamp.opengl.GL2ES2.GL_SAMPLER_CUBE;
import static com.jogamp.opengl.GL2ES2.GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE;
import static com.jogamp.opengl.GL2ES2.GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY;
import static com.jogamp.opengl.GL2ES2.GL_VERTEX_SHADER;
import static com.jogamp.opengl.GL2ES3.GL_FLOAT_MAT2x3;
import static com.jogamp.opengl.GL2ES3.GL_FLOAT_MAT2x4;
import static com.jogamp.opengl.GL2ES3.GL_FLOAT_MAT3x2;
import static com.jogamp.opengl.GL2ES3.GL_FLOAT_MAT3x4;
import static com.jogamp.opengl.GL2ES3.GL_FLOAT_MAT4x2;
import static com.jogamp.opengl.GL2ES3.GL_FLOAT_MAT4x3;
import static com.jogamp.opengl.GL2ES3.GL_IMAGE_2D;
import static com.jogamp.opengl.GL2ES3.GL_IMAGE_2D_ARRAY;
import static com.jogamp.opengl.GL2ES3.GL_IMAGE_3D;
import static com.jogamp.opengl.GL2ES3.GL_IMAGE_BUFFER;
import static com.jogamp.opengl.GL2ES3.GL_IMAGE_CUBE;
import static com.jogamp.opengl.GL2ES3.GL_INT_IMAGE_2D;
import static com.jogamp.opengl.GL2ES3.GL_INT_IMAGE_2D_ARRAY;
import static com.jogamp.opengl.GL2ES3.GL_INT_IMAGE_3D;
import static com.jogamp.opengl.GL2ES3.GL_INT_IMAGE_BUFFER;
import static com.jogamp.opengl.GL2ES3.GL_INT_IMAGE_CUBE;
import static com.jogamp.opengl.GL2ES3.GL_INT_SAMPLER_2D;
import static com.jogamp.opengl.GL2ES3.GL_INT_SAMPLER_2D_ARRAY;
import static com.jogamp.opengl.GL2ES3.GL_INT_SAMPLER_3D;
import static com.jogamp.opengl.GL2ES3.GL_INT_SAMPLER_BUFFER;
import static com.jogamp.opengl.GL2ES3.GL_INT_SAMPLER_CUBE;
import static com.jogamp.opengl.GL2ES3.GL_SAMPLER_2D_ARRAY;
import static com.jogamp.opengl.GL2ES3.GL_SAMPLER_2D_ARRAY_SHADOW;
import static com.jogamp.opengl.GL2ES3.GL_SAMPLER_BUFFER;
import static com.jogamp.opengl.GL2ES3.GL_SAMPLER_CUBE_SHADOW;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_ATOMIC_COUNTER;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_IMAGE_2D;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_IMAGE_2D_ARRAY;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_IMAGE_3D;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_IMAGE_BUFFER;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_IMAGE_CUBE;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_SAMPLER_2D;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_SAMPLER_2D_ARRAY;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_SAMPLER_3D;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_SAMPLER_BUFFER;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_SAMPLER_CUBE;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_VEC2;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_VEC3;
import static com.jogamp.opengl.GL2ES3.GL_UNSIGNED_INT_VEC4;
import static com.jogamp.opengl.GL2GL3.GL_DOUBLE;
import static com.jogamp.opengl.GL2GL3.GL_INT_SAMPLER_1D;
import static com.jogamp.opengl.GL2GL3.GL_INT_SAMPLER_1D_ARRAY;
import static com.jogamp.opengl.GL2GL3.GL_INT_SAMPLER_2D_RECT;
import static com.jogamp.opengl.GL2GL3.GL_SAMPLER_1D;
import static com.jogamp.opengl.GL2GL3.GL_SAMPLER_1D_ARRAY;
import static com.jogamp.opengl.GL2GL3.GL_SAMPLER_1D_ARRAY_SHADOW;
import static com.jogamp.opengl.GL2GL3.GL_SAMPLER_1D_SHADOW;
import static com.jogamp.opengl.GL2GL3.GL_SAMPLER_2D_RECT;
import static com.jogamp.opengl.GL2GL3.GL_SAMPLER_2D_RECT_SHADOW;
import static com.jogamp.opengl.GL2GL3.GL_UNSIGNED_INT_SAMPLER_1D;
import static com.jogamp.opengl.GL2GL3.GL_UNSIGNED_INT_SAMPLER_1D_ARRAY;
import static com.jogamp.opengl.GL2GL3.GL_UNSIGNED_INT_SAMPLER_2D_RECT;
import static com.jogamp.opengl.GL3.GL_DOUBLE_MAT2;
import static com.jogamp.opengl.GL3.GL_DOUBLE_MAT2x3;
import static com.jogamp.opengl.GL3.GL_DOUBLE_MAT2x4;
import static com.jogamp.opengl.GL3.GL_DOUBLE_MAT3;
import static com.jogamp.opengl.GL3.GL_DOUBLE_MAT3x2;
import static com.jogamp.opengl.GL3.GL_DOUBLE_MAT3x4;
import static com.jogamp.opengl.GL3.GL_DOUBLE_MAT4;
import static com.jogamp.opengl.GL3.GL_DOUBLE_MAT4x2;
import static com.jogamp.opengl.GL3.GL_DOUBLE_MAT4x3;
import static com.jogamp.opengl.GL3.GL_DOUBLE_VEC2;
import static com.jogamp.opengl.GL3.GL_DOUBLE_VEC3;
import static com.jogamp.opengl.GL3.GL_DOUBLE_VEC4;
import static com.jogamp.opengl.GL3.GL_IMAGE_1D;
import static com.jogamp.opengl.GL3.GL_IMAGE_1D_ARRAY;
import static com.jogamp.opengl.GL3.GL_IMAGE_2D_MULTISAMPLE;
import static com.jogamp.opengl.GL3.GL_IMAGE_2D_MULTISAMPLE_ARRAY;
import static com.jogamp.opengl.GL3.GL_IMAGE_2D_RECT;
import static com.jogamp.opengl.GL3.GL_INT_IMAGE_1D;
import static com.jogamp.opengl.GL3.GL_INT_IMAGE_1D_ARRAY;
import static com.jogamp.opengl.GL3.GL_INT_IMAGE_2D_MULTISAMPLE;
import static com.jogamp.opengl.GL3.GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY;
import static com.jogamp.opengl.GL3.GL_INT_IMAGE_2D_RECT;
import static com.jogamp.opengl.GL3.GL_UNSIGNED_INT_IMAGE_1D;
import static com.jogamp.opengl.GL3.GL_UNSIGNED_INT_IMAGE_1D_ARRAY;
import static com.jogamp.opengl.GL3.GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE;
import static com.jogamp.opengl.GL3.GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY;
import static com.jogamp.opengl.GL3.GL_UNSIGNED_INT_IMAGE_2D_RECT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;

public class Shader {

	private int program;
	private int vao;
	private Map<String, Integer> attributes;
	private Map<String, Integer> attributeTypes;
	private Map<String, Integer> uniforms;
	private Map<String, Integer> uniformTypes;
	private Map<Integer, Integer> bufferTypes;

	private FloatBuffer matrix2Buffer = Buffers.newDirectFloatBuffer(4);
	private FloatBuffer matrix3Buffer = Buffers.newDirectFloatBuffer(9);
	private FloatBuffer matrix4Buffer = Buffers.newDirectFloatBuffer(16);

	private FloatBuffer vector2Buffer = Buffers.newDirectFloatBuffer(2);
	private FloatBuffer vector3Buffer = Buffers.newDirectFloatBuffer(3);
	private FloatBuffer vector4Buffer = Buffers.newDirectFloatBuffer(16);

	/**
	 * Compile and link a vertex and fragment shader
	 * 
	 * @param vertexShaderFile
	 * @param fragmentShaderFile
	 * @throws IOException
	 * @throws GLException
	 */

	public Shader(File vertexShaderFile, File fragmentShaderFile) throws IOException, GLException {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		// compile the shaders

		int vertexShader = compileShader(GL_VERTEX_SHADER, vertexShaderFile);
		int fragmentShader = compileShader(GL_FRAGMENT_SHADER, fragmentShaderFile);

		// link the shaders

		this.program = gl.glCreateProgram();
		gl.glAttachShader(this.program, vertexShader);
		gl.glAttachShader(this.program, fragmentShader);
		gl.glLinkProgram(program);
		GLException.checkGLErrors();

		// check for linker errors

		int[] linked = new int[1];
		gl.glGetProgramiv(this.program, GL_LINK_STATUS, linked, 0);
		if (linked[0] != 1) {
			int[] maxlen = new int[1];
			int[] len = new int[1];
			byte[] log = null;
			String logString = "";

			// determine length of the program compilation log
			gl.glGetProgramiv(this.program, GL_INFO_LOG_LENGTH, maxlen, 0);

			if (maxlen[0] > 0) {
				log = new byte[maxlen[0]];

				gl.glGetProgramInfoLog(this.program, maxlen[0], len, 0, log, 0);
				logString = new String(log);
			}

			String message = String.format("Link failed:\n", logString);
			throw new GLException(message);
		}

		// create VAO

		int buffer[] = new int[1];
		gl.glGenVertexArrays(1, buffer, 0);
		this.vao = buffer[0];

		this.bufferTypes = new HashMap<Integer, Integer>();

		// record attribute and uniforms

		recordAttributes();
		recordUniforms();
	}

	/**
	 * Check if the shader has a particular attribute
	 * 
	 * @param name
	 * @return true if the shader has an attribute with the name provide
	 */

	public boolean hasAttribute(String name) {
		return this.attributes.containsKey(name);
	}

	/**
	 * Check if the shader has a particular uniform
	 * 
	 * @param name
	 * @return true if the shader has a uniform with the name provide
	 */

	public boolean hasUniform(String name) {
		return this.uniforms.containsKey(name);
	}

	/**
	 * Get the handle for an attribute
	 * 
	 * @param name
	 * @return
	 */

	public int getAttribute(String name) {
		if (!this.attributes.containsKey(name)) {
			throw new IllegalArgumentException(String.format("Unknown attribute: '%s'", name));
		}

		return this.attributes.get(name);
	}

	/**
	 * Get the handle for a uniform
	 * 
	 * @param name
	 * @return
	 */

	public int getUniform(String name) {
		if (!this.uniforms.containsKey(name)) {
			throw new IllegalArgumentException(String.format("Unknown uniform: '%s'", name));
		}

		return this.uniforms.get(name);
	}

	/**
	 * Enable the shader
	 */

	public void enable() {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		gl.glUseProgram(this.program);
		gl.glBindVertexArray(this.vao);
	}

	/**
	 * Create a new VBO (vertex buffer object) in graphics memory and copy data into
	 * it
	 * 
	 * @param data The data as an array of floats
	 * @parem type The type of data in this buffer
	 * @return
	 */
	public int createBuffer(float[] data, int type) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int[] buffers = new int[1];
		gl.glGenBuffers(buffers.length, buffers, 0);

		FloatBuffer buffer = Buffers.newDirectFloatBuffer(data);
		gl.glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
		gl.glBufferData(GL_ARRAY_BUFFER, data.length * Buffers.SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

		this.bufferTypes.put(buffers[0], type);
		if (data.length % typeSize(type) != 0) {
			System.err.println(
					String.format("Warning: buffer of type %s has length which is not a mutliple of %d.",
					typeName(type), typeSize(type)));
		}

		return buffers[0];
	}

	/**
	 * Create a new VBO (vertex buffer object) in graphics memory and copy data into
	 * it from a FloatBuffer
	 * 
	 * @param buffer A FloatBuffer containing the data
	 * @param type The type of data in this buffer
	 * @return
	 */
	public int createBuffer(FloatBuffer buffer, int type) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int[] buffers = new int[1];
		gl.glGenBuffers(buffers.length, buffers, 0);

		gl.glBindBuffer(GL_ARRAY_BUFFER, buffers[0]);
		gl.glBufferData(GL_ARRAY_BUFFER, buffer.limit() * Buffers.SIZEOF_FLOAT, buffer, GL_STATIC_DRAW);

		this.bufferTypes.put(buffers[0], type);
		if (buffer.limit() % typeSize(type) != 0) {
			System.err.println(
					String.format("Warning: buffer of type %s has length which is not a mutliple of %d.",
					typeName(type), typeSize(type)));
		}

		return buffers[0];
	}

	/**
	 * Create a new VBO (vertex buffer object) in graphics memory and copy data into
	 * it
	 * 
	 * @param data The data as an array of Vector2f
	 * @return
	 */
	public int createBuffer(Vector2f[] data) {
		// this is a hack, but I can't get it to work otherwise
		float[] array = new float[2 * data.length];
		int j = 0;
		for (int i = 0; i < data.length; i++) {
			array[j++] = data[i].x;
			array[j++] = data[i].y;
		}

		return createBuffer(array, GL_FLOAT_VEC2);
	}

	/**
	 * Create a new VBO (vertex buffer object) in graphics memory and copy data into
	 * it
	 * 
	 * @param data The data as an array of Vector3f
	 * @return
	 */
	public int createBuffer(Vector3f[] data) {
		// this is a hack, but I can't get it to work otherwise
		float[] array = new float[3 * data.length];
		int j = 0;
		for (int i = 0; i < data.length; i++) {
			array[j++] = data[i].x;
			array[j++] = data[i].y;
			array[j++] = data[i].z;
		}

		return createBuffer(array, GL_FLOAT_VEC3);
	}

	/**
	 * Create a new VBO (vertex buffer object) in graphics memory and copy data into
	 * it. 
	 * 
	 * @param data The data as an array of Vector4f
	 * @return
	 */
	public int createBuffer(Vector4f[] data) {
		// this is a hack, but I can't get it to work otherwise
		float[] array = new float[4 * data.length];
		int j = 0;
		for (int i = 0; i < data.length; i++) {
			array[j++] = data[i].x;
			array[j++] = data[i].y;
			array[j++] = data[i].z;
			array[j++] = data[i].w;
		}

		return createBuffer(array, GL_FLOAT_VEC4);
	}

	/**
	 * Create a new index buffer and initialise it
	 * 
	 * @param indices The indices as an array of ints
	 * @return
	 */
	public int createIndexBuffer(int[] indices) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int[] buffers = new int[1];
		gl.glGenBuffers(buffers.length, buffers, 0);

		IntBuffer buffer = Buffers.newDirectIntBuffer(indices);
		gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, buffers[0]);
		gl.glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices.length * Buffers.SIZEOF_INT, buffer, GL_STATIC_DRAW);

		this.bufferTypes.put(buffers[0], GL_INT);

		return buffers[0];
	}
	
	/**
	 * Create a render texture with the specified dimensions
	 * 
	 * @param width
	 * @param height
	 * @return
	 */

	public static int createRenderTexture(int width, int height) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int[] renderTexture = new int[1];
		gl.glGenTextures(1, renderTexture, 0);
		gl.glBindTexture(GL.GL_TEXTURE_2D, renderTexture[0]);
		gl.glTexImage2D(GL.GL_TEXTURE_2D, 0, GL.GL_RGB, width, height, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, null);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		
		return renderTexture[0];
	}
	
	/**
	 * Create a framebuffer that writes colours to the renderTexture given.
	 * 
	 * @param renderTexture	A rendertexture in which to store the colour buffer
	 * @return
	 * @throws GLException
	 */
	
	public static int createFrameBuffer(int renderTexture) throws GLException {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		int[] width = new int[1];
		int[] height = new int[1];
		gl.glGetTexLevelParameteriv(GL.GL_TEXTURE_2D, 0, GL4.GL_TEXTURE_WIDTH, width, 0);
		gl.glGetTexLevelParameteriv(GL.GL_TEXTURE_2D, 0, GL4.GL_TEXTURE_HEIGHT, height, 0);
		
		int[] framebufferName = new int[1];
		gl.glGenFramebuffers(1, framebufferName, 0);
		gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, framebufferName[0]);		
		
		int[] depthrenderbuffer = new int[1];
		gl.glGenRenderbuffers(1, depthrenderbuffer, 0);
		gl.glBindRenderbuffer(GL.GL_RENDERBUFFER, depthrenderbuffer[0]);
		gl.glRenderbufferStorage(GL.GL_RENDERBUFFER, GL4.GL_DEPTH_COMPONENT, width[0], height[0]);
		gl.glFramebufferRenderbuffer(GL.GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, GL.GL_RENDERBUFFER, depthrenderbuffer[0]);

		gl.glFramebufferTexture(GL.GL_FRAMEBUFFER, GL.GL_COLOR_ATTACHMENT0, renderTexture, 0);

		int[] drawBuffers = new int[] { GL4.GL_COLOR_ATTACHMENT0 };
		gl.glDrawBuffers(drawBuffers.length, drawBuffers, 0);
		
		if (gl.glCheckFramebufferStatus(GL.GL_FRAMEBUFFER) != GL.GL_FRAMEBUFFER_COMPLETE) {
			GLException.checkGLErrors();
			throw new GLException("Failed to create framebuffer");
		}

		return framebufferName[0];
	}
	
	/**
	 * Connect a buffer to a shader attribute
	 * 
	 * @param attributeName The name of the shader attribute
	 * @param buffer        The buffer
	 */
	public void setAttribute(String attributeName, int buffer) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int attribute = getAttribute(attributeName);
		int type = attributeTypes.get(attributeName);

		if (bufferTypes.get(buffer) != type) {
			throw new IllegalArgumentException(String.format("Expected buffer of type %s, got %s.", typeName(type),
					typeName(bufferTypes.get(buffer))));
		}

		int size = typeSize(type);
		int elementType = elementType(type);

		gl.glBindBuffer(GL_ARRAY_BUFFER, buffer);
		gl.glVertexAttribPointer(attribute, size, elementType, false, 0, 0);
		gl.glEnableVertexAttribArray(attribute);
	}

	/**
	 * Set the value of a uniform to an int
	 * 
	 * @param uniformName The GLSL uniform
	 * @param value       The int value
	 */
	public void setUniform(String uniformName, int value) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int uniform = getUniform(uniformName);
		int type = uniformTypes.get(uniformName);

		switch (type) {
		case GL_UNSIGNED_INT:
			gl.glUniform1ui(uniform, value);
			break;
		case GL_INT:
		case GL_SAMPLER_2D:
			gl.glUniform1i(uniform, value);
			break;			
		default:
			throw new IllegalArgumentException(String.format("Expected %s got int", typeName(type)));			
		}
	
	}

	/**
	/**
	 * Set the value of a uniform to a float
	 * 
	 * @param uniformName The GLSL uniform
	 * @param value       The float value
	 */
	public void setUniform(String uniformName, float value) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int uniform = getUniform(uniformName);
		int type = uniformTypes.get(uniformName);

		if (type != GL_FLOAT) {
			throw new IllegalArgumentException(String.format("Expected %s got float", typeName(type)));
		}

		gl.glUniform1f(uniform, value);
	}

	/**
	 * Set the value of a uniform to an array of floats
	 * 
	 * This works for GLSL types float, vec2, vec3, vec4, mat2, mat3 and mat4.
	 * 
	 * Note that for matrix types, the elements should be specified in column order
	 * 
	 * @param uniformName
	 * @param value
	 */
	public void setUniform(String uniformName, float[] value) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int uniform = getUniform(uniformName);
		int type = uniformTypes.get(uniformName);

		int expectedArgs = typeSize(type);

		if (value.length != expectedArgs) {
			throw new IllegalArgumentException(
					String.format("Expected %s got float[%d]", typeName(type), value.length));
		}

		switch (type) {
		case GL_FLOAT:
			gl.glUniform1f(uniform, value[0]);
			break;
		case GL_FLOAT_VEC2:
			gl.glUniform2f(uniform, value[0], value[1]);
			break;
		case GL_FLOAT_VEC3:
			gl.glUniform3f(uniform, value[0], value[1], value[2]);
			break;
		case GL_FLOAT_VEC4:
			gl.glUniform4f(uniform, value[0], value[1], value[2], value[3]);
			break;
		case GL_FLOAT_MAT2:
			gl.glUniformMatrix2fv(uniform, 1, false, value, 0);
			break;
		case GL_FLOAT_MAT3:
			gl.glUniformMatrix3fv(uniform, 1, false, value, 0);
			break;
		case GL_FLOAT_MAT4:
			gl.glUniformMatrix4fv(uniform, 1, false, value, 0);
			break;
		}

	}

	/**
	 * Set a uniform of type vec2 to a Vector2f value
	 * 
	 * @param uniformName the uniform to set
	 * @param vector      the vector value to send
	 */

	public void setUniform(String uniformName, Vector2f vector) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int uniform = getUniform(uniformName);
		int type = uniformTypes.get(uniformName);

		if (type != GL_FLOAT_VEC2) {
			throw new IllegalArgumentException(String.format("Expected %s got Vector2f", typeName(type)));
		}

		gl.glUniform2fv(uniform, 1, vector.get(vector2Buffer));
	}

	/**
	 * Set a uniform of type vec3 to a Vector3f value
	 * 
	 * @param uniformName the uniform to set
	 * @param vector      the vector value to send
	 */

	public void setUniform(String uniformName, Vector3f vector) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int uniform = getUniform(uniformName);
		int type = uniformTypes.get(uniformName);

		if (type != GL_FLOAT_VEC3) {
			throw new IllegalArgumentException(String.format("Expected %s got Vector3f", typeName(type)));
		}

		gl.glUniform3fv(uniform, 1, vector.get(vector3Buffer));
	}

	/**
	 * Set a uniform of type vec4 to a Vector4f value
	 * 
	 * @param uniformName the uniform to set
	 * @param vector      the vector value to send
	 */

	public void setUniform(String uniformName, Vector4f vector) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int uniform = getUniform(uniformName);
		int type = uniformTypes.get(uniformName);

		if (type != GL_FLOAT_VEC4) {
			throw new IllegalArgumentException(String.format("Expected %s got Vector4f", typeName(type)));
		}

		gl.glUniform4fv(uniform, 1, vector.get(vector4Buffer));
	}

	/**
	 * Set a uniform of type mat2 to a Matrix2f value
	 * 
	 * @param uniformName the uniform to set
	 * @param matrix      the matrix value to send
	 */

	public void setUniform(String uniformName, Matrix2f matrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int uniform = getUniform(uniformName);
		int type = uniformTypes.get(uniformName);

		if (type != GL_FLOAT_MAT2) {
			throw new IllegalArgumentException(String.format("Expected %s got Matrix2f", typeName(type)));
		}

		gl.glUniformMatrix2fv(uniform, 1, false, matrix.get(matrix2Buffer));
	}

	/**
	 * Set a uniform of type mat3 to a Matrix3f value
	 * 
	 * @param uniformName the uniform to set
	 * @param matrix      the matrix value to send
	 */

	public void setUniform(String uniformName, Matrix3f matrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int uniform = getUniform(uniformName);
		int type = uniformTypes.get(uniformName);

		if (type != GL_FLOAT_MAT3) {
			throw new IllegalArgumentException(String.format("Expected %s got Matrix3f", typeName(type)));
		}

		gl.glUniformMatrix3fv(uniform, 1, false, matrix.get(matrix3Buffer));
	}

	/**
	 * Set a uniform of type mat4 to a Matrix4 value
	 * 
	 * @param uniformName the uniform to set
	 * @param matrix      the matrix value to send
	 */

	public void setUniform(String uniformName, Matrix4f matrix) {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		int uniform = getUniform(uniformName);
		int type = uniformTypes.get(uniformName);

		if (type != GL_FLOAT_MAT4) {
			throw new IllegalArgumentException(String.format("Expected %s got Matrix4f", typeName(type)));
		}

		gl.glUniformMatrix4fv(uniform, 1, false, matrix.get(matrix4Buffer));
	}

	// ===================
	// PRIVATE METHODS
	// ===================
	
	
	private int typeSize(int type) {
		switch (type) {
		case GL_FLOAT:
			return 1;
		case GL_FLOAT_VEC2:
			return 2;
		case GL_FLOAT_VEC3:
			return 3;
		case GL_FLOAT_VEC4:
			return 4;
		case GL_FLOAT_MAT2:
			return 4;
		case GL_FLOAT_MAT3:
			return 9;
		case GL_FLOAT_MAT4:
			return 16;
		default:
			throw new UnsupportedOperationException(
					String.format("Unsupported GLSL attribute type: %s", typeName(type)));
		}

	}

	private int elementType(int type) {
		switch (type) {
		case GL_INT:
			return GL_INT;
		case GL_FLOAT:
		case GL_FLOAT_VEC2:
		case GL_FLOAT_VEC3:
		case GL_FLOAT_VEC4:
		case GL_FLOAT_MAT2:
		case GL_FLOAT_MAT3:
		case GL_FLOAT_MAT4:
			return GL_FLOAT;
		default:
			throw new UnsupportedOperationException(
					String.format("Unsupported GLSL attribute type: %s", typeName(type)));
		}

	}

	private String typeName(int type) {
		switch (type) {
		case GL_FLOAT:
			return "float";

		case GL_FLOAT_VEC2:
			return "vec2";

		case GL_FLOAT_VEC3:
			return "vec3";

		case GL_FLOAT_VEC4:
			return "vec4";

		case GL_DOUBLE:
			return "double";

		case GL_DOUBLE_VEC2:
			return "dvec2";

		case GL_DOUBLE_VEC3:
			return "dvec3";

		case GL_DOUBLE_VEC4:
			return "dvec4";

		case GL_INT:
			return "int";

		case GL_INT_VEC2:
			return "ivec2";

		case GL_INT_VEC3:
			return "ivec3";

		case GL_INT_VEC4:
			return "ivec4";

		case GL_UNSIGNED_INT:
			return "unsigned int";

		case GL_UNSIGNED_INT_VEC2:
			return "uvec2";

		case GL_UNSIGNED_INT_VEC3:
			return "uvec3";

		case GL_UNSIGNED_INT_VEC4:
			return "uvec4";

		case GL_BOOL:
			return "bool";

		case GL_BOOL_VEC2:
			return "bvec2";

		case GL_BOOL_VEC3:
			return "bvec3";

		case GL_BOOL_VEC4:
			return "bvec4";

		case GL_FLOAT_MAT2:
			return "mat2";

		case GL_FLOAT_MAT3:
			return "mat3";

		case GL_FLOAT_MAT4:
			return "mat4";

		case GL_FLOAT_MAT2x3:
			return "mat2x3";

		case GL_FLOAT_MAT2x4:
			return "mat2x4";

		case GL_FLOAT_MAT3x2:
			return "mat3x2";

		case GL_FLOAT_MAT3x4:
			return "mat3x4";

		case GL_FLOAT_MAT4x2:
			return "mat4x2";

		case GL_FLOAT_MAT4x3:
			return "mat4x3";

		case GL_DOUBLE_MAT2:
			return "dmat2";

		case GL_DOUBLE_MAT3:
			return "dmat3";

		case GL_DOUBLE_MAT4:
			return "dmat4";

		case GL_DOUBLE_MAT2x3:
			return "dmat2x3";

		case GL_DOUBLE_MAT2x4:
			return "dmat2x4";

		case GL_DOUBLE_MAT3x2:
			return "dmat3x2";

		case GL_DOUBLE_MAT3x4:
			return "dmat3x4";

		case GL_DOUBLE_MAT4x2:
			return "dmat4x2";

		case GL_DOUBLE_MAT4x3:
			return "dmat4x3";

		case GL_SAMPLER_1D:
			return "sampler1D";

		case GL_SAMPLER_2D:
			return "sampler2D";

		case GL_SAMPLER_3D:
			return "sampler3D";

		case GL_SAMPLER_CUBE:
			return "samplerCube";

		case GL_SAMPLER_1D_SHADOW:
			return "sampler1DShadow";

		case GL_SAMPLER_2D_SHADOW:
			return "sampler2DShadow";

		case GL_SAMPLER_1D_ARRAY:
			return "sampler1DArray";

		case GL_SAMPLER_2D_ARRAY:
			return "sampler2DArray";

		case GL_SAMPLER_1D_ARRAY_SHADOW:
			return "sampler1DArrayShadow";

		case GL_SAMPLER_2D_ARRAY_SHADOW:
			return "sampler2DArrayShadow";

		case GL_SAMPLER_2D_MULTISAMPLE:
			return "sampler2DMS";

		case GL_SAMPLER_2D_MULTISAMPLE_ARRAY:
			return "sampler2DMSArray";

		case GL_SAMPLER_CUBE_SHADOW:
			return "samplerCubeShadow";

		case GL_SAMPLER_BUFFER:
			return "samplerBuffer";

		case GL_SAMPLER_2D_RECT:
			return "sampler2DRect";

		case GL_SAMPLER_2D_RECT_SHADOW:
			return "sampler2DRectShadow";

		case GL_INT_SAMPLER_1D:
			return "isampler1D";

		case GL_INT_SAMPLER_2D:
			return "isampler2D";

		case GL_INT_SAMPLER_3D:
			return "isampler3D";

		case GL_INT_SAMPLER_CUBE:
			return "isamplerCube";

		case GL_INT_SAMPLER_1D_ARRAY:
			return "isampler1DArray";

		case GL_INT_SAMPLER_2D_ARRAY:
			return "isampler2DArray";

		case GL_INT_SAMPLER_2D_MULTISAMPLE:
			return "isampler2DMS";

		case GL_INT_SAMPLER_2D_MULTISAMPLE_ARRAY:
			return "isampler2DMSArray";

		case GL_INT_SAMPLER_BUFFER:
			return "isamplerBuffer";

		case GL_INT_SAMPLER_2D_RECT:
			return "isampler2DRect";

		case GL_UNSIGNED_INT_SAMPLER_1D:
			return "usampler1D";

		case GL_UNSIGNED_INT_SAMPLER_2D:
			return "usampler2D";

		case GL_UNSIGNED_INT_SAMPLER_3D:
			return "usampler3D";

		case GL_UNSIGNED_INT_SAMPLER_CUBE:
			return "usamplerCube";

		case GL_UNSIGNED_INT_SAMPLER_1D_ARRAY:
			return "usampler2DArray";

		case GL_UNSIGNED_INT_SAMPLER_2D_ARRAY:
			return "usampler2DArray";

		case GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE:
			return "usampler2DMS";

		case GL_UNSIGNED_INT_SAMPLER_2D_MULTISAMPLE_ARRAY:
			return "usampler2DMSArray";

		case GL_UNSIGNED_INT_SAMPLER_BUFFER:
			return "usamplerBuffer";

		case GL_UNSIGNED_INT_SAMPLER_2D_RECT:
			return "usampler2DRect";

		case GL_IMAGE_1D:
			return "image1D";

		case GL_IMAGE_2D:
			return "image2D";

		case GL_IMAGE_3D:
			return "image3D";

		case GL_IMAGE_2D_RECT:
			return "image2DRect";

		case GL_IMAGE_CUBE:
			return "imageCube";

		case GL_IMAGE_BUFFER:
			return "imageBuffer";

		case GL_IMAGE_1D_ARRAY:
			return "image1DArray";

		case GL_IMAGE_2D_ARRAY:
			return "image2DArray";

		case GL_IMAGE_2D_MULTISAMPLE:
			return "image2DMS";

		case GL_IMAGE_2D_MULTISAMPLE_ARRAY:
			return "image2DMSArray";

		case GL_INT_IMAGE_1D:
			return "iimage1D";

		case GL_INT_IMAGE_2D:
			return "iimage2D";

		case GL_INT_IMAGE_3D:
			return "iimage3D";

		case GL_INT_IMAGE_2D_RECT:
			return "iimage2DRect";

		case GL_INT_IMAGE_CUBE:
			return "iimageCube";

		case GL_INT_IMAGE_BUFFER:
			return "iimageBuffer";

		case GL_INT_IMAGE_1D_ARRAY:
			return "iimage1DArray";

		case GL_INT_IMAGE_2D_ARRAY:
			return "iimage2DArray";

		case GL_INT_IMAGE_2D_MULTISAMPLE:
			return "iimage2DMS";

		case GL_INT_IMAGE_2D_MULTISAMPLE_ARRAY:
			return "iimage2DMSArray";

		case GL_UNSIGNED_INT_IMAGE_1D:
			return "uimage1D";

		case GL_UNSIGNED_INT_IMAGE_2D:
			return "uimage2D";

		case GL_UNSIGNED_INT_IMAGE_3D:
			return "uimage3D";

		case GL_UNSIGNED_INT_IMAGE_2D_RECT:
			return "uimage2DRect";

		case GL_UNSIGNED_INT_IMAGE_CUBE:
			return "uimageCube";

		case GL_UNSIGNED_INT_IMAGE_BUFFER:
			return "uimageBuffer";

		case GL_UNSIGNED_INT_IMAGE_1D_ARRAY:
			return "uimage1DArray";

		case GL_UNSIGNED_INT_IMAGE_2D_ARRAY:
			return "uimage2DArray";

		case GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE:
			return "uimage2DMS";

		case GL_UNSIGNED_INT_IMAGE_2D_MULTISAMPLE_ARRAY:
			return "uimage2DMSArray";

		case GL_UNSIGNED_INT_ATOMIC_COUNTER:
			return "atomic_uint";
		}
		throw new IllegalArgumentException("Unknown GL type: " + type);
	}

	/**
	 * Establish the mapping from attribute names to IDs
	 */

	private void recordAttributes() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		this.attributes = new HashMap<String, Integer>();
		this.attributeTypes = new HashMap<String, Integer>();

		int[] iBuff = new int[1];
		gl.glGetProgramiv(this.program, GL_ACTIVE_ATTRIBUTES, iBuff, 0);
		int activeAttributes = iBuff[0];

		gl.glGetProgramiv(this.program, GL_ACTIVE_ATTRIBUTE_MAX_LENGTH, iBuff, 0);
		int maxNameSize = iBuff[0];

		byte[] nameBuffer = new byte[maxNameSize];

		int[] sizeBuffer = new int[1];
		int[] typeBuffer = new int[1];
		int[] nameLenBuffer = new int[1];
		for (int i = 0; i < activeAttributes; ++i) {
			gl.glGetActiveAttrib(this.program, i, maxNameSize, nameLenBuffer, 0, sizeBuffer, 0, typeBuffer, 0,
					nameBuffer, 0);
			String name = new String(nameBuffer, 0, nameLenBuffer[0]);

			this.attributes.put(name, gl.glGetAttribLocation(this.program, name));
			this.attributeTypes.put(name, typeBuffer[0]);
		}
	}

	/**
	 * Establish the mapping from uniform names to IDs
	 */

	private void recordUniforms() {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		this.uniforms = new HashMap<String, Integer>();
		this.uniformTypes = new HashMap<String, Integer>();

		int[] iBuff = new int[1];
		gl.glGetProgramiv(this.program, GL_ACTIVE_UNIFORMS, iBuff, 0);
		int activeUniforms = iBuff[0];

		gl.glGetProgramiv(this.program, GL_ACTIVE_UNIFORM_MAX_LENGTH, iBuff, 0);
		int maxNameSize = iBuff[0];

		byte[] nameBuffer = new byte[maxNameSize];

		int[] sizeBuffer = new int[1];
		int[] typeBuffer = new int[1];
		int[] nameLenBuffer = new int[1];
		for (int i = 0; i < activeUniforms; ++i) {
			gl.glGetActiveUniform(this.program, i, maxNameSize, nameLenBuffer, 0, sizeBuffer, 0, typeBuffer, 0,
					nameBuffer, 0);
			String name = new String(nameBuffer, 0, nameLenBuffer[0]);

			this.uniforms.put(name, gl.glGetUniformLocation(this.program, name));
			this.uniformTypes.put(name, typeBuffer[0]);
		}
	}

	/**
	 * Read source code from a shader file.
	 * 
	 * @param shaderFile
	 * @return
	 * @throws IOException
	 */
	private static String[] readSource(File shaderFile) throws IOException {
		ArrayList<String> source = new ArrayList<String>();
		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(shaderFile));

			for (String line = in.readLine(); line != null; line = in.readLine()) {
				source.add(line + "\n");
			}

		} catch (IOException e) {
			throw e;
		} finally {
			if (in != null) {
				in.close();
			}
		}

		String[] lines = new String[source.size()];
		return source.toArray(lines);
	}

	/**
	 * Compile a shader
	 * 
	 * @param type
	 * @param sourceFile
	 * @return
	 * @throws GLException
	 * @throws IOException
	 */

	private static int compileShader(int type, File sourceFile) throws GLException, IOException {
		GL4 gl = (GL4) GLContext.getCurrentGL();

		String[] source = readSource(sourceFile);

		int shader = gl.glCreateShader(type);
		gl.glShaderSource(shader, source.length, source, null, 0);
		gl.glCompileShader(shader);
		GLException.checkGLErrors();

		// check compilation

		int[] compiled = new int[1];
		gl.glGetShaderiv(shader, GL_COMPILE_STATUS, compiled, 0);
		String logString = "";

		if (compiled[0] != 1) {

			int[] maxlen = new int[1];
			gl.glGetShaderiv(shader, GL_INFO_LOG_LENGTH, maxlen, 0);

			if (maxlen[0] > 0) {
				int[] len = new int[1];
				byte[] log = null;

				log = new byte[maxlen[0]];
				gl.glGetShaderInfoLog(shader, maxlen[0], len, 0, log, 0);
				logString = new String(log);
			}

			String message = String.format("%s: compilation error\n%s", sourceFile.getName(), logString);
			throw new GLException(message);
		}

		return shader;
	}

	/**
	 * Turn a shader type constant into a descriptive string.
	 * 
	 * @param type
	 * @return
	 */
	private static String shaderType(int type) {
		switch (type) {
		case GL_VERTEX_SHADER:
			return "Vertex shader";
		case GL_FRAGMENT_SHADER:
			return "Fragment shader";
		}
		return "Unknown shader";
	}
	
}
