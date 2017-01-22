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
		
	}

	public double calcNetForceExertedByX(Planet p[]){
		double netForce = 0;
		double xForce;
		for(int i = 0; i < p.length; i++){
			if (!this.equals(p[i])){
				double changeX = this.xxPos - p[i].xxPos;
				xForce = (calcForceExertedBy(p[i]) * changeX) / calcDistance(p[i]);
				netForce += xForce;
			}
		}

		if (netForce < 0){
			return -netForce;
		}

		return netForce;
	}

	public double calcNetForceExertedByY(Planet p[]){
		double netForce = 0;
		double yForce;
		for(int i = 0; i < p.length; i++){
			if (!this.equals(p[i])){
				double changeY = this.yyPos - p[i].yyPos;
				yForce = (this.calcForceExertedBy(p[i]) * changeY) / calcDistance(p[i]);
				netForce += yForce;
			}
		}

		if (netForce < 0){
			return -netForce;
		}

		return netForce;
	}

}
