#version 410

uniform sampler2D u_texture;

in vec2 v_texcoord;		// UV

layout(location = 0) out vec4 colour;

const float offset = 1.0 / 300;

void main() {
	colour = vec4(0);

	colour = colour + texture(u_texture, v_texcoord + vec2(-offset, -offset));
	colour = colour + texture(u_texture, v_texcoord + vec2(      0, -offset));
	colour = colour + texture(u_texture, v_texcoord + vec2( offset, -offset));
	colour = colour + texture(u_texture, v_texcoord + vec2(-offset,       0));
	colour = colour + texture(u_texture, v_texcoord + vec2(      0,       0));
	colour = colour + texture(u_texture, v_texcoord + vec2( offset,       0));
	colour = colour + texture(u_texture, v_texcoord + vec2(-offset,  offset));
	colour = colour + texture(u_texture, v_texcoord + vec2(      0,  offset));
	colour = colour + texture(u_texture, v_texcoord + vec2( offset,  offset));	
	
	colour = colour / 9;
}

