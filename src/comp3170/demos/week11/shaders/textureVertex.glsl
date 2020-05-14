#version 410

in vec3 a_position;		// MODEL
in vec2 a_texcoord;		// UV
out vec2 v_texcoord;	// UV

uniform mat4 u_mvpMatrix;	// MODEL -> NDC

void main() {
    gl_Position = u_mvpMatrix * vec4(a_position,1);
    v_texcoord = a_texcoord;
}

