#version 410

uniform vec4 u_colour;

layout(location = 0) out vec4 o_colour;

in vec4 v_viewPos; // VIEW

void main() {
	float dist = gl_FragCoord.z;
    o_colour = vec4(u_colour.rgb, dist);
}

