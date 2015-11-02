// Class which contains trig approximations. 

class TrigApprox {
  float sinLUT[];
  float cosLUT[];
  float SINCOS_PRECISION = 0.1;
  int SINCOS_LENGTH = int(360.0 / SINCOS_PRECISION);
  
  TrigApprox() {
      sinLUT = new float[SINCOS_LENGTH];
      cosLUT = new float[SINCOS_LENGTH];
    
      for (int i = 0; i < SINCOS_LENGTH; i++) {
        sinLUT[i] = (float) Math.sin(i * DEG_TO_RAD * SINCOS_PRECISION);
        cosLUT[i] = (float) Math.cos(i * DEG_TO_RAD * SINCOS_PRECISION);
      }
  }
  
  // Return approximate cosine. Angle is in degrees
  float ncos(float deg) {
    return cosLUT[(int)(deg / SINCOS_PRECISION)];
  }
  
  // Returns approximate sin. Angle is in degrees
  float nsin(float deg) {
    return sinLUT[(int)(deg / SINCOS_PRECISION)];
  }
}
