/**
*Each instance of this Planet class is a Planet with a given x position,
*y position, x velocity, y velocity, mass and the image.
*/
public class Planet {
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	/**
	*This constructor creates an instance of a Planet with the given x position,
	*y position, x velocity, y velocity, mass and image.
	*@param xP inputted x position of the Planet
	*@param yP inputted y position of the Planet
	*@param xV inputted x velocity of the Planet
	*@param yV inputted y velocity of the Planet
	*@param m inputted mass of the Planet
	*@param img inputted file name of the image of the Planet
	*/
	public Planet(double xP, double yP, double xV, double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	/**This constructor creates an instance of a Planet.*/
	public Planet(Planet p){
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;
	}

	/**
	*This method calculates the distance between this Planet and an inputted Planet.
	*@param pl inputted Planet
	*/
	public double calcDistance(Planet pl){
		double changeX = this.xxPos - pl.xxPos;
		double changeY = this.yyPos - pl.yyPos;
		double distance = Math.sqrt((changeX * changeX) + (changeY * changeY));
		return distance;
	}

	/**
	*This method calculates the force exerted on this Planet by the inputted Planet.
	*@param pl inputted Planet
	*/
	public double calcForceExertedBy(Planet pl){
		double G = 6.67 * Math.pow(10, -11);
		double distance = calcDistance(pl);
		double force = ((G * this.mass * pl.mass)/(distance * distance));
		return force;
	}

	/**
	*This method calculates the force in the x direction exerted on this Planet by the inputted Planet.
	*@param pl inputted Planet
	*/
	public double calcForceExertedByX(Planet pl){
		double changeX = pl.xxPos - this.xxPos;
		double force = calcForceExertedBy(pl);
		double distance = calcDistance(pl);
		double xForce = (changeX * force) / distance;
		return xForce;
	}

	/**
	*This method calculates the force in the y direction exerted on this Planet by the inputted Planet.
	*@param pl inputted Planet
	*/
	public double calcForceExertedByY(Planet pl){
		double changeY = pl.yyPos - this.yyPos;
		double force = calcForceExertedBy(pl);
		double distance = calcDistance(pl);
		double yForce = (changeY * force) / distance;
		return yForce;
	}

	/**
	*This method calculates the net force in the x direction exerted on this Planet by the 
	*by other planets in the inputted array.
	*@param p inputted Planet array
	*/
	public double calcNetForceExertedByX(Planet[] p){
		double netForce = 0;
		double xForce;
		for(int i = 0; i < p.length; i++){
			if (!this.equals(p[i])){
				xForce = calcForceExertedByX(p[i]);
				netForce += xForce;
			}
		}

		return netForce;
	}

	/**
	*This method calculates the net force in the y direction exerted on this Planet by the 
	*by other planets in the inputted array.
	*@param p inputted Planet array
	*/
	public double calcNetForceExertedByY(Planet[] p){
		double netForce = 0;
		double yForce;
		for(int i = 0; i < p.length; i++){
			if (!this.equals(p[i])){
				yForce = calcForceExertedByY(p[i]);
				netForce += yForce;
			}
		}

		return netForce;
	}

	/**
	*This method finds this planet's cceleration in the x and y direction
	* to update the velocity and position in the x and y direction.
	*@param dt inputted change in time 
	*@param fX inputted x force
	*@param fY inputted y force 
	*/
	public void update(double dt, double fX, double fY){
		double xAcc = fX/this.mass;
		double yAcc = fY/this.mass;
		xxVel = this.xxVel + (dt * xAcc);
		yyVel = this.yyVel + (dt * yAcc);
		xxPos = this.xxPos + (dt * xxVel);
		yyPos = this.yyPos + (dt * yyVel);
	}

	/**
	*This method draws this planet at its x position and y position  .
	*/
	public void draw(){
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}

}
