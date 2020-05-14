#version 410

uniform vec4 u_colour;
uniform sampler2D u_texture;

in vec2 v_texcoord;

layout(location = 0) out vec4 colour;

void main() {
    colour = texture(u_texture, v_texcoord);
}

