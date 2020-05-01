#version 410

in vec3 v_normal;	// world space - interpolated from vertex shader

uniform vec4 u_lightDir; 		// in world coordinates
uniform vec4 u_viewDir; 		// in world coordinates
uniform vec3 u_specularMaterial;	// RGB
uniform float u_specularity;	// Phong coefficient 

layout(location = 0) out vec4 colour;

vec3 phongModel(vec3 normal, vec3 lightDir) {
    // make sure the viewDir has length 1
    vec3 viewDir = normalize(u_viewDir.xyz);    
	// calculate the reflection vector
	vec3 reflected = reflect(lightDir, normal);  
    
    // Phong model specular lighting equation (assuming the light is white)
    return u_specularMaterial * pow(max(0,dot(reflected, viewDir)), u_specularity);
}

void main() {
    // make sure the lightDir and normal have length 1
    vec3 normal = normalize(v_normal);
    vec3 lightDir = normalize(u_lightDir.xyz);        
    
    vec3 specular = vec3(0);
    // only apply specular lighting if the light is in front of the surface 
    if (dot(lightDir, normal) > 0) {
    	specular = phongModel(normal, lightDir);     
	}    
   	// interpolate to fragment colour
   	colour = vec4(specular, 1);    
}
