package game.engine.pos_functions;

import java.lang.Math.*;
import static java.lang.Math.*;

import game.engine.utils.primitives.Point;

public class pos_functions {

	static public float lendirx(float len, float dir) {
		return (float)(Math.sin(Math.PI*(dir+90)/180)*len);
	}

	static public float lendiry(float len, float dir) {
		return 	(float)(Math.cos(Math.PI*(dir+90)/180)*len);
	}

	static public Point lendir(float len, float dir) {
		Point point = new Point(lendirx(len,dir),lendiry(len,dir));
		return point;
	}

	static public float poidir(float x1, float y1, float x2, float y2) {
		if (x1==x2) {
			if (y1>y2) {
				return 90;
			} else {
				return 270;
			}
		} else {
			if ((x1<x2)&&(y1>y2)) {
				return (float) -(Math.atan((y2-y1)/(x2-x1))*180/Math.PI);
			} else if (x1>x2) {
				return (float) (180-atan((y2-y1)/(x2-x1))*180/Math.PI);
			} else {
				return (float) (360-atan((y2-y1)/(x2-x1))*180/Math.PI);
			}
		}
	}

	static public float poidir(Point inPoint1, Point inPoint2) {
		float x1 = inPoint1.getX();
		float y1 = inPoint1.getY();
		float x2 = inPoint2.getX();
		float y2 = inPoint2.getY();
		if (x1==x2) {
			if (y1>y2) {
				return 90;
			} else {
				return 270;
			}
		} else {
			if ((x1<x2)&&(y1>y2)) {
				return (float) -(Math.atan((y2-y1)/(x2-x1))*180/Math.PI);
			} else if (x1>x2) {
				return (float) (180-atan((y2-y1)/(x2-x1))*180/Math.PI);
			} else {
				return (float) (360-atan((y2-y1)/(x2-x1))*180/Math.PI);
			}
		}
	}


	static public float  corner(int c1,int c2) {
		int a; int b;
		a=360-c1+c2;
		if (a>360) {
			a=a-360;
		}
		b=360-a;
		if (a<b) {
			return a;
		} else if (b<a) {
			return -b;
		} else if (c1!=c2) {
			return 180;
		}
		return 0;
	}


	static public float poidis(float x1,float y1,float z1,float x2,float y2,float z2) {
		float length1= (float) sqrt(pow(abs(x1-x2),2)+pow(abs(y1-y2),2));
		return (float) sqrt(pow(length1,2)+pow(abs(z1-z2),2));
	}


	static public float poidis(float x1,float y1,float x2,float y2) {
		return (float) sqrt(pow(abs(x1-x2),2)+pow(abs(y1-y2),2));
	}

	static public float poidis(Point inPoint1, Point inPoint2) {
		return (float) sqrt(pow(abs(inPoint1.getX()-inPoint2.getX()),2)+pow(abs(inPoint1.getY()-inPoint2.getY()),2));
	}


	static public float normalizedDirection(float direction) {
		if (direction>359) {
			return direction-360;
		} else if (direction<0) {
			return 360+direction;
		}
		return direction;
	}



	static public boolean colPointSquare(float x1,float y1,float z1,float x2,float y2,float z2,float radius) {
		if ((abs(x1-x2)<radius)&&(abs(y1-y2)<radius)&&(abs(z1-z2)<radius)) {
			return true;
		}
		return false;
	}

	static public boolean colPointSquare(float x1,float y1, float x2,float y2,float radius) {
		if ((abs(x1-x2)<radius)&&(abs(y1-y2)<radius)) {
			return true;
		}

		return false;
	}

	static public boolean colPointSquare(Point inPoint1, Point inPoint2, float radius) {
		if ((abs(inPoint1.getX()-inPoint2.getX())<radius)&&(abs(inPoint1.getY()-inPoint2.getY())<radius)) {
			return true;
		}

		return false;
	}

	static public boolean colPointCircle(Point inPoint1, Point inPoint2, float radius) {
		if (poidis(inPoint1,inPoint2) <= radius) {
			return true;
		}
		return false;
	}


	/*static public float rotate(float *inDir, float inNeedDir, float inChDir) {
		float dirCorner = corner(*inDir, inNeedDir);
		int vector = 0;
		if (abs(dirCorner) < inChDir) {
			*inDir = inNeedDir;
		} else if (dirCorner > 0) {
			*inDir += inChDir;
			vector = 1;
		} else {
			*inDir -= inChDir;
			vector = -1;
		}

		normalizeDirection(inDir);

		return vector;
	}*/


	static public int sign(float inNumber) {
		if (inNumber>0) {
			return 1;
		} else if (inNumber<0) {
			return -1;
		}
		return 0;

	}

	static public Point normalizePoint(Point inPoint) {
		float lenght = (float) Math.sqrt(inPoint.getX()*inPoint.getX() + inPoint.getY()*inPoint.getY());
		inPoint.setX(inPoint.getX()/lenght);
		inPoint.setY(inPoint.getY()/lenght);
		return inPoint;
	}

	static public float vectorLenght(Point inPoint) {
		return (float) Math.abs(sqrt(inPoint.getX()*inPoint.getX() + inPoint.getY()*inPoint.getY()));
	}


	static public Point createVector(float inLength, float inDirection) {
		Point point = new Point(lendirx(inLength, inDirection),lendiry(inLength, inDirection));
		return point;
	}

	static public float abs(float inNumber) {
		if (inNumber > 0) {
			return inNumber;
		} else {
			return -inNumber;
		}
	}

}
