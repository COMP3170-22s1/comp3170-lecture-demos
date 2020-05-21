#version 410

uniform vec4 u_colour;	// RGBA
uniform sampler2D u_shadowBuffer;	// WORLD DEPTH

uniform vec4 u_lightPosition;	// WORLD
uniform mat4 u_lightMatrix;	// WORLD -> SHADOWBUFFER UV

in vec4 v_position;		// WORLD

layout(location = 0) out vec4 colour;

void main() {
	// calculate the distance from the light to the fragment 
	vec4 v = u_lightPosition - v_position;
	float dist = length(v);
	 		
	// compare to the value from the shadow buffer
	vec2 uv = (u_lightMatrix * v_position).xy; 
	float minDist = texture(u_shadowBuffer, uv);
	
	if (dist > minDist) {
		// in shadow
		colour = vec4(0,0,0,1);
	}
	else {
		// lit
		colour = u_colour;
	}
	 		
}

