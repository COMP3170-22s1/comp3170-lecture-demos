#version 410

layout(location = 0) out vec4 colour;

void main() {
	float depth = gl_FragCoord.z;
	colour = vec4(depth, depth, depth, 1);
}

