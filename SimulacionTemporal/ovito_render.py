import ovito


def particle_modifier(frame, data):
    data.cell.vis.enabled = False
    data.particles.display.radius = 1e-9
    print("\r{}".format(frame), end="")

def grid_modifier(frame, data):
    data.cell.vis.enabled = False
    data.particles.display.radius = 1e-9

    #data.particles.display.shape = ovito.vis.ParticlesVis.Shape.Square




def animate():
    static = ovito.io.import_file(
        "grid.xyz", columns=["Position.X", "Position.Y", "Color.R", "Color.G","Color.B"])
    particles = ovito.io.import_file("dynamic.xyz", columns=[
                                     "Position.X", "Position.Y", "Color.R"])

    particles.modifiers.append(particle_modifier)
    static.modifiers.append(grid_modifier)
    modifier = ovito.modifiers.GenerateTrajectoryLinesModifier(only_selected = False)
    particles.modifiers.append(modifier)
    modifier.vis.width = 1e-10
    modifier.vis.upto_current_time = True
    modifier.generate()

    particles.add_to_scene()
    static.add_to_scene()

    wall = static.compute()

    vp = ovito.vis.Viewport()
    vp.type = ovito.vis.Viewport.Type.TOP
    vp.fov = 1e-7
    x = max([x[0] for x in wall.particles.positions])
    y = max([x[1] for x in wall.particles.positions])

    vp.camera_pos = (x/2, y/2, 0)
    #vp.zoom_all()

    #ratio = y/x
    #size = 1000
    vp.render_anim(size=(600,600),
                   filename="simulation.avi", fps=15, background=(0, 0, 0))
    static.remove_from_scene()
    particles.remove_from_scene()


