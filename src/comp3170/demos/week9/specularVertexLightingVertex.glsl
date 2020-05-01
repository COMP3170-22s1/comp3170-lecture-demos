#version 410

in vec3 a_position;	// model coordinates	
in vec3 a_normal;	// model coordinates

uniform mat4 u_mvpMatrix;		// model -> NDC
uniform mat3 u_normalMatrix;	// model normal -> world

uniform vec4 u_lightDir; 		// in world coordinates
uniform vec4 u_viewDir; 		// in world coordinates
uniform vec3 u_specularMaterial;	// RGB

uniform float u_specularity;	// Phong coefficient 

out vec3 v_colour;	// RGB

vec3 phongModel(vec3 normal, vec3 lightDir) {
    // make sure the viewDir has length 1
    vec3 viewDir = normalize(u_viewDir.xyz);    
	// calculate the reflection vector
	vec3 reflected = reflect(lightDir, normal);  
    
    // Phong model specular lighting equation (assuming the light is white)
    return u_specularMaterial * pow(max(0,dot(reflected, viewDir)), u_specularity);
}


void main() {
    gl_Position = u_mvpMatrix * vec4(a_position, 1);
    
    // convert the normal to world coordinates
    vec3 normal = u_normalMatrix * a_normal;

    // make sure the lightDir, viewDir and normal have length 1
    normal = normalize(normal);
    vec3 lightDir = normalize(u_lightDir.xyz);    
    
    vec3 specular = vec3(0);

    // only apply specular lighting if the light is in front of the surface 
    if (dot(lightDir, normal) > 0) {     	   
		specular = phongModel(normal, lightDir);
    }
	// interpolate to fragment colour
    v_colour = specular;
}

