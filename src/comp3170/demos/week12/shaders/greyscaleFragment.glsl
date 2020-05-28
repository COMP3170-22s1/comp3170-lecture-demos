#version 410

uniform sampler2D u_texture;

in vec2 v_texcoord;		// UV

layout(location = 0) out vec4 colour;

void main() {	
	colour = texture(u_texture, v_texcoord);
	
	// luma calculation based of apparent brightness of each colour
	float grey = 0.2989 * colour.r + 0.5870 * colour.g + 0.1140 * colour.b;	
	colour = vec4(grey, grey, grey, 1);
}

