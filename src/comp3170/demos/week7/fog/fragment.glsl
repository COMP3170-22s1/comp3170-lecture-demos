#version 410

uniform vec3 u_colour;			// RGB
uniform vec3 u_fogColour;		// RGB
uniform vec4 u_cameraPosition;	// WORLD

in vec4 v_position; 	// WORLD 


layout(location = 0) out vec4 o_colour;	// output to colour buffer (r,g,b,a)

void main() {
    float distance = length(u_cameraPosition - v_position);

    // simple fog: calculate colour based on the distance to the fragment
    vec3 colour = mix(u_colour, u_fogColour, 1.0 - pow(0.5, distance));
	
    o_colour = vec4(colour, 1);
}
