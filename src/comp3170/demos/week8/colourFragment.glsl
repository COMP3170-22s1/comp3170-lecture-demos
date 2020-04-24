#version 410

in vec4 v_colour;

layout(location = 0) out vec4 colour;

void main() {
    colour = v_colour;
}

