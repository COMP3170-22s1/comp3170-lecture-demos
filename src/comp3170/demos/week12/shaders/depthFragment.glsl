#version 410

uniform vec4 u_colour;
uniform float u_near;
uniform float u_far;

layout(location = 0) out vec4 o_colour;

in vec4 v_viewPos; // VIEW

void main() {
	float dist = (-v_viewPos.z - u_near) / (u_far - u_near);
    o_colour = vec4(u_colour.rgb, dist);	
}

