import ovito

def particle_modifier(frame, data):
    data.cell.vis.enabled = False
    data.particles.display.shape = ovito.vis.ParticlesVis.Shape.Circle
    print("\r{}".format(frame), end="")

def wall_modifier(frame, data):
    data.cell.vis.enabled = False
    data.particles.display.shape = ovito.vis.ParticlesVis.Shape.Circle





def animate():
    static = ovito.io.import_file(
        "static.xyz", columns=["Position.X", "Position.Y", "Radius"])
    particles = ovito.io.import_file("zombies.xyz", columns=[
                                     "Position.X", "Position.Y", "Radius", "Color.R", "Color.G"])

    particles.modifiers.append(particle_modifier)
    static.modifiers.append(wall_modifier)
    
    particles.add_to_scene()
    static.add_to_scene()


    vp = ovito.vis.Viewport()
    vp.type = ovito.vis.Viewport.Type.TOP

    vp.fov = 12
    vp.camera_pos = (0,0 , 0)

    
    
    vp.render_anim(size=(800, 600),
                   filename="zombies.mp4", fps=20, background=(0, 0, 0), every_nth=1)
    static.remove_from_scene()
    particles.remove_from_scene()


if __name__ == "__main__":
    import data_import, ovito_export
    data = data_import.Data("zombies.txt")
    ovito_export.save("zombies.xyz", data)
    animate()
