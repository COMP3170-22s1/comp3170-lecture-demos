package comp3170.demos.week11.shaders;

import java.io.File;
import java.io.IOException;

import comp3170.GLException;
import comp3170.Shader;

public class ShaderLibrary {

	final private static File DIRECTORY = new File("src/comp3170/demos/week11/shaders"); 
	
	public static Shader compileShader(String vertex, String fragment) {
		Shader shader = null;
		try {
			File vertexShader = new File(DIRECTORY, vertex);
			File fragmentShader = new File(DIRECTORY, fragment);
			shader = new Shader(vertexShader, fragmentShader);		

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (GLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return shader;

	}

}
