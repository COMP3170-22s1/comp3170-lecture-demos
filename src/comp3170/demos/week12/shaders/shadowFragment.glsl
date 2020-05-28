#version 410

uniform vec4 u_colour;	// RGBA
uniform sampler2D u_shadowBuffer;	// WORLD DEPTH

uniform mat4 u_lightMatrix;	// WORLD -> SHADOWBUFFER UV

in vec4 v_position;		// WORLD

layout(location = 0) out vec4 colour;

void main() {
	// get the fragment position in NDC coordinates
	// with regard to the light source
	vec4 shadowPos = u_lightMatrix * v_position;
	shadowPos = shadowPos / shadowPos.w;
	
	// convert [-1..1] to [0..1]  
	shadowPos = (shadowPos + 1) / 2; 	
	
	// look up the point in the shadow buffer
	vec4 minDist = texture(u_shadowBuffer, shadowPos.xy);
	
	if (shadowPos.z > minDist.z) {
		// in shadow
		colour = vec4(0,0,0,1);
	}
	else {
		// lit
		colour = u_colour;
	}
	 		
}

