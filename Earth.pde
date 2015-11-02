class Earth extends Planet {
  
  Earth(Vector3D pos_new, Vector3D vel_new, Vector3D acc_new, float mass, float r, String tex_name) {
    
    super(pos_new, vel_new, acc_new, mass, r, tex_name);
    solver = new ODEEulerSolver();
    radius = r;
    bounding_sphere_radius = r;

  }
}
