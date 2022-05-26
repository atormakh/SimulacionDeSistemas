import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Environment {
    List<Entity> entities = new LinkedList<>();
    List<Zombie> zombies = new LinkedList<>();
    double inactiveZombieVelocity = 0.3;
    double initialZombieDistance = 1;

    double t = 0;
    double dt = 1e-3;
    double radius;

    public Environment(double dt, double radius) {
        this.radius = radius;
    }

    public void printToFile(FileWriter out) throws IOException {
        out.write(t + "\n");
        for (Entity entity : entities) {
            char zombie = entity instanceof Zombie ? 'z' : 'h';
            out.write(entity.position.x + " " + entity.position.y + " " + entity.velocity.x + " " + entity.velocity.y + " " + zombie + "\n");
        }
    }

    void addZombie() {
        Zombie zombie = new Zombie(0, 0, inactiveZombieVelocity, activeZombieVelocity);
        //Zombie zombie = new Zombie(new Vec2(0,0), new Vec2(inactiveZombieVelocity,inactiveZombieVelocity), new Vec2(0,0));

        entities.add(zombie);
        zombies.add(zombie);
    }

    void addHuman() {
        Human human = new Human(0, 0, humanVel);
        do {
            double mod = Math.random() * radius;
            double angle = Math.random() * 2 * Math.PI;
            human.radius = Math.random();
            human.position.x = mod * Math.cos(angle);
            human.position.y = mod * Math.sin(angle);

        } while (!nearZombie(human) && !isColliding(human));

        entities.add(human);


    }

    private boolean isColliding(Entity entity) {
        for (Entity e : entities) {
            if (e.position.dist(entity.position) < (e.radius + entity.radius)) return true;
        }
        return false;
    }

    private boolean nearZombie(Entity entity) {
        for (Zombie zombie : zombies) {
            if (zombie.position.dist(entity.position) < (initialZombieDistance + entity.radius + entity.radius))
                return true;
        }
        return false;
    }

    public void update() {
        entities.stream().forEach((e)->{
            if ( e instanceof Zombie) {
                //Chequear si hay algun humano a menos de 4 o 5 metros de distancia
                //Caso contrario seguir moviendose en la direccion en la que venia
                for (Entity entity:entities){
                    if(entity instanceof Human) {
                        double dist = e.position.dist(entity.position) - e.radius - entity.radius;
                        if (dist < 4) {
                            //Zombie se activa y se mueve en esa direccion
                        } else if (dist < 5) {
                            //Zombie NO se activa pero se mueve en esa direccion
                        }
                    }
                }

            }
            });

    }
}
