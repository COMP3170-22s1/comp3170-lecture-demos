#version 410

in vec3 v_colour;	// RGB - interpolated from vertex shader

layout(location = 0) out vec4 colour;

void main() {
    colour = vec4(1,1,1, 1);
}

