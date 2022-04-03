import ovito


def flow_modifier(frame, data):
    data.particles.displacement.vis.enabled = True
    data.particles.displacement.display.alignment = ovito.vis.VectorVis.Alignment.Center
    data.particles.displacement.display.width = 0.3
    data.particles.display.radius = 0
    data.cell.vis.enabled = False
    print("\r{}".format(frame), end="")


def particle_modifier(frame, data):
    print("\r{}".format(frame), end="")
    data.particles.display.radius = 0.5


def density_modifier(frame, data, avg_grid):
    data.particles.display.shape = ovito.vis.ParticlesVis.Shape.Square
    data.particles.display.radius = avg_grid/2
    data.cell.vis.enabled = False

    print("\r{}".format(frame), end="")


def animate_particles():
    static = ovito.io.import_file(
        "wall.xyz", columns=["Position.X", "Position.Y", "Position.Z", "Color.R"])
    particles = ovito.io.import_file("lattice_gas.xyz", columns=[
                                     "Position.X", "Position.Y", "Position.Z"])

    particles.modifiers.append(particle_modifier)

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
    vp.render_anim(size=(size, int(size*ratio)),
                   filename="particles.avi", fps=20, background=(0, 0, 0))
    static.remove_from_scene()
    particles.remove_from_scene()


def animate_flow():
    static = ovito.io.import_file(
        "wall.xyz", columns=["Position.X", "Position.Y", "Position.Z", "Color.R"])
    flow = ovito.io.import_file("flow.xyz", columns=[
                                "Position.X", "Position.Y", "Displacement.X", "Displacement.Y"])

    static.add_to_scene()
    flow.add_to_scene()

    flow.modifiers.append(flow_modifier)

    wall = static.compute()

    vp = ovito.vis.Viewport()
    vp.type = ovito.vis.Viewport.Type.TOP

    x = max([x[0] for x in wall.particles.positions])
    y = max([x[1] for x in wall.particles.positions])

    vp.camera_pos = (x/2, y/2, 0)

    ratio = y/x
    size = 500
    vp.render_anim(size=(size, size), filename="flow.avi",
                   fps=15, background=(0, 0, 0))
    static.remove_from_scene()
    flow.remove_from_scene()


def animate_density(avg_grid=10):
    static = ovito.io.import_file(
        "wall-no-transform.xyz", columns=["Position.X", "Position.Y", "Position.Z", "Color.R"])
    density = ovito.io.import_file("density.xyz", columns=[
                                   "Position.X", "Position.Y", "Transparency"])

    static.add_to_scene()
    density.add_to_scene()

    density.modifiers.append(
        lambda frame, data: density_modifier(frame, data, avg_grid))

    wall = static.compute()

    vp = ovito.vis.Viewport()
    vp.type = ovito.vis.Viewport.Type.TOP

    x = max([x[0] for x in wall.particles.positions])
    y = max([x[1] for x in wall.particles.positions])

    vp.camera_pos = (x/2, y/2, 0)

    ratio = y/x
    size = 500
    vp.render_anim(size=(size, size), filename="density.avi",
                   fps=15, background=(0, 0, 0))
    static.remove_from_scene()
    density.remove_from_scene()


def animate():
    static = ovito.io.import_file(
        "wall.xyz", columns=["Position.X", "Position.Y", "Position.Z", "Color.R"])
    particles = ovito.io.import_file("lattice_gas.xyz", columns=[
                                     "Position.X", "Position.Y", "Position.Z"])
    flow = ovito.io.import_file("flow.xyz", columns=[
                                "Position.X", "Position.Y", "Displacement.X", "Displacement.Y"])

    static.add_to_scene()
    particles.add_to_scene()
    flow.add_to_scene()

    flow.modifiers.append(flow_modifier)
    particles.modifiers.append(particle_modifier)

    wall = static.compute()

    vp = ovito.vis.Viewport()
    vp.type = ovito.vis.Viewport.Type.TOP

    x = max([x[0] for x in wall.particles.positions])
    y = max([x[1] for x in wall.particles.positions])

    vp.camera_pos = (x/2, y/2, 0)

    ratio = y/x
    size = 1000
    vp.render_anim(size=(size, int(size*ratio)),
                   filename="animation.avi", fps=20, background=(0, 0, 0))

    static.remove_from_scene()
    particles.remove_from_scene()
    flow.remove_from_scene()


if __name__ == "__main__":
    # animate_particles()
    # animate_flow()
    animate_density(20)
    # animate()
