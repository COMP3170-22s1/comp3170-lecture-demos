#version 410

in vec3 v_normal;	// world space - interpolated from vertex shader

uniform vec3 u_lightDir; 		// in world coordinates
uniform vec3 u_viewDir; 		// in world coordinates
uniform vec3 u_specularMaterial;	// RGB
uniform float u_specularity;	// Phong coefficient 

layout(location = 0) out vec4 colour;

void main() {
    // make sure the lightDir and normal have length 1
    vec3 normal = normalize(v_normal);
    vec3 lightDir = normalize(u_lightDir);        
    vec3 viewDir = normalize(u_viewDir);    
    
    // calculate the reflection vector
    vec3 reflected = reflect(lightDir, normal);  
    
    // Phong model specular lighting equation (assuming the light is white)
    vec3 specular = u_specularMaterial * pow(max(0,dot(reflected, viewDir)), u_specularity);
    
    // interpolate to fragment colour
    colour = vec4(specular, 1);    
}
