public class Planet {
	public double xxPos;
	public double yyPos;
	public double xxVel;
	public double yyVel;
	public double mass;
	public String imgFileName;

	public Planet(double xP, double yP, double xV, double yV, double m, String img){
		xxPos = xP;
		yyPos = yP;
		xxVel = xV;
		yyVel = yV;
		mass = m;
		imgFileName = img;
	}

	public Planet(Planet p){
		xxPos = p.xxPos;
		yyPos = p.yyPos;
		xxVel = p.xxVel;
		yyVel = p.yyVel;
		mass = p.mass;
		imgFileName = p.imgFileName;

	}

	public double calcDistance(Planet p){
		double changeX = this.xxPos - p.xxPos;
		double changeY = this.yyPos - p.yyPos;
		double distance = Math.sqrt((changeX * changeX) + (changeY * changeY));
		return distance;
	}

	public double calcForceExertedBy(Planet p){
		double G = 6.67 * Math.pow(10, -11);
		double distance = calcDistance(p);
		double force = ((G * this.mass * p.mass)/(distance * distance));
		return force;
	}

	public double calcForceExertedByX(Planet p){
		double changeX = p.xxPos - this.xxPos;
		double force = calcForceExertedBy(p);
		double distance = calcDistance(p);
		double xForce = (changeX * force) / distance;
		return xForce;
	}

	public double calcForceExertedByY(Planet p){
		double changeY = p.yyPos - this.yyPos;
		double force = calcForceExertedBy(p);
		double distance = calcDistance(p);
		double yForce = (changeY * force) / distance;
		return yForce;
	}

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

	public void update(double dt, double fX, double fY){
		double xAcc = fX/this.mass;
		double yAcc = fY/this.mass;
		xxVel = this.xxVel + (dt * xAcc);
		yyVel = this.yyVel + (dt * yAcc);
		xxPos = this.xxPos + (dt * xxVel);
		yyPos = this.yyPos + (dt * yyVel);
	}

	public void draw(){
		StdDraw.picture(xxPos, yyPos, "images/" + imgFileName);
	}
}
