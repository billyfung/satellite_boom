// Main Sources:
//   (i)   http://www.bruce-shapiro.net/pair/
//   (ii)  http://www.amsat.org/amsat/keps/kepmodel.html
//   (iii) http://en.wikipedia.org/wiki/Orbital_elements
//   (iv)  http://www.elsevierdirect.com/companions/9780126836301/appendices/Chapter_2_-_Kepler_Orbit_Elements_to_Eci_Cartesian_Coordinates_Conversion.pdf

static class OrbitalElements {

  // Semimajor axis
  static float a = 7078; //km?

  // Eccentricity
  static float e = 0.0; //range [0,1) (but breaks above ~0.97)
  // Inclination
  static float incl = 1.693; //radians, range [0,PI]
  // Argument of periapsis
  static float omega = 0; //radians, range [0,2PI]
  // Longitude of the ascending node
  static float OMEGA = 0; //radians, range [0,2PI]
  // Mean anomaly
  static float M = PI/2; //range [0,2PI]

  // Orbital period
  static float T = 6; //days/revolution
  
  static JavaSucksReturnTwo getParameters(Vector3D pos, Vector3D vel) {
  
    //Vector3D curPoint = new Vector3D();
    //Vector3D prevPoint = new Vector3D();
  
    //float t = i/(numPoints/10.0);
    //M = n*(t - T/(2*PI));
    // Calculate the eccentric anomaly, E
    float E = CalcEccentricAnomaly(M);
    //println("M = " + M + "; E = " + E + ";");
    
    // Calculate the true anomaly, range (0,2PI)
    float nu = 2*atan(sqrt((1+e)/(1-e))*tan(E/2));
    
    // Calculate radius, r
    float r = a*(1-pow(e,2))/(1+e*cos(nu));
    
    // Calculate mu:
    // Note: not sure if this is correct in this context
    float mu = Config.GRAVITY_CONSTANT*(Config.MASS_EARTH + Config.MASS_SATELLITE);
    
    // Compute specific angular momentum, h
    float h = sqrt(mu*a*(1-pow(e,2)));    
    
    // Calculate position and velocity in cartesian co-ords
    //Vector3D pos = new Vector3D();
    //Vector3D vel = new Vector3D();
  
    pos = new Vector3D(
      r*(cos(OMEGA)*cos(omega+nu) - sin(OMEGA)*sin(omega+nu)*cos(incl)),
      r*(sin(OMEGA)*cos(omega+nu) + cos(OMEGA)*sin(omega+nu)*cos(incl)),
      r*(sin(incl)*sin(omega+nu))
    );
    
    vel = new Vector3D(
      pos.getX()*h*e*sin(nu)/(r*a*(1-pow(e,2))) - h*(cos(OMEGA)*sin(omega+nu)+sin(OMEGA)*cos(omega+nu)*cos(incl))/r,
      pos.getY()*h*e*sin(nu)/(r*a*(1-pow(e,2))) - h*(sin(OMEGA)*sin(omega+nu)-cos(OMEGA)*cos(omega+nu)*cos(incl))/r,
      pos.getZ()*h*e*sin(nu)/(r*a*(1-pow(e,2))) + h*(sin(incl)*cos(omega+nu))/r
    );
    
    JavaSucksReturnTwo res = new JavaSucksReturnTwo();
    res.a = pos;
    res.b = vel;
    return res;
  }

  static private float CalcEccentricAnomaly(float M0){
    // Want to calculate the eccentric anomaly given a known mean anomaly 
    
    // Set how accurate the apprimation must be (lower maxDiff -> more accurate)
    float maxDiff = 1E-15;
    
    float[] E = new float[2];
    E[0] = M0;
    E[1] = E[0] - (E[0] - e*sin(E[0]) - M0)/(1-e*cos(E[0]));
    
    // Track number of iterations of the approximation
    // If it has tried over 100 times, stop trying and take the value found
    int numIter = 0;
    
    while(abs(E[0]-E[1]) > maxDiff && numIter < 100){
      numIter ++;
      E[0] = E[1];
      E[1] = E[0] - (E[0] - e*sin(E[0]) - M0)/(1-e*cos(E[0]));
    }
    //println("Iterations: " + numIter);
    return E[1];  
  }
}
