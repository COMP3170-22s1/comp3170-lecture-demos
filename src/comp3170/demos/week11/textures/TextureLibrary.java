package comp3170.demos.week11.textures;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class TextureLibrary {
	
	final private static File DIRECTORY = new File("src/comp3170/demos/week11/textures"); 

	public final static Map<String, Integer> loadedTextures = new HashMap<String, Integer>();
	
	
	public static int loadTexture(String filename) throws IOException {
		GL4 gl = (GL4) GLContext.getCurrentGL();
		
		if (loadedTextures.containsKey(filename)) {
			return loadedTextures.get(filename);
		}
		
		File textureFile = new File(DIRECTORY, filename);
		
		Texture tex = TextureIO.newTexture(textureFile, true);
		int textureID = tex.getTextureObject();
		
		gl.glBindTexture(GL.GL_TEXTURE_2D, textureID);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);
//		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR_MIPMAP_LINEAR);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
		gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL.GL_REPEAT);
		gl.glGenerateMipmap(GL.GL_TEXTURE_2D);

		loadedTextures.put(filename, textureID);
		return textureID;
	}

}
