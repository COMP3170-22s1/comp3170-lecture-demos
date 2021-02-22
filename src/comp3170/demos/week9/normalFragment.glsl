#version 410

in vec3 v_normal;	// MODEL

layout(location = 0) out vec4 colour;

void main() {
    colour = vec4(v_normal, 1);
}

