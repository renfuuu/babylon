package cs371m.tardibear.suito.boids;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import cs371m.tardibear.suito.gfx.Graphics;

/**
 * Created by kaleb on 11/29/2016.
 */

public class Flock {

    List<Boid> flock;



    public Flock(List<Boid> flock) {
        this.flock = flock;
    }

    public int size() {
        return flock.size();
    }

    public List<Boid> getList() {
        return flock;
    }

    public Flock() {
        flock = new ArrayList<>();
        flock.add(new Boid(Graphics.getRandomColor4(), new Vec3(1.0f, 0.0f, 0.0f), new Vec3(1.0f, 0.0f, 0.0f)));
        flock.add(new Boid(Graphics.getRandomColor4(), new Vec3(1.0f, 1.0f, 0.0f), new Vec3(1.0f, 0.0f, 0.0f)));

    }

    public Flock(int x_bound, int y_bound, int num) {
        flock = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < num; i++) {
            int x = r.nextInt(x_bound);
            int y = r.nextInt(y_bound);
            flock.add(new Boid(Graphics.getRandomColor4(), new Vec3(x, y, 0.0f), new Vec3(x, y, 0.0f), this));
        }
    }

    public void addBoid(String name, float size,  Vec4 color, Vec3 coeffs) {
        Random r = new Random();
        Boid b = new Boid(name, size, color, coeffs, Vec3.zero(), Vec3.randomVec3(), this);
//        Log.d("Created: ", b.toString());
        flock.add(b);
    }

    public void addRandomBoid() {
        addBoid(null, 1.0f, new Vec4(Vec3.randomVec3()), Vec3.randomVec3());
    }

    public void update() {

        for (Boid b :
                flock) {
            Vec3 a = Boid.align(b, flock);
            Vec3 s = Boid.separate(b, flock);
            Vec3 c = Boid.cohesion(b, flock);
            b.applyIncentives(a,s,c);

        }

        for (Boid b :
                flock) {
            b.update();
        }
    }

    public void gravitate(float v, Vec3 p) {
        Vec3 pp = p.xy();
        for (Boid b :
                flock) {
            Vec3 temp = b.seek(pp);
            temp.normalize();
            temp.scale(v);
            b.applyForce(temp);
            b.updateAcceleration();
        }
    }
}
