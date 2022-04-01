import ovito

def animate_particles():
    static = ovito.io.import_file("wall.xyz", columns=["Position.X","Position.Y","Position.Z", "Color.R"])
    particles = ovito.io.import_file("lattice_gas.xyz",columns=["Position.X", "Position.Y", "Position.Z"])

    particles.modifiers.append(ovito.modifiers.ComputePropertyModifier(output_property="Radius", expressions=["0.5"]))

    particles.add_to_scene()
    static.add_to_scene()

    wall = static.compute()

    vp = ovito.vis.Viewport()
    vp.type = ovito.vis.Viewport.Type.TOP

    x = max([x[0] for x in wall.particles.positions])
    y = max([x[1] for x in wall.particles.positions])

    vp.camera_pos = (x/2, y/2 ,0)

    ratio = y/x
    size = 1000
    vp.render_anim(size=(size,int(size*ratio)), filename="particles.avi", fps=20, background=(0,0,0))


if __name__ == "__main__":
    animate_particles()