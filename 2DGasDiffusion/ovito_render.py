import ovito


def particle_modifier(frame, data):
    data.cell.vis.enabled = False
    print("\r{}".format(frame), end="")

def wall_modifier(frame, data):
    data.cell.vis.enabled = False
    data.particles.display.shape = ovito.vis.ParticlesVis.Shape.Square




def animate_particles():
    static = ovito.io.import_file(
        "wall.xyz", columns=["Position.X", "Position.Y", "Radius", "Color.R"])
    particles = ovito.io.import_file("gas.xyz", columns=[
                                     "Position.X", "Position.Y","Displacement.X","Displacement.Y", "Radius"])

    particles.modifiers.append(particle_modifier)
    static.modifiers.append(wall_modifier)
    particles.add_to_scene()
    static.add_to_scene()

    wall = static.compute()

    vp = ovito.vis.Viewport()
    vp.type = ovito.vis.Viewport.Type.TOP

    x = max([x[0] for x in wall.particles.positions])
    y = max([x[1] for x in wall.particles.positions])

    vp.camera_pos = (x/2, y/2, 0)

    ratio = y/x
    size = 1000
    vp.render_anim(size=(size, int(size*ratio/2)*2),
                   filename="particles.avi", fps=20, background=(0, 0, 0))
    static.remove_from_scene()
    particles.remove_from_scene()


if __name__ == "__main__":
    animate_particles()
