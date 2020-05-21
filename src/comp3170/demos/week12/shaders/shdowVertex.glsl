#version 410

in vec3 a_position;		// MODEL

uniform mat4 u_mvpMatrix;	// MODEL -> NDC
uniform mat4 u_worldMatrix;	// MODEL -> WORLD

out vec4 v_position;		// WORLD

void main() {
	vec4 position = vec4(a_position, 1);
    gl_Position = u_mvpMatrix * position;    
    v_position = u_worldMatrix * position;
}

