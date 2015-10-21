// based off of http://introcs.cs.princeton.edu/32class/Quaternion.java.html

public class Quaternion {
    public final double x0, x1, x2, x3; 

    // create a new object with the given components
    public Quaternion(double x0, double x1, double x2, double x3) {
        this.x0 = x0;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
    }
    
    // create a new quaternion with the given components with the option to normalize
    public Quaternion(double x0, double x1, double x2, double x3, boolean needsNormalization) {
        if (needsNormalization) {
            double invnorm = 1. / Math.sqrt(x0*x0 + x1*x1 + x2*x2 + x3*x3);
            x0 *= invnorm;
            x1 *= invnorm;
            x2 *= invnorm;
            x3 *= invnorm;
        }
        this.x0 = x0;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
    }
    
    // quaternion from a rotation
    public Quaternion(Vector3D axis, double angle) {
        this.x0 = Math.cos(angle/2);
        axis = axis.normalize();
        double sinhalf = Math.sin(angle/2);
        this.x1 = axis.getX() * sinhalf;
        this.x2 = axis.getY() * sinhalf;
        this.x3 = axis.getZ() * sinhalf;
    }

    // default quaternion (no rotation)
    public Quaternion() {
        this.x0 = 1;
        this.x1 = 0;
        this.x2 = 0;
        this.x3 = 0;
    }
    
    // quaternion from one vector to another (note: this does not uniquely define a quaternion!)
    public Quaternion(Vector3D v1, Vector3D v2) {
        double angle;
        try {
          angle = Vector3D.angle(v1, v2);
        } catch (ArithmeticException e) {
          print("broken @ Quaternion(Vector3D v1, Vector3D v2)\n");
          x0 = 1;
          x1 = 0;
          x2 = 0;
          x3 = 0;
          return;
        }
        Vector3D axis = Vector3D.crossProduct(v1, v2);
       
        //print(makeString(v1) + " : " + makeString(v2) + "\n");
        //print(angle);
       
        if (angle > PI-0.000005) {
          // rotation by 180 degrees
          this.x0 = -1;
          this.x1 = 0;
          this.x2 = 0;
          this.x3 = 0;
        } else if (angle > 0.000005) {
          // copied from axis/angle code. Java sucks...
          this.x0 = Math.cos(angle/2);
          axis = axis.normalize();
          double sinhalf = Math.sin(angle/2);
          this.x1 = axis.getX() * sinhalf;
          this.x2 = axis.getY() * sinhalf;
          this.x3 = axis.getZ() * sinhalf;
        } else {
          // no rotation actually occurred
          this.x0 = 1;
          this.x1 = 0;
          this.x2 = 0;
          this.x3 = 0;
        }
    }

    // return a string representation of the invoking object
    public String toString() {
        return x0 + " + " + x1 + "i + " + x2 + "j + " + x3 + "k";
    }

    // return the quaternion norm
    public double norm() {
        return Math.sqrt(x0*x0 + x1*x1 +x2*x2 + x3*x3);
    }
    
    // return a normalized quaternion
    public Quaternion normalize() {
        double d = this.norm();
        return new Quaternion(x0/d, x1/d, x2/d, x3/d);
    }

    // return the quaternion conjugate
    public Quaternion conjugate() {
        return new Quaternion(x0, -x1, -x2, -x3);
    }

    // return a new Quateyrnion whose value is (this + b)
    public Quaternion plus(Quaternion b) {
        Quaternion a = this;
        return new Quaternion(a.x0+b.x0, a.x1+b.x1, a.x2+b.x2, a.x3+b.x3);
    }


    // return a new Quaternion whose value is (this * b)
    public Quaternion times(Quaternion b) {
        Quaternion a = this;
        double y0 = a.x0*b.x0 - a.x1*b.x1 - a.x2*b.x2 - a.x3*b.x3;
        double y1 = a.x0*b.x1 + a.x1*b.x0 + a.x2*b.x3 - a.x3*b.x2;
        double y2 = a.x0*b.x2 - a.x1*b.x3 + a.x2*b.x0 + a.x3*b.x1;
        double y3 = a.x0*b.x3 + a.x1*b.x2 - a.x2*b.x1 + a.x3*b.x0;
        return new Quaternion(y0, y1, y2, y3);
    }
    
    // scalar multiplication
    public Quaternion times(double b) {
        Quaternion a = this;
        double y0 = a.x0*b;
        double y1 = a.x1*b;
        double y2 = a.x2*b;
        double y3 = a.x3*b;
        return new Quaternion(y0, y1, y2, y3);
    }

    // return a new Quaternion whose value is the inverse of this
    public Quaternion inverse() {
        double d = x0*x0 + x1*x1 + x2*x2 + x3*x3;
        return new Quaternion(x0/d, -x1/d, -x2/d, -x3/d);
    }

    // return a / b
    public Quaternion divides(Quaternion b) {
        Quaternion a = this;
        return a.inverse().times(b);
    }


    // return the corresponding left handed rotation matrix
    public double[][] getLeftMatrix() {
        // adapted from http://en.wikipedia.org/wiki/Rotation_matrix
        // verified at http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/transforms/index.htm
        // this corresponds to a left rotation
      
        // pointless, but more readable.
        double w = this.x0;
        double x = this.x1;
        double y = this.x2;
        double z = this.x3;
      
        double[][] res = new double[3][3];
        res[0][0] = 2*x*x + 2*w*w - 1;
        res[0][1] = 2*x*y - 2*z*w;
        res[0][2] = 2*x*z + 2*y*w;
        res[1][0] = 2*x*y + 2*z*w;
        res[1][1] = 2*y*y + 2*w*w - 1;
        res[1][2] = 2*y*z - 2*x*w;
        res[2][0] = 2*x*z - 2*y*w;
        res[2][1] = 2*y*z + 2*x*w;
        res[2][2] = 2*z*z + 2*w*w - 1;
        
        return res;
    }

    // return the corresponding right handed rotation matrix
    public double[][] getRightMatrix() {
        // from http://osdir.com/ml/games.devel.algorithms/2002-11/msg00318.html
        // results verified with http://www.cprogramming.com/tutorial/3d/rotationMatrices.html
      
        // pointless, but more readable.
        double w = this.x0;
        double x = this.x1;
        double y = this.x2;
        double z = this.x3;
      
        double[][] res = new double[3][3];
        res[0][0] = 2*x*x + 2*w*w - 1;
        res[0][1] = 2*x*y + 2*z*w;
        res[0][2] = 2*x*z - 2*y*w;
        res[1][0] = 2*x*y - 2*z*w;
        res[1][1] = 2*y*y + 2*w*w - 1;
        res[1][2] = 2*y*z + 2*x*w;
        res[2][0] = 2*x*z + 2*y*w;
        res[2][1] = 2*y*z - 2*x*w;
        res[2][2] = 2*z*z + 2*w*w - 1;
        
        return res;
    }
    
    // return the corresponding axis of rotation
    public Vector3D getAxis() {
        double s = Math.sqrt(1 - x0*x0);
        if (s < 0.00001) {
            // axis of rotation is arbitrary if no rotation occurs
            return new Vector3D(1,0,0);
        }
        return new Vector3D(x1 / s, x2 / s, x3 / s);
    }
    
    // return the corresponding angle for axis/angle notation
    public double getAngle() {
        return 2*Math.acos(x0);
    }

    // return a vector with the transformation applied to it
    public Vector3D applyTo(Vector3D v) {
        // LEFT quaternion, so q*v*conj(q)
        // this is equivalent to multiplying by the corresponding rotation matrix
        //print("right mat: " + makeString(multMatrix(getRightMatrix(), v)) + "\n");
     /*   print("left mat:  " + makeString(multMatrix(getLeftMatrix(), v)) + "\n");
        Quaternion qvec = new Quaternion(0, v.getX(), v.getY(), v.getZ());
        Quaternion qres = this.times(qvec.times(this.inverse()));
        Vector3D res = new Vector3D(qres.x1, qres.x2, qres.x3);
        print("quat mult: " + makeString(res) + "\n");
        return res;*/
        return multMatrix(getRightMatrix(), v);
    }
}

