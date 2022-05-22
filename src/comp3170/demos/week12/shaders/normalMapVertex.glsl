#version 410

in vec4 a_position;	// MODEL
in vec2 a_texcoord;	// UV 

in vec3 a_normal;	// MODEL
in vec3 a_tangent;	// MODEL
in vec3 a_bitangent;	// MODEL

uniform mat4 u_mvpMatrix;			// MODEL -> NDC
uniform mat4 u_modelMatrix;			// MODEL -> WORLD

out vec4 v_position;	// WORLD
out vec2 v_texcoord;	// UV 
out vec3 v_normal;		// MODEL
out vec3 v_tangent;		// MODEL
out vec3 v_bitangent;	// MODEL

void main() {
	v_texcoord = a_texcoord;
	v_normal = a_normal;
	v_tangent = a_tangent;
	v_bitangent = a_bitangent;
	v_position = u_modelMatrix * a_position;
	
    gl_Position = u_mvpMatrix * a_position;
}

