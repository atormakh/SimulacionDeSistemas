package gasDiffusion;

import java.io.IOException;
import java.util.*;
import java.io.FileWriter;

public class GasBox {

    int numParticles;
    double holeSize;
    float threshold;
    Double BOX_HEIGHT = 0.09, BOX_WIDTH = 0.24, PARTICLE_MASS = 1.0, INITIAL_VELOCITY = 0.1, PARTICLE_RADIUS = 0.0015, THRESHOLD = 0.07;
    Random random;
    List<Particle> particles;
    Queue<Event> queue;
    double time;

    public GasBox(double holeSize, int numParticles, int seed, float threshold) {
        this.holeSize = holeSize;
        this.numParticles = numParticles;
        this.random = new Random(seed);
        this.threshold = threshold;
        this.time = 0;
        particles = new ArrayList<>();
        queue = new PriorityQueue<>();
    }

    private boolean isCollision(Double x, Double y) {
        for (Particle particle : particles) {
            if (Math.sqrt(Math.pow(particle.x - x, 2) + Math.pow(particle.y - y, 2)) <= 2 * PARTICLE_RADIUS) {
                return true;
            }
        }
        return false;
    }


    private void calculateEventsForParticle(Particle particle) {
        double tc;
        //Choque con el borde izq/der
        tc = particle.vx > 0 ? (BOX_WIDTH - particle.radius - particle.x) / particle.vx : (particle.radius - particle.x) / particle.vx;
        queue.add(new EventX(tc + time, particle));

        //Choque con el borde sup/inf
        tc = particle.vy > 0 ? (BOX_HEIGHT - particle.radius - particle.y) / particle.vy : (particle.radius - particle.y) / particle.vy;
        queue.add(new EventY(tc + time, particle));


        //Choque con el medio
        Event ev = null;
        if (particle.x < BOX_WIDTH / 2 && particle.vx > 0) {
            tc = (BOX_WIDTH / 2 - particle.radius - particle.x) / particle.vx;
            ev = new EventX(tc + time, particle);

        } else if (particle.x > BOX_WIDTH / 2 && particle.vx < 0) {
            tc = (BOX_WIDTH / 2 + particle.radius - particle.x) / particle.vx;
            ev = new EventX(tc + time, particle);
        }
        //Chequeamos que la particula no pase en X por el tabique pero por el agujero
        if (ev != null) {
            double y = particle.y + particle.vy * tc;
            if (y < BOX_HEIGHT / 2 - holeSize / 2 || y > BOX_HEIGHT / 2 + holeSize / 2)
                queue.add(ev);
            else if (y == BOX_HEIGHT / 2 - holeSize / 2 || y == BOX_HEIGHT / 2 + holeSize / 2) { //Choque con extremos tabique
                //queue.add(new ObstacleEvent(ev.getTime(), particle));
            }
        }

        //Choque con otras partículas
        for (Particle particle2 : particles) {
            if (particle != particle2) {

                double dvx = particle2.vx - particle.vx;
                double dvy = particle2.vy - particle.vy;
                double dvdv = dvx * dvx + dvy * dvy;

                double dx = particle2.x - particle.x;
                double dy = particle2.y - particle.y;

                double drdr = dx * dx + dy * dy;

                double dvdr = dvx * dx + dvy * dy;

                double sigma = particle.radius + particle2.radius;

                double d = dvdr * dvdr - dvdv * (drdr - sigma * sigma);


                if (dvdr < 0 && d > 0) {
                    tc = -(dvdr + Math.sqrt(d)) / dvdv;
                    queue.add(new CollisionEvent(tc+time, particle, particle2));
                }


            }
        }

    }

    private void calculateInitialEvents() {
        for (Particle particle : particles) {
            calculateEventsForParticle(particle);
        }
    }

    public void run(int maxIterations, FileWriter out) throws IOException {
        //1) Se definen las posiciones y velocidades iniciales, los radios y tamaño de la caja.
        for (int i = 0; i < numParticles; i++) {
            Double x, y;
            do {
                x = random.nextDouble() * (BOX_WIDTH / 2 - 2 * PARTICLE_RADIUS) + 2 * PARTICLE_RADIUS;
                y = random.nextDouble() * (BOX_HEIGHT - 2 * PARTICLE_RADIUS) + 2 * PARTICLE_RADIUS;
            } while (isCollision(x, y));


            double angle = random.nextDouble() * 2 * Math.PI;

            double vx = INITIAL_VELOCITY * Math.cos(angle);
            double vy = INITIAL_VELOCITY * Math.sin(angle);

            particles.add(new Particle(x, y, vx, vy, PARTICLE_MASS, PARTICLE_RADIUS));
        }

/*
        numParticles = 2;
        particles.add(new Particle(BOX_WIDTH/2 - BOX_WIDTH*0.2, BOX_HEIGHT/2, INITIAL_VELOCITY, 0d, PARTICLE_MASS, PARTICLE_RADIUS));
        particles.add(new Particle(BOX_WIDTH/2 -2*PARTICLE_RADIUS, BOX_HEIGHT/2, -INITIAL_VELOCITY, 0d, PARTICLE_MASS, PARTICLE_RADIUS));
*/

        out.write(numParticles + " " + holeSize + "\n");

        out.write(time + " 1 0\n");
        for (Particle particle : particles) {
            out.write(particle.x + " " + particle.y + " " + particle.vx + " " + particle.vy + "\n");
        }

        System.out.println("Calculating initial events");
        calculateInitialEvents();


        double a;
        int iter = 0;
        double delta;
        while (iter <= maxIterations && (a = calculateBalance(particles)) >= (0.5 - THRESHOLD)) {
            iter++;

;
            Event event;
            do {
                event = queue.poll();
                if (event == null) {
                    System.out.println("No hay eventos");
                    System.exit(1);
                }
            } while (!event.isValid());

            System.out.printf("%.2f %.2f %s \n",time,a, event.getClass().getName());

            delta = event.getTime() - time;
            time = event.getTime();
            out.write(time + " " + a + " " + (1 - a) + "\n");

            // Se evolucionan todas las partículas según sus ecuaciones de movimiento hasta tc .
            for (Particle particle : particles) {
                particle.update(delta);
            }

            //se determinan las nuevas velocidades después del choque, solo para las partículas que chocaron.
            event.updateParticles();

            //Se guarda el estado del sistema (posiciones y velocidades) en t = tc
            for (Particle particle : particles) {
                out.write(particle.x + " " + particle.y + " " + particle.vx + " " + particle.vy + "\n");
            }

            //Se invalidan los eventos para las partículas que han chocado.
            for (Event ev : queue) {
                for (Particle particle : event.getParticles()) {
                    if (ev.getParticles().contains(particle)) {
                        ev.invalidate();
                    }
                }
            }

            //Se calculan los nuevos eventos para las partículas que han chocado.
            for (Particle particle : event.getParticles()) {
                calculateEventsForParticle(particle);
            }

        }
    }

    //Calcula el porcentaje de particulas en el primer rectangulo
    private Double calculateBalance(List<Particle> particles) {
        int amount = 0;
        for (Particle particle : particles) {
            if (isInLeftRectangle(particle)) amount++;
        }
        return amount * 1.0 / numParticles;
    }

    public boolean isInLeftRectangle(Particle particle) {
        return particle.x < BOX_WIDTH / 2;
    }
}