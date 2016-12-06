package cs371m.tardibear.suito.boids;

import android.support.v4.graphics.ColorUtils;

import java.util.List;

import cs371m.tardibear.suito.gfx.Model;

/**
 * Created by kaleb on 11/29/2016.
 */

public class Boid
{
    public static int instances = 0;

    public static final float MAX_SPEED = 0.01f;

    public static final float MAX_ACCER = 0.05f;

    public static Vec3 separate(Boid boid, List<Boid> flock, float desiredSep) {
        int count = 0;
        Vec3 steer = new Vec3();
        Vec3 temp;
        for(Boid o : flock) {
            if( o != boid) {
                temp = boid.getLocation().sub(o.getLocation());
                float d = temp.len();
                if((d>0) && (d < desiredSep)) {
                    float dd = (desiredSep - d);
                    dd *= dd;
                    temp.normalize();
                    temp.scale(dd);
                    steer = steer.add(temp);
                    count++;
                }
            }
        }
        return steer;
    }

    public static Vec3 separate(Boid boid, List<Boid> flock) {
       return separate(boid, flock, 1.0f);
    }

    public static Vec3 cohesion(Boid boid, List<Boid> flock, float neighborhoodDist) {
        Vec3 sum = new Vec3();
        int count = 0;
        for (Boid o :
                flock) {
            float d = (boid.getLocation().sub(o.getLocation())).len();
            if (boid != o && d < neighborhoodDist) {
                sum = sum.add(o.getLocation());
                count++;
            }
        }
        if(count  > 0) {
            sum.scale(1.0f/count);
            return boid.seek(sum);
        }
        sum.scale(0);
        return sum;
    }

    public static Vec3 cohesion(Boid boid, List<Boid> flock) {
        return cohesion(boid, flock, 1.5f);
    }

    public static Vec3 align(Boid boid, List<Boid> flock, float neighborhoodDist) {
        Vec3 sum = new Vec3();
        Vec3 zero = new Vec3();
        int count = 0;
        for (Boid o :
                flock) {
            float d = (boid.getLocation().sub(o.getLocation())).len();
            if (boid != o && d < neighborhoodDist) {
                Vec3 temp = zero.add(o.getVelocity());
                temp.normalize();
                sum = sum.add(temp);
                count++;
            }
        }
        if(count  > 0) {
            sum.scale(1.0f/count);
            return sum.sub(boid.getVelocity());
        }
        return zero;
    }

    public static Vec3 align(Boid boid, List<Boid> flock) {
        return align(boid, flock, .5f);
    }

    private float[] color;
    private Vec3 location;
    private Vec3 velocity;
    private Vec3 acceleration;
    private Vec3 separateIncentive;
    private Vec3 alignIncentive;
    private Vec3 cohereIncentive;
    private float separateCoeff;
    private float alignCoeff;
    private float cohereCoeff;

    private float size;

    private float[] weights;
    private Model model;
    private String name;


    public Boid(String name, float size, Vec4 color, Vec3 incentives, Vec3 location, Vec3 steer) {
        this.name = name;
        this.size = size;
        this.location = location;
        this.velocity = steer;
        velocity.setZ(0);
        separateCoeff = incentives.asArray()[0];
        alignCoeff = incentives.asArray()[1];
        cohereCoeff = incentives.asArray()[2];
        this.color = color.asArray();
        acceleration = new Vec3();
    }


    public Boid(String name, float size, Vec4 color, Vec3 incentives, Vec3 location, Vec3 steer, Flock flock) {
        this(name, size, color, incentives, location, steer);
        if(name == null){
            setName(flock);
        }

    }

    public Boid(float[] mColor, Vec3 mLocation, Vec3 steer) {
        this.color = mColor;
        this.location = mLocation;
        velocity = steer;
        acceleration = new Vec3();

        separateCoeff = 0.34f;
        alignCoeff = 0.31f;
        cohereCoeff = 0.33f;
        size = 0;
        setName(Integer.toString(instances++));

    }

    public Boid(float[] mColor, Vec3 mLocation, Vec3 steer, Flock flock) {
        this(mColor, mLocation, steer);
        setName(flock);

    }



    public Vec3 seek(Vec3 target) {
        Vec3 des = target.sub(location);
        des.normalize();
        Vec3 steer = des.sub(velocity);
        return steer;
    }



    // GETTERS AND SETTERS

    public float[] getColor() {
        return color;
    }

    public void setColor(float[] color) {
        this.color = color;
    }

    public Vec3 getLocation() {
        return location;
    }

    public void setLocation(Vec3 location) {
        this.location = location;
    }

    public Vec3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec3 velocity) {
        this.velocity = velocity;
    }

    public Vec3 getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vec3 acceleration) {
        this.acceleration = acceleration;
    }

    public float[] getWeights() {
        return weights;
    }

    public void setWeights(float[] weights) {
        this.weights = weights;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }


    public void update() {
        updateAcceleration();
        updateVelocity();
        updateLocation();
    }

    public void updateLocation() {

        location = location.add(velocity);

//        acceleration.scale(0);
    }

    public void updateAcceleration() {
        if(acceleration.len() > MAX_ACCER) {
            acceleration.normalize();
            acceleration.scale(MAX_ACCER);
        }

        if(location.len() > 5) {
            acceleration = seek(Vec3.zero());
            acceleration.normalize();
            acceleration.scale(MAX_ACCER);
        }
    }

    public void updateVelocity() {
        velocity = velocity.add(acceleration);

        if(velocity.len() > MAX_SPEED) {
            velocity.normalize();
            velocity.scale(MAX_SPEED);
        }
    }

    public void applyIncentives(Vec3 a, Vec3 s, Vec3 c) {
        alignIncentive = a;
        separateIncentive = s;
        cohereIncentive = c;

        alignIncentive.scale(alignCoeff);
        separateIncentive.scale(separateCoeff);
        cohereIncentive.scale(cohereCoeff);

        applyForce(alignIncentive);
        applyForce(cohereIncentive);
        applyForce(separateIncentive);
    }

    public void applyForce(Vec3 a) {
        acceleration = acceleration.add(a);
    }

    public void setForce(Vec3 a) {
        acceleration = a;
    }

    public void setName(Flock flock){
        this.name = "Boid #" + (flock.size() + 1);
    }

    private void setName(String name) {
        this.name = "Boid " + name;
    }

    public String getName(){
        return this.name;
    }

    @Override
    public String toString() {
        return "Boid{" +
                "name=" + name +
                ", location=" + location +
                ", velocity=" + velocity +
                ", acceleration=" + acceleration +
                ", separateIncentive=" + separateIncentive +
                ", alignIncentive=" + alignIncentive +
                ", cohereIncentive=" + cohereIncentive +
                ", color=(" + color[0] + "," + color[1] + "," + color[2] + ")" +
                '}';
    }

    public float getSize() {
        return size;
    }
}
