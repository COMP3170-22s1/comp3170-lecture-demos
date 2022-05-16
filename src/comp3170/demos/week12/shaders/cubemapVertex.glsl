#version 410

in vec4 a_position;	// MODEL
uniform mat4 u_mvpMatrix;			// MODEL -> NDC

out vec4 v_position;	// WORDL

void main() {
	v_position = a_position;
    vec4 position = u_mvpMatrix * a_position;
    gl_Position = position.xyww;
}

