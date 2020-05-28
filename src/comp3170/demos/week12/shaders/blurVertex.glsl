#version 410

uniform mat4 u_mvpMatrix;	// MODEL -> NDC
	
in vec3 a_position;		// MODEL
out vec2 v_texcoord;	// UV

void main() {
    gl_Position = u_mvpMatrix * vec4(a_position, 1);
    v_texcoord = (a_position.xy + 1) / 2;
}

