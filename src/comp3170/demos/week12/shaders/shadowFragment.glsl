#version 410

uniform vec4 u_colour;	// RGBA
uniform sampler2D u_shadowBuffer;	// WORLD DEPTH

uniform mat4 u_lightMatrix;	// WORLD -> SHADOWBUFFER UV

in vec4 v_position;		// WORLD

layout(location = 0) out vec4 colour;

const float BIAS = 0.0001;

void main() {
	// compare to the value from the shadow buffer
	vec4 shadowPos = u_lightMatrix * v_position;
	shadowPos = shadowPos / shadowPos.w; 
	
	// scale into [0,1]
	shadowPos = (shadowPos + 1) / 2; 
	vec4 minDist = texture(u_shadowBuffer, shadowPos.xy);
	
	if (shadowPos.z >= minDist.z + 0.00001) {
		// in shadow
		colour = vec4(0,0,0,1);
	}
	else {
		// lit
		colour = u_colour;
	}
	 		
}

