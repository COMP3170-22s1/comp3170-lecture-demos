#version 410

uniform vec3 u_intensity;        // source intensity (RGB)
uniform vec3 u_ambientIntensity; // ambient intensity (RGB)

uniform vec4 u_lightDirection;   // source vector (WORLD)
uniform vec4 u_viewDirection;  // view vector (WORLD)

uniform vec3 u_diffuseMaterial;  // diffuse material cofficients (RGB)
uniform vec3 u_specularMaterial;  // specular material cofficients (RGB)

in vec4 v_normal;                // interpolated surface normal (WORLD)
in vec4 v_position;              // interpolated fragment position (WORLD)

const float SPECULARITY = 100.;
const float GAMMA = 2.2;

layout(location = 0) out vec4 o_colour;

void main() {
    // normalise the vectors
    vec4 n = normalize(v_normal);
    vec4 s = normalize(u_lightDirection);
    vec4 r = vec4(0);
    vec4 v = normalize(u_viewDirection);

	// gamma correction
	vec3 colour = pow(u_diffuseMaterial, vec3(GAMMA));
	
    vec3 ambient = u_ambientIntensity * colour;
    vec3 diffuse = u_intensity * colour * max(0, dot(s,n));
	vec3 specular = vec3(0);
	
	if (dot(s,n) > 0) {
	    r = -reflect(s, n);
		// gamma correction
		colour = pow(u_specularMaterial, vec3(GAMMA));
		specular = u_intensity * colour * pow(max(0,dot(r,v)), SPECULARITY); 
	}

	vec3 intensity = ambient + diffuse + specular;
	vec3 brightness = pow(intensity, vec3(1./GAMMA));
	
    o_colour = vec4(brightness, 1);
}